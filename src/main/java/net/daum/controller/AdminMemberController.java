package net.daum.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import net.daum.service.AdminMemberService;
import net.daum.vo.MemberVO;
import net.daum.vo.PageVO;
import pwdconv.PwdChange;

@Controller
public class AdminMemberController {

	@Autowired
	private AdminMemberService adminMemberService;

	//관리자 회원목록
	@GetMapping("/admin_member_list")
	public ModelAndView admin_member_list(HttpServletResponse response,HttpSession session,
			PageVO p,HttpServletRequest request) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		
		if(isLogin(session, response)) {//관리자로 로그인 된 상태=>관리자 회원목록(페이징과 검색기능)
			int page = 1;//쪽번호
		    int limit = 7;//한페이지에 보여지는 목록 개수
		    if(request.getParameter("page") != null) {//get방식으로 전달된 쪽번호가 있는 경우
		    	page = Integer.parseInt(request.getParameter("page"));//쪽번호를 정수 숫자로 변경해서 저장
		    }
		    String find_name = request.getParameter("find_name");//검색어
		    String find_field = request.getParameter("find_field");//검색 필드
		    p.setFind_name("%"+find_name+"%");//%는 sql문에서 하나이상의 임의의 모르는 문자와 매핑대응=>검색기능
		    p.setFind_field(find_field);//검색 필드 저장
		    
		    int memberCount = this.adminMemberService.getRowCount(p);//검색전 전체회원수,검색전후 회원수
		    
		    p.setStartrow((page-1)*7+1);//쪽나누기인 페이징에서 시작행번호
		    p.setEndrow(p.getStartrow()+limit-1);//끝행번호
		    
		    List<MemberVO> mlist=this.adminMemberService.getMemberList(p);//검색전후 회원목록
		    
		    /* 페이징 연산 */
		    int maxpage = (int)((double)memberCount/limit+0.95);//총페이지 수
		    int startpage = (((int)((double)page/10+0.9))-1)*10+1;//현재 페이지에 보여질 시작페이지
		    int endpage = maxpage;//현재 페이지에 보여질 마지막 페이지
		    if(endpage > startpage+10-1) endpage = startpage+10-1;
		    
		    ModelAndView listM = new ModelAndView();
		    listM.addObject("mlist", mlist);
		    listM.addObject("page", page);
		    listM.addObject("startpage", startpage);
		    listM.addObject("endpage", endpage);
		    listM.addObject("maxpage", maxpage);
		    listM.addObject("memberCount", memberCount);
		    listM.addObject("find_field", find_field);
		    listM.addObject("find_name", find_name);
		    
		    listM.setViewName("admin/admin_member_list");//메서드 인자값으로 뷰페이지 경로나 매핑주소 경로를
		    //넣을 수 있다.
		    return listM;
		}
		return null;
	}//admin_member_list()
	
	//관리자 회원관리 상세정보와 수정폼
	@GetMapping("/admin_member_info")
	public ModelAndView admin_member_info(String mem_id,String state,int page,
			HttpServletResponse response,HttpSession session) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		
		if(isLogin(session, response)) {
			MemberVO am=this.adminMemberService.getMemberInfo(mem_id);//아이디에 해당하는 회원정보를
			//읽어옴
			
			String del_cont = null;
			if(am.getMem_delcont() != null) {//탈퇴 사유가 있는 경우
				del_cont = am.getMem_delcont().replace("\n", "<br>");//textarea에서 엔터키를 친부분을
				//줄바꿈						
			}
			
			String[] phone = {"010","011","019"};
			String[] email = {"gmail.com","naver.com","daum.net","nate.com","직접입력"};
			
			ModelAndView m=new ModelAndView();
			m.addObject("phone", phone);//phone키이름에 phone배열을 저장
			m.addObject("email", email);
			m.addObject("am", am);
			m.addObject("del_cont", del_cont);
			m.addObject("page", page);
			
			if(state.equals("info")) {//관리자 회원 상세정보 보기
				m.setViewName("admin/admin_member_info");
			}else if(state.equals("edit")) {//관리자 회원정보 수정폼
				m.setViewName("admin/admin_member_edit");
			}
			return m;
		}
		return null;
	}//admin_member_info()
	
	//관리자로 회원정보수정 완료
	@PostMapping("/admin_member_edit")
	public String admin_member_edit(MemberVO m,HttpServletResponse response,HttpSession
			session,int page) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		if(isLogin(session, response)) {
			m.setMem_pwd(PwdChange.getPassWordToXEMD5String(m.getMem_pwd()));//비번 암호화
			this.adminMemberService.editM(m);
			/* 문제) 아이디를 기준으로 비번,회원이름,우편번호,주소,폰번호,멜주소,가입탈퇴 회원 구분값(1,2),탈퇴사유를
			 * 수정되게 만들어 보자. admin_member.xml에서 설정할 유일 아이디명은 'am_edit'이다. 
			 */
			
			out.println("<script>");
			out.println("alert('관리자로 사용자 회원정보를 수정했습니다!');");
			out.println("location='admin_member_info?state=info&mem_id="+m.getMem_id()
			                      +"&page="+page+"';");
			out.println("</script>");
		}
		return null;
	}//admin_member_edit()
	
	//관리자로 회원삭제
	@GetMapping("/admin_member_del")
	public ModelAndView admin_member_del(String mem_id, int page,HttpServletResponse 
			response,HttpSession session) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		
		if(isLogin(session, response)) {
			this.adminMemberService.delM(mem_id);//관리자에서 아이디를 기준으로 사용자 회원삭제
			
			return new ModelAndView("redirect:/admin_member_list?page="+page);
		}
		
		return null;
	}//admin_member_del()

	//반복적인 코드를 하나로 줄이기
	public static boolean isLogin(HttpSession session,HttpServletResponse response)
			throws Exception{
		PrintWriter out=response.getWriter();
		String admin_id=(String)session.getAttribute("admin_id");//관리자 세션 아이디값을 구함

		if(admin_id == null) {
			out.println("<script>");
			out.println("alert('관리자로 다시 로그인 하세요!');");
			out.println("location='admin_Login';");
			out.println("</script>");

			return false;
		}
		return true;
	}//isLogin()
}
