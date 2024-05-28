package net.daum.dao;

import java.util.List;

import net.daum.vo.GongjiVO;
import net.daum.vo.PageVO;

public interface AdminGongjiDAO {

	int getListCount(PageVO p);
	List<GongjiVO> getGongjiList(PageVO p);
	void insertGongji(GongjiVO g);
	GongjiVO getGongjiCont(int no);
	void editGontji(GongjiVO eg);
	void delGongji(int no);

}
