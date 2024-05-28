package net.daum.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.daum.vo.MemberVO;
import net.daum.vo.PageVO;

@Repository //@Repository 애노테이션을 설정해야 스프링에서 모델 DAO로 인식한다.
public class AdminMemberDAOImpl implements AdminMemberDAO {

	@Autowired //자동 의존성 주입
	private SqlSession sqlSession;//sqlSession이 mybatis 쿼리문 수행
	
	@Autowired
	private AdminMemberRepository adminMemberRepo;

	@Override
	public int getRowCount(PageVO p) {
		return this.sqlSession.selectOne("am_count", p);
	}//검색전후 회원수	

	@Override
	public List<MemberVO> getMemberList(PageVO p) {
		return this.sqlSession.selectList("am_list", p);//mybatis 에서 selectList()메서드는 하나이상의
		//레코드를 검색해서 컬렉션 List로 반환, am_list는 admin_member.xml에서 설정할 유일 select 아이디명이다.
	}//검색전후 목록

	@Override
	public MemberVO getMemberInfo(String mem_id) {
		//return this.sqlSession.selectOne("am_info", mem_id);
		
		System.out.println(" \n 관리자 회원상세정보와 수정폼(JPA) ======================>");
		MemberVO am = this.adminMemberRepo.getReferenceById(mem_id);
		/*getReferenceById() 내장메서드는 아이디에 해당하는 회원정보가 없을때는 예외 오류가 발생한다. 결국 아이디에 해당하는
		 * 회원정보가 반드시 있는 경우만 사용하길 바란다. NULL인 경우는 예외 오류 처리문제로 사용안하는 것이 좋다.
		 */
		return am;
	}//관리자 회원 상세정보와 수정폼

	@Transactional //javax.transaction.Transactional 패키지를 임포트한다. 
	/* Executing an update/delete query; nested exception is javax.persistence.
	 * TransactionRequiredException: Executing an update/delete query 에러를 방지하기 위해서는 
          @Transactional을 처리해 줘야 한다. 
	 */
	@Override
	public void editM(MemberVO m) {
		//this.sqlSession.update("am_edit", m);	
		
		System.out.println(" \n 관리자로 사용자 회원정보수정(JPA) =======================>");
		this.adminMemberRepo.adminMember_Update(m.getMem_pwd(), m.getMem_name(),
				m.getMem_zip(), m.getMem_zip2(), m.getMem_addr(), m.getMem_addr2(),
				m.getMem_phone01(), m.getMem_phone02(), m.getMem_phone03(),m.getMail_id(),
				m.getMail_domain(), m.getMem_state(), m.getMem_delcont(), m.getMem_id());
	}//관리자로 사용자 회원정보 수정

	@Override
	public void delM(String mem_id) {
		//this.sqlSession.delete("am_del", mem_id);//mybatis에서 delete()메서드는 레코드를 삭제
		
		System.out.println(" \n 관리자로 사용자 회원삭제(JPA) ================>");
		this.adminMemberRepo.deleteById(mem_id);
	}//관리자로 일반회원 삭제
}








