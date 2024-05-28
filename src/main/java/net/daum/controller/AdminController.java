package net.daum.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import net.daum.service.AdminService;
import net.daum.vo.AdminVO;
import pwdconv.PwdChange;

@Controller
public class AdminController {//관리자 컨트롤러 클래스

	@Autowired
	private AdminService adminService;
	
	//관리자 로그인 폼
	@GetMapping("/admin_Login")
	public String admin_login() {
		return "admin/admin_login"; //뷰리졸브 경로=> /WEB-INF/views/admin/admin_login.jsp
	}//admin_Login()
	
	//관리자 정보 저장과 관리자 로그인 인증
	@PostMapping("/admin_Login_Ok")
	public ModelAndView admin_Login_Ok(AdminVO ab,HttpServletResponse response,
			HttpSession session) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		ab.setAdmin_pwd(PwdChange.getPassWordToXEMD5String(ab.getAdmin_pwd()));//관리자 비번 md5
		//암호화
		
		/*ab.setAdmin_no(1);
		ab.setAdmin_name("관리자");
		this.adminService.insertAdmin(ab);//관리자 정보 저장 */
		
		AdminVO admin_info = this.adminService.adminLogin(ab.getAdmin_id());
		
		if(admin_info == null) {
			out.println("<script>");
			out.println("alert('관리자 정보가 없습니다!');");
			out.println("history.go(-1);");
			out.println("</script>");
		}else {
			if(!admin_info.getAdmin_pwd().equals(ab.getAdmin_pwd())) {
				out.println("<script>");
				out.println("alert('관리자 비번이 다릅니다!');");
				out.println("history.back();");
				out.println("</script>");
			}else {
		        session.setAttribute("admin_id", ab.getAdmin_id());//admin_id 세션 아이디 키이름에
		        //관리자 아이디 저장
		        session.setAttribute("admin_name", admin_info.getAdmin_name());
		        
		        return new ModelAndView("redirect:/admin_index");//생성자 인자값으로 뷰페이지 경로나 매핑주
		        //소 경로가 들어간다. 관리자 로그인 인증후 관리자 메인 매핑주소로 이동
			}
		}		
		return null;
	}//admin_Login_Ok()
	
	//관리자 로그인 인증후 메인화면
	@GetMapping("/admin_index")
	public ModelAndView admin_index(HttpServletResponse response,HttpSession session)
	throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		String admin_id = (String)session.getAttribute("admin_id");//관리자 세션아이디를 구함
		
		if(admin_id == null) {
			out.println("<script>");
			out.println("alert('관리자 아이디로 다시 로그인 하세요!');");
			out.println("location='admin_Login';");
			out.println("</script>");
		}else {
			return new ModelAndView("admin/admin_main");//생성자 인자값으로 관리자 메인 뷰페이지 경로설정
		}
		return null;
	}//admin_index()
	
	//관리자 로그아웃
	@PostMapping(value="/admin_logout")
	public ModelAndView admin_logout(HttpServletResponse response, HttpSession session)
	throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		session.invalidate();//세션 만료=>관리자 로그아웃
		
		out.println("<script>");
		out.println("alert('관리자 로그아웃 되었습니다!');");
		out.println("location='admin_Login';");
		out.println("</script>");
		
		return null;
	}//admin_logout()
}















