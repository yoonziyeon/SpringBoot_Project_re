package net.daum.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.daum.service.AdminBbsService;
import net.daum.vo.Bbs2VO;
import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Controller
public class AdminBbsController {

	@Autowired
	private AdminBbsService adminBbsService;


	//관리자 자료실 목록
	@RequestMapping("/admin_bbs_list")
	public ModelAndView admin_bbs_list(BbsVO b,HttpServletResponse response,
			HttpServletRequest request,HttpSession session,PageVO p) throws Exception{

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();

		String admin_id = (String)session.getAttribute("admin_id");

		if(admin_id == null) {
			out.println("<script>");
			out.println("alert('관리자로 다시 로그인 하세요!');");
			out.println("location='admin_Login';");
			out.println("</script>");
		}else {
			int page=1;//쪽번호
			int limit=7;//한페이지에 보여지는 목록개수
			if(request.getParameter("page") != null) {
				page=Integer.parseInt(request.getParameter("page"));         
			}
			String find_name=request.getParameter("find_name");//검색어
			String find_field=request.getParameter("find_field");//검색 필드
			p.setFind_field(find_field);
			p.setFind_name("%"+find_name+"%");
			//%는 sql문에서 검색 와일드 카드 문자로서 하나이상의 임의의 모르는 문자와 매핑 대응, 하나의 모르는 문자와 매핑 대응하는 와일드 카드문자는 _

			int listcount=this.adminBbsService.getListCount(p);
			//검색전 전체 레코드 개수 또는 검색후 레코드 개수
			//System.out.println("총 게시물수:"+listcount+"개");

			p.setStartrow((page-1)*7+1);//시작행번호
			p.setEndrow(p.getStartrow()+limit-1);//끝행번호

			List<BbsVO> blist=this.adminBbsService.getBbsList(p);//검색 전후 목록

			//총페이지수
			int maxpage=(int)((double)listcount/limit+0.95);
			//현재 페이지에 보여질 시작페이지 수(1,11,21)
			int startpage=(((int)((double)page/10+0.9))-1)*10+1;
			//현재 페이지에 보여줄 마지막 페이지 수(10,20,30)
			int endpage=maxpage;
			if(endpage > startpage+10-1) endpage=startpage+10-1;

			ModelAndView listM=new ModelAndView();

			listM.addObject("blist",blist);//blist 키이름에 값 저장
			listM.addObject("page",page);
			listM.addObject("startpage",startpage);
			listM.addObject("endpage",endpage);
			listM.addObject("maxpage",maxpage);
			listM.addObject("listcount",listcount);   
			listM.addObject("find_field",find_field);
			listM.addObject("find_name", find_name);

			listM.setViewName("admin/admin_bbs_list");//뷰페이지 경로
			return listM;
		}
		return null;
	}//admin_bbs_list()


	//관리자 자료실 글쓰기
	@RequestMapping("/admin_bbs_write")
	public ModelAndView admin_bbs_write(HttpServletResponse response,HttpSession session,
			HttpServletRequest request) throws Exception{
		response.setContentType("text/html;charset=UTF-8");

		if(isAdminLogin(session,response)) {//true 이면 관리자로 로그인 된 상태, ==true 가 생략됨.	
			int page=1;

			if(request.getParameter("page") != null) {
				page=Integer.parseInt(request.getParameter("page"));				
			}

			ModelAndView wm=new ModelAndView("admin/admin_bbs_write");
			wm.addObject("page",page);//페이징에서 책갈피 기능때문에 추가
			return wm;
		}
		return null;
	}//admin_bbs_write()


	//관리자 자료실 저장
	@RequestMapping("/admin_bbs_write_ok")
	public ModelAndView admin_bbs_write_ok(HttpSession session,HttpServletResponse response,
			HttpServletRequest request, BbsVO bbs,Bbs2VO bbs2) throws Exception{
		response.setContentType("text/html;charset=UTF-8");

		if(isAdminLogin(session,response)) {
			String uploadFolder = request.getSession().getServletContext().getRealPath("upload");
			//System.out.println(uploadFolder);
			MultipartFile uploadFile = bbs2.getUploadFile();

			//for (MultipartFile multipartFile : bbs_file) {
			if(!uploadFile.isEmpty()) {//첨부파일이 있다면

				System.out.println("-------------------------------------");
				System.out.println("Upload File Name: " + uploadFile.getOriginalFilename());
				//원래 업로드 원본파일명
				System.out.println("Upload File Size: " + uploadFile.getSize());//업로드 파일크기
				System.out.println("글쓴이 : "+bbs.getBbs_name());

				String fileName=uploadFile.getOriginalFilename();//첨부 원본 파일명
				Calendar cal=Calendar.getInstance();//칼렌더는 추상클래스로 new로 객체 생성을 못함. 년월일 시분초 값을 반환
				int year=cal.get(Calendar.YEAR);//년도값
				int month=cal.get(Calendar.MONTH)+1;//월값, +1을 한 이유는 1월이 0으로 반환 되기 때문에
				int date=cal.get(Calendar.DATE);//일값

				String homedir=uploadFolder+"/"+year+"-"+month+"-"+date;//오늘 날짜 폴더 경로 저장
				File path01=new File(homedir);

				if(!(path01.exists())){
					path01.mkdir();//오늘날짜 폴더 생성
				}
				Random r=new Random();//난수를 발생시키는 클래스
				int random=r.nextInt(100000000);//0이상 1억 미만의 정수 숫자 난수 발생

				/*첨부 파일 확장자를 구함*/
				int index=fileName.lastIndexOf(".");//마침표를 맨 오른쪽부터 찾아서 가장 먼저 나오는 .의 위치번호를 맨 왼쪽부터 카운터 해서 반환
				//첫문자는 0부터 시작
				String fileExtendsion=fileName.substring(index+1);//마침표 이후부터 마지막 문자까지 구함.즉 첨부파일 확장자를 구함.
				String refileName="bbs"+year+month+date+random+"."+fileExtendsion;//새로운 파일명 저장
				String fileDBName="/"+year+"-"+month+"-"+date+"/"+refileName;//데이터베이스에 저장될 레코드값


				bbs.setBbs_file(fileDBName);

				File saveFile = new File(homedir+"/", refileName);

				try {
					uploadFile.transferTo(saveFile);// upload폴더에 오늘날짜로 새롭게 생성된 폴더에 변경된 첨부파일명으로 실제 업로드
				} catch (Exception e) {
					e.printStackTrace();
				} // end catch
			}else {
				System.out.println("글쓴이 : "+bbs.getBbs_name());
				String fileDBName="";
				bbs.setBbs_file(fileDBName);
			}//if else

			this.adminBbsService.insertBbs(bbs);//자료실 저장

			return new ModelAndView("redirect:/admin_bbs_list"); 
		}
		return null;
	}//admin_bbs_write_ok()


	//관리자 자료실 상세정보 보기+수정폼
	@RequestMapping("/admin_bbs_cont")
	public ModelAndView admin_bbs_cont(int no,int page,String state,HttpServletResponse 
			response,HttpSession session) throws Exception{
		response.setContentType("text/html;charset=UTF-8");

		if(isAdminLogin(session, response)) {
			BbsVO bc=this.adminBbsService.getAdminBbsCont(no);//번호에 해당하는 DB레코드 값을 가져온다.
			//System.out.println("자료실 내용:"+bc.getBbs_cont());
			/* 첫번째 문제) 번호에 해당하는 자료실 내용을 출력되게 만들어보고 개발자 테스트 까지 해보자.
			 * getAdminBbsCont(no);메서드를 작성해 보자.
			 * 
			 * 두번째 문제)관리자 자료실 내용이 뷰페이지 출력 안되고 있다. 이유는 textarea에서 엔터키 친 부분을 줄바꿈 처리해
			 * 서 내용부분을 보이게 하는 코드를 추가하면 된다.
			 */
			String bbs_cont=bc.getBbs_cont().replace("\n","<br>");

			ModelAndView cm=new ModelAndView();
			cm.addObject("b", bc);
			cm.addObject("page",page);
			cm.addObject("bbs_cont", bbs_cont);

			if(state.equals("cont")) {
				cm.setViewName("admin/admin_bbs_cont");
			}else if(state.equals("edit")) {//관리자 자료실 수정폼일때 실행
				cm.setViewName("admin/admin_bbs_edit");//뷰페이지 경로
			}
			return cm;
		}
		return null;
	}//admin_bbs_cont()	


	//관리자 자료실 수정
	@RequestMapping("/admin_bbs_edit_ok")
	public ModelAndView admin_bbs_edit_ok(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,BbsVO bbs,Bbs2VO bbs2) throws Exception{
		response.setContentType("text/html;charset=UTF-8");

		if(isAdminLogin(session, response)) {
			PrintWriter out=response.getWriter();
			String uploadFolder=request.getSession().getServletContext().getRealPath("upload");//첨부된 파일 업로드 되는 실제 서버경로			

			int page=1;
			if(request.getParameter("page") != null) {
				page=Integer.parseInt(request.getParameter("page"));
			}	

			BbsVO db_pwd=this.adminBbsService.getAdminBbsCont(bbs.getBbs_no());//조회수가 증가되지 않는 상태에서 오라클로 부터
			//비번을 가져옴.

			MultipartFile uploadFile = bbs2.getUploadFile();//첨부된 파일을 가져옴.

			if(!uploadFile.isEmpty()) {//첨부파일 있는 경우만 실행
				String fileName=uploadFile.getOriginalFilename();////첨부 원본 파일명
				//System.out.println(fileName);
				File delFile=new File(uploadFolder+db_pwd.getBbs_file());//삭제할 파일객체 생성
				if(delFile.exists()) {//삭제할 파일이 있다면
					delFile.delete();//기본 첨부파일을 삭제
				}
				Calendar cal=Calendar.getInstance();
				int year=cal.get(Calendar.YEAR);
				int month=cal.get(Calendar.MONTH)+1;
				int date=cal.get(Calendar.DATE);

				String homedir=uploadFolder+"/"+year+"-"+month+"-"+date;
				File path01=new File(homedir);
				if(!(path01.exists())) {
					path01.mkdir();//오늘 날짜 폴더를 생성
				}
				Random r=new Random();
				int random=r.nextInt(100000000);

				/*첨부 파일 확장자를 구함*/
				int index=fileName.lastIndexOf(".");//마침표 위치번호를 구함.
				String fileExtendsion=fileName.substring(index+1);//.이후부터 마지막 문자까지 구함->결국 
				//첨부파일 확장자만 구함.
				String refileName="bbs"+year+month+date+random+"."+fileExtendsion;//새로운 파일명 저장
				String fileDBName="/"+year+"-"+month+"-"+date+"/"+refileName;
				//오라클 DB에 저장될 레코드값

				//System.out.println(refileName);

				bbs.setBbs_file(fileDBName);

				File saveFile = new File(homedir+"/", refileName);
				try {
					uploadFile.transferTo(saveFile);// upload폴더에 오늘날짜로 새롭게 생성된 폴더에 변경된 첨부파일명으로 실제 업로드
				} catch (Exception e) {
					e.printStackTrace();
				} // end catch


			}else {//수정 첨부파일을 하지 않았을 때
				String fileDBName="";
				if(db_pwd.getBbs_file() != null) {//기존 첨부 파일이 있다면
					bbs.setBbs_file(db_pwd.getBbs_file());
				}else {
					bbs.setBbs_file(fileDBName);
				}
			}//if else

			this.adminBbsService.adminUpdateBbs(bbs);//자료실 수정
			/* 문제) 번호를 기준으로 글쓴이,글제목,글내용,첨부파일을 수정되게 한다. (개발자 테스트까지 해본다.) 
			 */

			ModelAndView em=new ModelAndView("redirect:/admin_bbs_list?page="+page);
			return em;//admin_bbs_list?page=쪽번호 가 get으로 전달된다.
		}
		return null;
	}//admin_bbs_edit_ok()


	//관리자 자료실 삭제
	@RequestMapping("/admin_bbs_del")
	public ModelAndView admin_bbs_del(int no,int page,HttpServletResponse response,HttpSession 
			session,HttpServletRequest request) throws Exception{
		response.setContentType("text/html;charset=UTF-8");

		if(isAdminLogin(session, response)) {
			String saveFolder = request.getSession().getServletContext().getRealPath("upload");
			BbsVO db_file = this.adminBbsService.getAdminBbsCont(no);//DB로 부터 기존 첨부파일을 가져옴.

			if(db_file.getBbs_file() != null) {//기존 첨부파일이 있는 경우
				File delFile=new File(saveFolder+db_file.getBbs_file());//삭제할 파일 객체 생성
				delFile.delete();//기존파일 삭제
			}

			this.adminBbsService.adminBbsDel(no);
			/* 문제)번호를 기준으로 DB로 부터 레코드를 삭제되게 만들어 본다. 
			 */
			return new ModelAndView("redirect:/admin_bbs_list?page="+page);
		}
		return null;
	}//admin_bbs_del()	

	//반복적인 관리자 로그인을 안하기 위한 코드 추가
	public static boolean isAdminLogin(HttpSession session,HttpServletResponse response)
			throws Exception{
		/* 문제) 추가코드 해야 할 부분 ->로그아웃되었을 때 처리 부분
		 */
		PrintWriter out=response.getWriter();
		String admin_id = (String)session.getAttribute("admin_id");//세션 아이디를 구함.

		if(admin_id == null) {//관리자 로그아웃 되었을때 즉 세션 만료 된 경우
			out.println("<script>");
			out.println("alert('관리자로 다시 로그인 하세요!');");
			out.println("location='admin_Login';");
			out.println("</script>");

			return false;
		}
		return true;//관리자 로그인 된 경우
	}//isAdminLogin()
}

























