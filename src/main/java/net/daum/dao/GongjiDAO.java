package net.daum.dao;

import java.util.List;

import net.daum.vo.GongjiVO;

public interface GongjiDAO {

	List<GongjiVO> getGongjiList();
	void updateHit(int gongji_no);
	GongjiVO getGongjiCont(int gongji_no);

}
