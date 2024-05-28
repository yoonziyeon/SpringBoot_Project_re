package net.daum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.daum.dao.AdminMemberDAO;
import net.daum.vo.MemberVO;
import net.daum.vo.PageVO;

@Service //@Service 애노테이션을 설정하면 스프링에서 서비스로 인식한다.
public class AdminMemberServiceImpl implements AdminMemberService {

	@Autowired
	private AdminMemberDAO adminMemberDao;

	@Override
	public int getRowCount(PageVO p) {
		return this.adminMemberDao.getRowCount(p);
	}

	@Override
	public List<MemberVO> getMemberList(PageVO p) {
		return this.adminMemberDao.getMemberList(p);
	}

	@Override
	public MemberVO getMemberInfo(String mem_id) {
		return this.adminMemberDao.getMemberInfo(mem_id);
	}

	@Override
	public void editM(MemberVO m) {
		this.adminMemberDao.editM(m);		
	}

	@Override
	public void delM(String mem_id) {
		this.adminMemberDao.delM(mem_id);		
	}	
}
