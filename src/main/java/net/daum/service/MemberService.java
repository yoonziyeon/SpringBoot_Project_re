package net.daum.service;

import java.util.List;

import net.daum.vo.MemberVO;
import net.daum.vo.ZipCodeVO;

public interface MemberService {

	MemberVO idCheck(String id);
	List<ZipCodeVO> zipFind(String dong);
	void insertMember(MemberVO m);
	MemberVO pwdMember(MemberVO m);
	void updatePwd(MemberVO m);
	MemberVO loginCheck(String login_id);
	MemberVO getMember(String id);
	void editMember(MemberVO em);
	void delMem(MemberVO m);

}
