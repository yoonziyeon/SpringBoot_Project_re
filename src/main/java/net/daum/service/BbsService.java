package net.daum.service;

import java.util.List;

import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

public interface BbsService {

	void insertBbs(BbsVO bbs);
	int getTotalCount(PageVO p);
	List<BbsVO> getBbsList(PageVO p);
	BbsVO getBbsCont(int bbs_no);
	BbsVO getBbsCont2(int bbs_no);
	void replyBbs(BbsVO rb);
	void editBbs(BbsVO bbs);
	void delBbs(int bbs_no);

}
