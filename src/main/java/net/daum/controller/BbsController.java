package net.daum.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.daum.service.BbsService;
import net.daum.vo.Bbs2VO;
import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Controller //@Controller 애노테이션을 설정해서 스프링에서 컨트롤러로 인식하게 한다.
public class BbsController {//사용자 자료실 스프링 컨트롤러

	@Autowired
	private BbsService bbsService;
	
	//사용자 자료실 글쓰기폼
	@GetMapping(value="/bbs_write") //get으로 접근하는 bbs_write 매핑주소 처리
	public ModelAndView bbs_write(HttpServletRequest request) {
		/* 문제) 페이징 목록에서 내가 본 쪽번호로 이동되게 책갈피 기능을 만들어 본다.이 기능을
		 * 북마크라고도 한다. 
		 */
		int page=1;
		if(request.getParameter("page") != null) {
			page=Integer.parseInt(request.getParameter("page"));
		}
		
		ModelAndView wm=new ModelAndView();
		wm.addObject("page", page);
		wm.setViewName("bbs/bbs_write");//메서드 인자값으로 뷰페이지 경로와 파일명
		//=>/WEB-INF/views/bbs/bbs_write.jsp
		
		return wm;
	}//bbs_write()
	
	//자료실 저장
	@PostMapping("/bbs_write_ok")
	public String bbs_write_ok(BbsVO bbs,Bbs2VO bbs2,HttpServletRequest
			request) {
		//스프링 api인 MultipartFile 타입을 사용해서 업로드 되는 파일데이터를 쉽게 처리
		
		String uploadFolder = 
		 request.getSession().getServletContext().getRealPath("upload");
		//첨부파일 업로드 실제 경로를 구함
		
		MultipartFile uploadFile = bbs2.getUploadFile();//첨부파일을 가져옴
		
		if(!uploadFile.isEmpty()) {//첨부파일이 있는 경우 실행
          
			String fileName = uploadFile.getOriginalFilename();//첨부원본파일명
            Calendar c=Calendar.getInstance();//칼렌더는 추상클래스로 new로 객체생성
            //못함, 하지만 년월일 시분초 값을 반환할 때는 유용하게 사용
            int year=c.get(Calendar.YEAR);//년도
            int month=c.get(Calendar.MONTH)+1;//월값, +1을 하는 이유는 1월이 0
            //으로 반환되기 때문이다.
            int date=c.get(Calendar.DATE);
            
            String homedir=uploadFolder+"/"+year+"-"+month+"-"+date;
            //오늘날짜 폴더경로 저장
            File path01=new File(homedir);
            
            if(!(path01.exists())) {//오늘날짜 폴더경로가 존재하지 않으면
            	path01.mkdir();//오늘날짜 폴더를 생성
            }
            Random r=new Random();//랜덤클래스는 난수를 발생
            int random = r.nextInt(100000000);//0이상 1억미만사이의 정수숫자 난수발생
            
            /*첨부파일 확장자를 구함*/
            int index=fileName.lastIndexOf(".");//마침표를 맨오른쪽부터 찾아서 가장
            //먼저 나오는 해당문자위치번호를 맨 왼쪽부터 카운터해서 반환, 첫문자는 0부터 시작
            String fileExtendsion=fileName.substring(index+1);//마침표 이후부터
            //마지막 문자까지 구함. 첨부파일 확장자
            String refileName="bbs"+year+month+date+random+"."+fileExtendsion;
            //새로운 파일명 저장
            String fileDBName = "/"+year+"-"+month+"-"+date+"/"+refileName;
            //데이터베이스에 저장될 레코드값
            
            bbs.setBbs_file(fileDBName);
            
            File saveFile = new File(homedir+"/", refileName);
            
            try {
            	uploadFile.transferTo(saveFile);//upload폴더 오늘날짜 폴더에
            	//변경된 파일로 실제 업로드
            }catch(Exception e) {e.printStackTrace();}
		}else {//파일을 첨부하지 않았을때 실행
			String fileDBName="";
			bbs.setBbs_file(fileDBName);
		}//if else
		
		this.bbsService.insertBbs(bbs);//자료실 저장
		
		return "redirect:/bbs_list";//자료실 목록 매핑주소로 이동,redirect:/로 이동
		//하는 방식은 get방식이다.
	}//bbs_write_ok()
	
	//자료실 목록
	@RequestMapping(value="/bbs_list",method=RequestMethod.GET)//GET으로 접
	//근하는 매핑주소를 처리
	public ModelAndView bbs_list(HttpServletRequest request,BbsVO b,
			PageVO p) {
		
		int page=1;//현재쪽번호
		int limit=10;//현재페이지에 보여지는 목록개수,한페이지에 10개 목록이 보여지는 페이징
		if(request.getParameter("page") != null) {
			page=Integer.parseInt(request.getParameter("page"));
			//get으로 전달된 쪽번호를 정수숫자로 변경해서 저장
		}
		
		String find_name=request.getParameter("find_name");//검색어
		String find_field=request.getParameter("find_field");//검색필드
		p.setFind_field(find_field);
		p.setFind_name("%"+find_name+"%");//SQL문에서 % 와일드카드 문자는 하나이상의
		//임의의 모르는 문자와 매핑대응한다.주로 검색할 때 많이 사용한다.
		
		int totalCount = this.bbsService.getTotalCount(p);
		//검색 전후 레코드 개수
		//System.out.println("총게시물수:"+totalCount+"개");
		/*문제)bbs.xml에 설정할 유일한 select아이디명은 bbs_count해서 총레코드 개수를
		 * 구하는 getTotalCount()메서드를 만들어 보자.
		 */
		
		/*페이징(쪽나누기) 관련 코드*/
		p.setStartrow((page-1)*10+1);//시작행번호
		p.setEndrow(p.getStartrow()+limit-1);//끝행번호
		
		List<BbsVO> blist = this.bbsService.getBbsList(p);// 페이징 목록
		
		/*페이징 연산*/
		int maxpage=(int)((double)totalCount/limit+0.95);//총 페이지 수
		int startpage=(((int)((double)page/10+0.9))-1)*10+1;
		//현재 페이지에 보여질 시작 페이지
		int endpage=maxpage;//현재 페이지에 보여질 마지막 페이지
		
		if(endpage > startpage+10-1) endpage=startpage+10-1;
				
		ModelAndView listM=new ModelAndView("bbs/bbs_list");//생성자 인자값으로
		//뷰페이지 경로 설정 => /WEB-INF/views/bbs/bbs_list.jsp
		listM.addObject("totalCount", totalCount);//왼쪽의 totalCount키이름에 
		//레코드 개수를 저장,bbs_list.jsp에서 EL에서 키이름을 참조해서 값을 가져온다.
		listM.addObject("blist", blist);//목록
		listM.addObject("page", page);//현재 쪽번호
		listM.addObject("startpage", startpage);//시작페이지
		listM.addObject("endpage", endpage);//마지막 페이지
		listM.addObject("maxpage", maxpage);//최대 페이지
		listM.addObject("find_field", find_field);//검색필드
		listM.addObject("find_name", find_name);//검색어
		
		return listM;
	}//bbs_list()
	
	//자료실 내용보기+답변폼+수정폼+삭제폼
	@RequestMapping("/bbs_cont") //get or post로 접근하는 매핑주소를 처리
	public ModelAndView bbs_cont(int bbs_no,int page, 
			@RequestParam("state") String state,BbsVO b) {
		/* @RequestParam("state")의 뜻은 request.getParameter("state")와
		 * 같은 기능을 한다.
		 */
		if(state.equals("cont")) {//내용보기 일때만 조회수 증가
		   b = this.bbsService.getBbsCont(bbs_no);
		}else {//답변폼,수정폼,삭제폼일때는 조회수 증가 안함
		   b = this.bbsService.getBbsCont2(bbs_no);	
		}
		
		String bbs_cont=b.getBbs_cont().replace("\n", "<br>");//textarea
		//에서 엔터키를 친 부분을 줄바꿈처리
		
		ModelAndView cm=new ModelAndView();
		cm.addObject("b", b);
		cm.addObject("bbs_cont", bbs_cont);
		cm.addObject("page", page);
		
		if(state.equals("cont")) {//내용보기 일때
			cm.setViewName("bbs/bbs_cont");//뷰리졸브 경로 => /WEB-INF/views/
			//bbs/bbs_cont.jsp
		}else if(state.equals("reply")) {//답변폼
			cm.setViewName("bbs/bbs_reply");
		}else if(state.equals("edit")) {//수정폼
			cm.setViewName("bbs/bbs_edit");
		}else if(state.equals("del")) {//삭제폼
			cm.setViewName("bbs/bbs_del");
		}
		return cm;
	}//bbs_cont()
	
	//답변저장
	@RequestMapping(value="/bbs_reply_ok", method=RequestMethod.POST) 
	//post방식으로 접근하는 매핑주소 처리
	public ModelAndView bbs_reply_ok(BbsVO rb, int page) {
		/* bbs_reply.jsp의 네임피라미터이름과 BbsVO.java의 변수명이 같으면 rb객체에
		 * 값이 저장되어있다. page는 빈클래스 변수명에 없기 때문에 별도로 가져와야 한다.
		 */
		this.bbsService.replyBbs(rb);//답변저장
		
		ModelAndView rm=new ModelAndView();
		rm.setViewName("redirect:/bbs_list");
		rm.addObject("page", page);
		return rm;// bbs_list?page=쪽번호 get방식으로 목록보기 매핑주소로 이동한다.
	}//bbs_reply_ok()
	
	//수정완료
	@RequestMapping("/bbs_edit_ok") //get or post방식으로 접근하는 매핑주소 처리 
	public ModelAndView bbs_edit_ok(HttpServletRequest request,
			HttpServletResponse response,BbsVO bbs,Bbs2VO bbs2)
	throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		//웹상에 출력되는 문자와 태그,언어코딩 타입을 지정
		PrintWriter out=response.getWriter();//출력스트림 out생성
		String uploadFolder=
		request.getSession().getServletContext().getRealPath("upload");
		//수정첨부된 파일이 실제 업로드 될 경로
		
		int page=1;
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		
		BbsVO db_pwd=this.bbsService.getBbsCont2(bbs.getBbs_no());//조회수
		//가 증가되지 않은 상태에서 오라클로 부터 비번을 가져옴.
		
		if(!db_pwd.getBbs_pwd().equals(bbs.getBbs_pwd())) {
			out.println("<script>");
			out.println("alert('비번이 다릅니다!');");
			out.println("history.back();");
			out.println("</script>");
		}else {
			MultipartFile uploadFile = bbs2.getUploadFile();//수정 첨부된
			//파일을 가져옴.
			
			if(!uploadFile.isEmpty()) {//수정 첨부된 파일이 있다면
				String fileName = uploadFile.getOriginalFilename();//수정
				//첨부 파일명을 구함
				File delFile =
					new File(uploadFolder+db_pwd.getBbs_file());
				//삭제할 파일 객체 생성
				if(delFile.exists()) {//삭제할 파일이 있다면
					delFile.delete();//기존 첨부파일을 삭제
				}
				Calendar c=Calendar.getInstance();
				int year = c.get(Calendar.YEAR);//년도값
				int month = c.get(Calendar.MONTH)+1;//월값,+1을 한 이유는 1월이
				//0으로 반환되기 때문이다.
				int date = c.get(Calendar.DATE);//일값
				
				String homedir = uploadFolder +"/"+year+"-"+month+"-"+date;
				//오늘날짜 폴더경로 저장
				File path01=new File(homedir);
				if(!(path01.exists())) {
					path01.mkdir();//오늘 날짜 폴더 생성
				}
				Random r=new Random();
				int random = r.nextInt(100000000);
				
				/*첨부파일 확장자 */
				int index=fileName.lastIndexOf(".");
				String fileExtendsion=fileName.substring(index+1);
				String refileName="bbs"+year+month+date+random+"."+
				fileExtendsion;//새로운 이진파일명
				String fileDBName="/"+year+"-"+month+"-"+date+"/"+
				refileName;//db에 저장된 레코드값
				
				bbs.setBbs_file(fileDBName);
				
				File saveFile=new File(homedir+"/",refileName);
				
				try {
					uploadFile.transferTo(saveFile);
				}catch(Exception e) {e.printStackTrace();}
			}else {//수정 첨부하지 않았을 때
				String fileDBName="";
				if(db_pwd.getBbs_file() != null) {//기존 파일이 있다면
					bbs.setBbs_file(db_pwd.getBbs_file());
				}else {
					bbs.setBbs_file(fileDBName);
				}
			}//if else
			
			this.bbsService.editBbs(bbs);
			/* 문제)글번호를 기준으로 글쓴이,글제목,글내용,수정첨부파일만 수정되게 만들어 보자.
			 * bbs.xml에서 설정할 유일아이디명은 bbs_edit 이다.
			 */
			
			ModelAndView em=new ModelAndView("redirect:/bbs_cont");
			em.addObject("bbs_no", bbs.getBbs_no());
			em.addObject("page", page);
			em.addObject("state", "cont");
			return em;//bbs_cont?bbs_no=번호값&page=쪽번호&state=cont
		}
		return null;
	}//bbs_edit_ok()
	
	//삭제완료
	@PostMapping("/bbs_del_ok")
	public String bbs_del_ok(int bbs_no,int page,String del_pwd,
			HttpServletResponse response,HttpServletRequest request)
	throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		//웹브라우저에 출력되는 문자와 태그,언어코딩 타입을 설정
		PrintWriter out=response.getWriter();//출력스트림 out 생성
		String uploadFolder = 
		request.getSession().getServletContext().getRealPath("upload");
		
		BbsVO db_pwd = this.bbsService.getBbsCont2(bbs_no);//조회수가 증가되지
		//않은 상태에서 오라클로 부터 비번을 가져옴.
		
		if(!db_pwd.getBbs_pwd().equals(del_pwd)) {
			out.println("<script>");
			out.println("alert('비번이 다릅니다!');");
			out.println("history.go(-1);");
			out.println("</script>");
		}else {
			this.bbsService.delBbs(bbs_no);
			/* 문제)번호를 기준으로 삭제되게 delBbs()메서드를 만들어 보자. bbs.xml에서
			 * 설정할 유일한 delete 아이디명은 bbs_del로 한다. 주의사항은 오라클로 부터
			 * 레코드가 삭제되게 하고,동시에 upload 폴더하위에 오늘날짜 폴더에 있는 기존 첨부파
			 * 일도 꼭 삭제되는지 확인해 보자.
			 */
			
			if(db_pwd.getBbs_file() != null) {//기존 첨부파일이 있다면
				File delFile = new File(uploadFolder +
						db_pwd.getBbs_file());//삭제 파일 객체 생성
				
				delFile.delete();//폴더는 삭제 안되고 기존 첨부파일만 삭제된다.
			}
			
			return "redirect:/bbs_list?page="+page;
		}//if else
		return null;
	}//bbs_del_ok()
}

























