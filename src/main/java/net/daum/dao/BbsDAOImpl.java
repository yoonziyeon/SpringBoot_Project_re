package net.daum.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Repository //@Repository 애노테이션을 설정해야 스프링에서 모델 DAO로 인식한다.
public class BbsDAOImpl implements BbsDAO {//사용자 자료실 DAO

	@Autowired //자동 의존성 주입
	private SqlSession sqlSession;//mybatis 쿼리문 수행 sqlSession
	
	@Autowired
	private BbsRepository bbsRepo;

	@Override
	public void insertBbs(BbsVO bbs) {
	   //this.sqlSession.insert("bbs_in", bbs);//mybatis에서 bbs_in은 bbs.xml
	   //에서 설정할 유일아이디명, 
		
		System.out.println(" \n 사용자 자료실 저장(JPA) =================>");
		int bbs_no = this.sqlSession.selectOne("bbsSeq_no");//시퀀스 번호값 가져오기
		bbs.setBbs_no(bbs_no);//자료실 번호값 저장
		bbs.setBbs_ref(bbs_no);//글 그룹번호 저장
		
		this.bbsRepo.save(bbs);//자료실 저장
	}//자료실 저장

	@Override
	public int getTotalCount(PageVO p) {
		return this.sqlSession.selectOne("bbs_count",p);//mybatis에서 
		//bbs_count는 bbs.xml에서 설정할 유일한 select아이디명이다. selectOne()메서드는
		//단 한개의 레코드값만 반환한다.
	}//레코드 개수

	@Override
	public List<BbsVO> getBbsList(PageVO p) {
		return this.sqlSession.selectList("bbs_list", p);//mybatis에서 sele
		//ctList()메서드는 하나이상의 레코드를 검색해서 컬렉션 List로 반환
	}// 페이지 목록

	@Override
	public void updateHit(int bbs_no) {
		//this.sqlSession.update("bbs_hi", bbs_no);		
		
		System.out.println(" \n 자료실 레코드 검색후 조회수 증가(JPA) ==============>");
		Optional<BbsVO> bbs_hit = this.bbsRepo.findById(bbs_no);
		
		bbs_hit.ifPresent(bbs_hit2 -> {
			bbs_hit2.setBbs_hit(bbs_hit2.getBbs_hit()+1);//조회수 +1
			this.bbsRepo.save(bbs_hit2);
		});
	}//조회수 증가

	@Override
	public BbsVO getBbsCont(int bbs_no) {
		//return this.sqlSession.selectOne("bbs_co", bbs_no);
		
		System.out.println(" \n 번호에 해당하는 자료실 내용보기(JPA) =============>");
		BbsVO bc = this.bbsRepo.getReferenceById(bbs_no);
		return bc;
	}//내용보기

	@Override
	public void updateLevel(BbsVO rb) {
		//this.sqlSession.update("levelUp", rb);
		
		System.out.println(" \n 자료실 답변 레벨증가(JPA) =============>");
		this.bbsRepo.updateLevel(rb.getBbs_ref(), rb.getBbs_level());
	}//답변 레벨 증가

	@Override
	public void replyBbs(BbsVO rb) {
		//this.sqlSession.insert("reply_in2", rb);//mybatis에서 insert()메서드는
		//레코드를 저장시킨다. reply_in2는 bbs.xml에서 설정할 유일 아이디명이다.
		
		System.out.println(" \n 자료실 답변저장(JPA) ==============>");
		int bbs_no = this.sqlSession.selectOne("bbsSeq_no");//시퀀스로 부터 번호값 가져오기
		rb.setBbs_no(bbs_no);//자료실 번호 저장
		rb.setBbs_step(rb.getBbs_step()+1);
		rb.setBbs_level(rb.getBbs_level()+1);
		
		this.bbsRepo.save(rb);
	}//답변 저장

	@Transactional  //TransactionRequiredException: Exception an update/delete query 에러가 발생하
	//기 때문에 @Transactional 을 해줘야 한다.
	@Override
	public void editBbs(BbsVO bbs) {
		//this.sqlSession.update("bbs_edit", bbs);	
		
		System.out.println(" \n 자료실 수정(JPA) ===============>");
		this.bbsRepo.updateBbs(bbs.getBbs_name(), bbs.getBbs_title(), bbs.getBbs_cont(),
				bbs.getBbs_file(), bbs.getBbs_no());
	}//수정

	@Override
	public void delBbs(int bbs_no) {
		//this.sqlSession.delete("bbs_del", bbs_no);		
		
		System.out.println(" \n 자료실 삭제(JPA) ===============>");
		this.bbsRepo.deleteById(bbs_no);
	}//자료실 삭제
}





