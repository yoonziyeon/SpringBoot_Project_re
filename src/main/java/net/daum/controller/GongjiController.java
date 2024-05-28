package net.daum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.daum.service.GongjiService;
import net.daum.vo.GongjiVO;

@RequestMapping("/gongji/*") //경로 구분하기 위해서 컨트롤러 자체 매핑주소 등록
@Controller
public class GongjiController {

	@Autowired
	private GongjiService gongjiService;
	
	//사용자 최신 공지글 5개보기
	@GetMapping("/gongji_list")
	public ModelAndView gongji_list() {
		
		List<GongjiVO> glist = this.gongjiService.getGongjiList();//최신 공지글 5개
				
		ModelAndView listM = new ModelAndView("gongji/gongji_list");//생성자 인자값으로 뷰페이지 경로설정
		listM.addObject("glist", glist);
		return listM;
	}//gongji_list()
	
	//사용자 공지내용보기와 조회수 증가
	@GetMapping("/gongji_cont")
	public ModelAndView gongji_cont(int gongji_no) {
		
		GongjiVO g = this.gongjiService.getGongjiCont(gongji_no);
		String g_cont = g.getGongji_cont().replace("\n", "<br>");
		
		ModelAndView cm=new ModelAndView();
		cm.setViewName("gongji/gongji_cont");
		cm.addObject("g", g);
		cm.addObject("g_cont", g_cont);
		return cm;
	}//gongji_cont()
}














