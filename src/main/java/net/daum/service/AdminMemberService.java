package net.daum.service;

import java.util.List;

import net.daum.vo.MemberVO;
import net.daum.vo.PageVO;

public interface AdminMemberService {

	int getRowCount(PageVO p);
	List<MemberVO> getMemberList(PageVO p);
	MemberVO getMemberInfo(String mem_id);
	void editM(MemberVO m);
	void delM(String mem_id);

}
