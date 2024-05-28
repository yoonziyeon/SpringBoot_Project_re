package net.daum.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.daum.service.MemberService;
import net.daum.vo.MemberVO;
import net.daum.vo.ZipCode2VO;
import net.daum.vo.ZipCodeVO;
import pwdconv.PwdChange;


@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	//로그인 페이지
	@GetMapping("/member_Login")
	public ModelAndView member_login() {
		
		ModelAndView loginM=new ModelAndView();
		loginM.setViewName("member/member_login");//뷰리졸브 경로설정=> /WEB-INF/views/member/
		//member_login.jsp
		return loginM;
	}//member_Login()
	
	//회원가입폼
	@GetMapping("/member_Join")
	public String member_Join(Model m) {

		String[] phone = {"010","011","019"};
		String[] email = {"gmail.com","naver.com","daum.net","nate.com","직접입력"};
		
		m.addAttribute("phone", phone);
		m.addAttribute("email", email);
		return "member/member_join";
	}//member_Join()
	
	//아이디 중복체크
	@PostMapping("/member_Idcheck")
	public String member_idcheck(@RequestParam("id") String id,HttpServletResponse response) 
	throws Exception{
		/* @RequestParam("id") 애노테이션은 request.getParameter("id)와 기능이 같다. 
		 */
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		MemberVO db_id = this.memberService.idCheck(id);//아이디에 해당하는 회원정보를 db로 부터 검색
		
		int result = -1;//중복 아이디가 없을때 반환값
		
		if(db_id != null) {//중복아이디가 있는 경우
			result = 1;
		}
		
		out.println(result);//값 반환
		
		return null;
	}//member_Idcheck()
	
	//우편번호주소 검색 공지창
	@RequestMapping(value="/zip_Find",method=RequestMethod.GET) //get 으로 접근하는 매핑주소 처리
	public ModelAndView zip_find() {
		
		ModelAndView zm=new ModelAndView("member/zip_find");//생성자 인자값으로 뷰페이지경로 전달
		return zm;
	}//zip_Find()
	
	//우편주소 검색 결과
	@RequestMapping(value="/zip_Find_ok", method=RequestMethod.POST) //post로 접근하는 매핑주소 처리
	public ModelAndView zip_Find_ok(String dong) {
		
		List<ZipCodeVO> zlist = this.memberService.zipFind("%"+dong+"%");
		/* % 와일드 카드 문자는 SQL에서 하나이상의 임의의 모르는 문자와 매핑대응한다. 
		 */
		
		List<ZipCode2VO> zlist2 = new ArrayList<>();
		
		for(ZipCodeVO z : zlist) {
			ZipCode2VO z2 = new ZipCode2VO();
			
			z2.setZipcode(z.getZipcode());//우편번호
			z2.setAddr(z.getSido()+" "+z.getGugun()+" "+z.getDong());//시도 구군 동
			
			zlist2.add(z2);//컬렉션에 추가
		}
		
		ModelAndView zm = new ModelAndView("member/zip_find");
		zm.addObject("zipcodelist", zlist2);
		zm.addObject("dong", dong);
		return zm;
	}//zip_Find_ok()
	
	//회원 저장
	@RequestMapping("/member_Join_ok")
	public ModelAndView member_Join_ok(MemberVO m) {
		/* member_join.jsp의 네임피라미터 이름과 MemberVO빈클래스 변수명이 같으면 m객체에 입력한 회원정보가 저장되어
		 * 있다.
		 */
		m.setMem_pwd(PwdChange.getPassWordToXEMD5String(m.getMem_pwd()));//비번 암호화
		this.memberService.insertMember(m);//회원 저장
		
		ModelAndView jm = new ModelAndView();
		jm.setViewName("redirect:/member_Login");//메서드 인자값으로 로그인 매핑주소 경로가 들어감.
		return jm;
	}//member_Join_ok()
	
	//비번찾기 공지창
	@GetMapping("pwd_Find")
	public String pwd_find() {
	   return "member/pwd_Find";
	}//pwd_find()
	
	//비번찾기 결과
	@PostMapping(value="/pwd_Find_ok")
	public ModelAndView pwd_find_ok(String pwd_id,String pwd_name,HttpServletResponse 
			response,MemberVO m) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		m.setMem_id(pwd_id); m.setMem_name(pwd_name);
		MemberVO pm=this.memberService.pwdMember(m);//아이디와 회원이름을 기준으로 DB로부터 회원정보 검색
		
		if(pm == null) {
			out.println("<script>");
			out.println("alert('회원으로 검색되지 않습니다!\\n 올바른 회원정보를 입력하세요!');");
			out.println("history.back();");
			out.println("</script>");
		}else {
		    Random r=new Random();
		    int pwd_random = r.nextInt(100000);//0이상 십만 미만 사이의 정수숫자  난수 발생
		    String ran_pwd = Integer.toString(pwd_random);//정수숫자 비번을 문자열로 변경
		    m.setMem_pwd(PwdChange.getPassWordToXEMD5String(ran_pwd));//임시 비번 암호화
		    
		    this.memberService.updatePwd(m);//암호화된 임시비번으로 수정
		    
		    ModelAndView fm=new ModelAndView("member/pwd_find_ok");//생성자 인자값으로 뷰페이지 경로와
		    //파일명 설정
		    /* 문제) 암호화 된 임시비번으로 수정되게 해 본다. member.xml에 설정할 유일 update 아이디명은 p_edit이다.
		     * 에러가 발생하면 디버깅까지 개발자 테스트 해보자.
		     */
		    
		    fm.addObject("pwd_ran", ran_pwd);
		    return fm;
		}//if else		
		return null;
	}//pwd_find_ok()
	
	//로그인 인증
	@PostMapping("/member_Login_ok")
	public String member_Login_ok(String login_id,String login_pwd,HttpServletResponse 
			response,HttpSession session) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		MemberVO m = this.memberService.loginCheck(login_id);//아이디와 mem_state=1 인 경우만 로그인
		//인증 처리
		/* 문제) 아이디와 가입회원인 mem_state=1인 경우만 로그인 인증처리가 되게 해보자. 에러가 발생하면 디버깅을 해보고
		 * 개발자 테스트까지 해보자. member.xml에서 설정할 유일아이디명은 m_loginCheck로 한다.
		 */
		
		if(m == null) {
			out.println("<script>");
			out.println("alert('가입 안되 회원입니다!');");
			out.println("history.go(-1);");//go(-1)과 back()메서드는 같은 기능을 한다. 이전 주소로 이동
			out.println("</script>");
		}else {
			if(!m.getMem_pwd().equals(PwdChange.getPassWordToXEMD5String(login_pwd))) {
				//암호된 비번끼리 비교해서 같지 않으면
				out.println("<script>");
				out.println("alert('비번이 다릅니다!');");
				out.println("history.back();");
				out.println("</script>");
			}else {
				session.setAttribute("id", login_id);//세션 id에 아이디 저장
				return "redirect:/member_Login";
			}//if else
		}
		return null;
	}//member_Login_ok()
	
	//로그아웃
	@RequestMapping("/member_LogOut")
	public ModelAndView member_logout(HttpServletResponse response,HttpSession session)
	throws Exception{
	  response.setContentType("text/html;charset=UTF-8");
	  PrintWriter out=response.getWriter();
	  
	  session.invalidate();//세션 만료
	  
	  out.println("<script>");
	  out.println("alert('로그아웃 되었습니다!');");
	  out.println("location='member_Login';");
	  out.println("</script>");
	  
      return null;		
	}//member_LogOut()
	
	//회원정보 수정폼
	@GetMapping("/member_Edit")
	public ModelAndView member_Edit(HttpServletResponse response,HttpSession session)
	throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		String id=(String)session.getAttribute("id");
		
		if(isLogin(session, response)) {
			String[] phone = {"010","011","019"};
			String[] email = {"gmail.com","naver.com","daum.net","nate.com","직접입력"};
			MemberVO em=this.memberService.getMember(id);//아이에 해당하는 회원정보를 구함
			
			ModelAndView m=new ModelAndView();
			m.addObject("em", em);
			m.addObject("phone", phone);
			m.addObject("email", email);
			m.setViewName("member/member_edit");//뷰페이지 경로 설정
			return m;
		}
		return null;
	}//member_Edit()
	
	//정보수정 완료
	@PostMapping("/member_Edit_ok")
	public String member_Edit_ok(MemberVO em, HttpServletResponse response,HttpSession session)
	throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		String id = (String)session.getAttribute("id");//세션 아이디값을 구함
		
		if(isLogin(session, response)) {
			em.setMem_id(id);
			em.setMem_pwd(PwdChange.getPassWordToXEMD5String(em.getMem_pwd()));//정식비번 암호화
			
			this.memberService.editMember(em);//정보수정
			
			out.println("<script>");
			out.println("alert('정보 수정했습니다!');");
			out.println("location='member_Edit';");
			out.println("</script>");
		}
		return null;
	}//member_Edit_ok()
	
	//회원탈퇴 폼
	@GetMapping("/member_Del")
	public ModelAndView member_Del(HttpServletResponse response,HttpSession session)
	throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		String id=(String)session.getAttribute("id");
		
		if(isLogin(session, response)) {
			MemberVO dm=this.memberService.getMember(id);
			
			ModelAndView m=new ModelAndView("member/member_del");//생성자 인자값으로 뷰페이지 경로설정
			m.addObject("dm", dm);
			return m;
		}
		return null;
	}//member_Del()
	
	//회원탈퇴 완료
	@PostMapping("/member_Del_Ok")
	public ModelAndView member_Del_Ok(HttpServletResponse response,HttpSession session,
			String del_pwd,String del_cont)	throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String id=(String)session.getAttribute("id");
		
		if(isLogin(session, response)) {
			del_pwd = PwdChange.getPassWordToXEMD5String(del_pwd);
			MemberVO dm = this.memberService.getMember(id);
			
			if(!dm.getMem_pwd().equals(del_pwd)) {
				out.println("<script>");
				out.println("alert('비번이 다릅니다!');");
				out.println("history.go(-1);");
				out.println("</script>");
			}else {
				MemberVO m=new MemberVO();
				m.setMem_id(id); m.setMem_delcont(del_cont);
				
				this.memberService.delMem(m);//회원탈퇴
				/* 문제)아이디를 기준으로 탈퇴사유,탈퇴날짜,mem_state=2 로 변경되게 해보자. member.xml에 설정할
				 * 유일한 update 아이디명은 mDel_ok이다. 수정쿼리문으로 작업한다.
				 */
				
				session.invalidate();//세션 만료=>로그아웃
				
				out.println("<script>");
				out.println("alert('회원 탈퇴했습니다!');");
				out.println("location='member_Login';");
				out.println("</script>");
				
			}//if else
		}		
		return null;
	}//member_Del_Ok()
	
	//반복적인 코드를 하나로 줄이기
	public static boolean isLogin(HttpSession session,HttpServletResponse response)
	throws Exception{
		PrintWriter out=response.getWriter();
		String id=(String)session.getAttribute("id");//세션 아이디값을 구함
		
		if(id == null) {
			out.println("<script>");
			out.println("alert('다시 로그인 하세요!');");
			out.println("location='member_Login';");
			out.println("</script>");
			
			return false;
		}
		return true;
	}//isLogin()
}











