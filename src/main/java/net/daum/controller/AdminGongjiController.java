package net.daum.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import net.daum.service.AdminGongjiService;
import net.daum.vo.GongjiVO;
import net.daum.vo.PageVO;

@Controller
public class AdminGongjiController {

	@Autowired
	private AdminGongjiService adminGongjiService;
	
	//관리자 공지목록
	@GetMapping("/admin_gongji_list")
	public ModelAndView admin_gongji_list(PageVO p,HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		
		if(isLogin(session, response)) {
			int page=1;
			int limit=7;//한페이지에 보여지는 목록개수
			if(request.getParameter("page") != null) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			
			String find_name = request.getParameter("find_name");//검색어
			String find_field = request.getParameter("find_field");//검색 필드
			p.setFind_field(find_field); 
			p.setFind_name("%"+find_name+"%");
			
			int listCount = this.adminGongjiService.getListCount(p);//검색전후 게시물수
			
			/* 페이징 */
			p.setStartrow((page-1)*7+1);//시작행 번호
			p.setEndrow(p.getStartrow()+limit-1);//끝행번호
			
			List<GongjiVO> glist = this.adminGongjiService.getGongjiList(p);//검색전후 공지목록
			
			/*페이징 연산*/			
		    int maxpage = (int)((double)listCount/limit+0.95);//총페이지 수
		    int startpage = (((int)((double)page/10+0.9))-1)*10+1;//현재 페이지에 보여질 시작페이지
		    int endpage = maxpage;//현재 페이지에 보여질 마지막 페이지
		    if(endpage > startpage+10-1) endpage = startpage+10-1;
		    
		    ModelAndView listM = new ModelAndView("admin/admin_gongji_list");
		    listM.addObject("glist", glist);
		    listM.addObject("listcount", listCount);
		    listM.addObject("page", page);
		    listM.addObject("startpage", startpage);
		    listM.addObject("endpage", endpage);
		    listM.addObject("maxpage", maxpage);
		    listM.addObject("find_field", find_field);
		    listM.addObject("find_name", find_name);
		    
		    return listM;
		}
		return null;
	}//admin_gongji_list()
	
	//관리자 공지작성
	@GetMapping("/admin_gongji_write")
	public String admin_gongji_write(int page, HttpServletResponse response,HttpSession 
			session,Model m) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		
		if(isLogin(session, response)) {
			m.addAttribute("page", page);
			return "admin/admin_gongji_write";//뷰페이지 경로 설정
		}		
		return null;
	}//admin_gongji_write()
	
	//관리자 공지저장
	@PostMapping("/admin_gongji_write_ok")
	public ModelAndView admin_gongji_write_ok(GongjiVO g, HttpServletResponse response,
			HttpSession session) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		
		if(isLogin(session, response)){
			this.adminGongjiService.insertGongji(g);//공지 저장
			/* 문제)공지 저장되게 만들어 본다. admin_gongji.xml에서 설정할 유일한 insert 아이디명은 ag_in으로 한다.
			 * 에러가 발생하면 디버깅까지 해보자.
			 */
			
			ModelAndView gm = new ModelAndView();
			gm.setViewName("redirect:/admin_gongji_list");
			return gm;
		}
		return null;
	}//admin_gongji_write_ok()
	
	//관리자 공지내용과 수정폼
	@GetMapping("/admin_gongji_cont")
	public ModelAndView admin_gongji_cont(int no,int page,String state,HttpServletResponse 
			response,HttpSession session) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		
		if(isLogin(session, response)){
			GongjiVO gc=this.adminGongjiService.getGongjiCont(no);
			String g_cont = gc.getGongji_cont().replace("\n", "<br>");//textarea에서 엔터키 친부분을
			//줄바꿈 
			
			ModelAndView cm=new ModelAndView();
			cm.addObject("gc", gc);
			cm.addObject("g_cont", g_cont);
			cm.addObject("page", page);
			
			if(state.equals("cont")) {//공지내용보기
				cm.setViewName("admin/admin_gongji_cont");
			}else if(state.equals("edit")) {//공지수정폼
				cm.setViewName("admin/admin_gongji_edit");
			}
			
			return cm;
		}
		return null;
	}//admin_gongji_cont()
	
	//관리자 공지수정 완료
	@RequestMapping("/admin_gongji_edit_ok") //get or post로 전송되는 매핑주소를 받아서 처리
	public ModelAndView admin_gongji_edit_ok(GongjiVO eg,int no,int page,HttpSession session,
			HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		
		if(isLogin(session, response)) {
			eg.setGongji_no(no);//번호 저장
			this.adminGongjiService.editGongji(eg);//공지 수정
			
			ModelAndView em=new ModelAndView("redirect:/admin_gongji_cont");
			em.addObject("no", no);
			em.addObject("state", "cont");
			em.addObject("page", page);
			return em;//주소창에 노출되는 get방식으로 admin_gongji_cont?no=번호&state=cont&page=쪽번호
			//3개의 인자값이 전달된다.
		}		
		return null;
	}//admin_gongji_edit_ok()
	
	//관리자 공지 삭제
	@RequestMapping(value="/admin_gongji_del",method=RequestMethod.GET) //GET으로 접근하는 매핑주소 처
	//리, admin_gongji_del 매핑주소 등록
	public String admin_gongji_del(int no,int page,HttpServletResponse response,
			HttpSession session) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		
		if(isLogin(session, response)) {
		  
			this.adminGongjiService.delGongji(no);//공지번호를 기준으로 삭제
			/* 문제) mybatis 매퍼태그 유일아이디명을 ag_del로 설정해서 번호를 기준으로 삭제되게 만들어 보자. 
			 */
			
			return "redirect:/admin_gongji_list?page="+page;
		}
		
		return null;
	}//admin_gongji_del()
	
	//관리자 로그아웃 되었을 때 처리하는 부분
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






