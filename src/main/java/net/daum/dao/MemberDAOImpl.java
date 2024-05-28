package net.daum.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.daum.vo.MemberVO;
import net.daum.vo.ZipCodeVO;

@Repository
public class MemberDAOImpl implements MemberDAO {

	@Autowired //자동 의존성 주입
	private SqlSession sqlSession; //mybatis 쿼리문 수행 sqlSession 의존성 주입
	
	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private ZipCodeRepository zipcodeRepo;

	@Override
	public MemberVO idCheck(String id) {
		//return this.sqlSession.selectOne("m_idcheck", id);//mybatis에서 selectOne()메서드는 단 한개의
		//레코드만 검색, m_idcheck는 member.xml에서 설정할 유일아이디명이다. 
		
		System.out.println("  \n 아이디 중복 검색(JPA) ====================>");
		Optional<MemberVO> resultMember = this.memberRepo.findById(id);
		MemberVO member;
		if(resultMember.isPresent()) {//중복아이디가 있는 경우
			member = resultMember.get();//MemberVO 엔티티빈 객체타입을 구함
		}else {//중복아이디가 없는 경우
			member = null;
		}
		return member;
	}//아이디 중복검색	

	@Override
	public List<ZipCodeVO> zipFind(String dong) {
		//return this.sqlSession.selectList("zip_List", dong);//mybatis에서 selectList()메서드는 하나
		//이상의 레코드를 검색해서 컬렉션 List로 반환
		
		System.out.println(" \n 우편주소 검색(JPA) =====================>");
		List<ZipCodeVO> zlist = this.zipcodeRepo.findByDong(dong);
		return zlist;
	}//우편주소 검색

	@Override
	public void insertMember(MemberVO m) {
		//this.sqlSession.insert("mem_in", m); //mybatis에서 insert()메서드는 레코드를 저장한다.	
		
		System.out.println(" \n 회원저장(JPA) ======================>");
		m.setMem_state(1);//가입회원일때 1 저장
		this.memberRepo.save(m);
	}//회원 저장

	@Override
	public MemberVO pwdMember(MemberVO m) {
		//return this.sqlSession.selectOne("p_find",m);
		
		System.out.println(" \n 비번 검색(JPA)===================>");
		MemberVO pm = this.memberRepo.pwdFind(m.getMem_id(), m.getMem_name());
		return pm;
	}//비번찾기->아이디와 회원이름을 기준으로 비번검색

	@Transactional //TransactionRequiredException: Exception an update/delete query 에러가 발생하
	//기 때문에 @Transactional 을 해줘야 한다.
	@Override
	public void updatePwd(MemberVO m) {
		//this.sqlSession.update("p_edit", m);//mybatis에서 update()메서드는 레코드를 수정한다. 	
		
		System.out.println(" \n 암호화된 임시비번으로 수정(JPA) ======================>");
		this.memberRepo.updatePwd(m.getMem_pwd(), m.getMem_id());
	}//암호화된 임시비번으로 수정

	@Override
	public MemberVO loginCheck(String login_id) {
		//return this.sqlSession.selectOne("m_loginCheck", login_id);
		
		System.out.println(" \n 로그인 인증(JPA) ===============>");
		MemberVO m = this.memberRepo.loginCheck(login_id);
		return m;
	}//로그인 인증 처리

	@Override
	public MemberVO getMember(String id) {		
		//return this.sqlSession.selectOne("m_info", id);
		
		System.out.println(" \n 회원정보 보기(JPA) ===============>");
		MemberVO m = this.memberRepo.getReferenceById(id);
		/* 로그인 된 상태에서 아이디에 대한 회원정보보기는 반드시 해당 레코드 값이 있는 경우에 사용되는 getReferenceById
		 * () 내장메서드를 사용한다.
		 */
		return m;
	}//아이디에 해당하는 회원정보 보기

	@Transactional
	@Override
	public void editMember(MemberVO em) {
		//this.sqlSession.update("medit_ok", em);		
		
		System.out.println(" \n 회원정보 수정(JPA)====================>");
		
		this.memberRepo.updateMember(em.getMem_pwd(), em.getMem_name(), em.getMem_zip(),
				em.getMem_zip2(), em.getMem_addr(), em.getMem_addr2(), em.getMem_phone01(),
				em.getMem_phone02(), em.getMem_phone03(), em.getMail_id(), 
				em.getMail_domain(), em.getMem_id());
	}//정보수정

	@Override
	public void delMem(MemberVO m) {
		//this.sqlSession.update("mDel_ok", m);		
		
		System.out.println(" \n 회원 탈퇴(JPA) ==================>");
		
		Optional<MemberVO> resultMember = this.memberRepo.findById(m.getMem_id());
		/* java 8 에서 추가된 Optional을 사용하는 이유는 null 처리를 해결하기 위한 래퍼클래스이다. null이면 
		 * NullPointerException 예외 처리를 하기 위해서 try~catch 문등을 사용해야 한다.
		 * 이런 부분을 해결해 준다.
		 */
		MemberVO member;
		if(resultMember.isPresent()) {//검색된 회원정보가 있는 경우
			member = resultMember.get();
			
			member.setMem_delcont(m.getMem_delcont());//탈퇴사유 저장
			member.setMem_state(2);//탈퇴 회원이면 2로 저장
			member.setMem_deldate(new Timestamp(System.currentTimeMillis()));//탈퇴날짜 저장
			
			this.memberRepo.save(member);
		}
	}//회원 탈퇴
}







