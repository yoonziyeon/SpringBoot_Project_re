package net.daum.dao;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.daum.vo.GongjiVO;
import net.daum.vo.PageVO;

@Repository
public class AdminGongjiDAOImpl implements AdminGongjiDAO {

	@Autowired
	private SqlSession sqlSession;//mybatis 쿼리문 수행 sqlSession
	
	@Autowired
	private AdminGongjiRepository adminGongjiRepo;

	@Override
	public int getListCount(PageVO p) {		
		return this.sqlSession.selectOne("ag_count", p);
	}//검색전후 레코드 개수	

	@Override
	public List<GongjiVO> getGongjiList(PageVO p) {
		return this.sqlSession.selectList("ag_list", p);
	}//관리자 공지 검색전후 목록

	@Override
	public void insertGongji(GongjiVO g) {
		//this.sqlSession.insert("ag_in", g);
		
		System.out.println(" \n 관리자 공지저장(JPA) =================>");
		this.adminGongjiRepo.save(g);
	}//관리자 공지저장

	@Override
	public GongjiVO getGongjiCont(int no) {
		//return this.sqlSession.selectOne("ag_cont", no);//mybatis에서 selectOne()메서드는 단 한개의
		//레코드만 반환하고, ag_cont는 admin_gongji.xml에서 설정할 유일 아이디명이다.
		
		System.out.println(" \n 관리자 공지상세정보와 수정폼(JPA) ===============>");
		GongjiVO gc = this.adminGongjiRepo.getReferenceById(no);
		/*getReferenceById() 메서드는 내용이 없는 경우 예외 오류가 발생한다. 그러므로 반드시 자료가 있는 경우만 사용해야
		 * 한다. 
		 */
		return gc;
	}//관리자 공지 수정폼과 상세정보

	@Override
	public void editGontji(GongjiVO eg) {
		//this.sqlSession.update("ag_edit",eg);
		
		System.out.println(" \n 관리자 공지수정 완료(JPA) ==============>");
		Optional<GongjiVO> egResult = this.adminGongjiRepo.findById(eg.getGongji_no());
		GongjiVO g;
		
		if(egResult.isPresent()) {//공지번호에 해당하는 해당 값이 있다면 참
			g=egResult.get();
			
			g.setGongji_name(eg.getGongji_name());//수정할 공지작성자 저장
			g.setGongji_title(eg.getGongji_title());//수정할 공지제목 저장
			g.setGongji_cont(eg.getGongji_cont());//수정할 공지내용 저장
			
			this.adminGongjiRepo.save(g);
		}
	}//관리자 공지 수정완료

	@Override
	public void delGongji(int no) {
		//this.sqlSession.delete("ag_del", no);	
		
		System.out.println(" \n 관리자 공지삭제(JPA) ================>");
		this.adminGongjiRepo.deleteById(no);
	}//관리자 공지삭제
}










