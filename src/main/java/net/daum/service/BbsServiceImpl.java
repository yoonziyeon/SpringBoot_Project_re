package net.daum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import net.daum.dao.BbsDAO;
import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Service //@Service 애노테이션을 설정해야 스프링에서 서비스로 인식한다.
public class BbsServiceImpl implements BbsService {//사용자 자료실 Service
	
	@Autowired
	private BbsDAO bbsDao;

	@Override
	public void insertBbs(BbsVO bbs) {
		this.bbsDao.insertBbs(bbs);		
	}

	@Override
	public int getTotalCount(PageVO p) {
		return this.bbsDao.getTotalCount(p);
	}

	@Override
	public List<BbsVO> getBbsList(PageVO p) {
		return this.bbsDao.getBbsList(p);
	}

	//스프링의 AOP를 통한 트랜잭션 적용
	@Transactional(isolation = Isolation.READ_COMMITTED)
	//트랜잭션 격리(트랜잭션이 적용되는 중간에 외부 간섭을 배제)
	@Override
	public BbsVO getBbsCont(int bbs_no) {
		this.bbsDao.updateHit(bbs_no);//조회수 증가
		/*문제)bbs.xml에 설정할 유일한 update 아이디명을 bbs_hi로 해서 조회수가 증가되게
		 * 만들어 보자.
		 */
		BbsVO bc=this.bbsDao.getBbsCont(bbs_no);//내용보기
		return bc;
	}

	@Override
	public BbsVO getBbsCont2(int bbs_no) {
		return this.bbsDao.getBbsCont(bbs_no);
	}

	@Transactional //트랜잭션 적용
	@Override
	public void replyBbs(BbsVO rb) {
	   this.bbsDao.updateLevel(rb);//답변 레벨 증가
	   this.bbsDao.replyBbs(rb);//답변 저장
	}//답변 레벨 증가+답변 저장=>스프링의 aop를 통한 트랜잭션 적용	

	@Override
	public void editBbs(BbsVO bbs) {
	   this.bbsDao.editBbs(bbs);		
	}

	@Override
	public void delBbs(int bbs_no) {
	   this.bbsDao.delBbs(bbs_no);		
	}
}




