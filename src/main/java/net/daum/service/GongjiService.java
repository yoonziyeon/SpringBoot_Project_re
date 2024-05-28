package net.daum.service;

import java.util.List;

import net.daum.vo.GongjiVO;

public interface GongjiService {

	List<GongjiVO> getGongjiList();
	GongjiVO getGongjiCont(int gongji_no);
}
