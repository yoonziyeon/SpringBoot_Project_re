package net.daum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import net.daum.dao.GongjiDAO;
import net.daum.vo.GongjiVO;

@Service
public class GongjiServiceImpl implements GongjiService {

	@Autowired
	private GongjiDAO gongjiDao;

	@Override
	public List<GongjiVO> getGongjiList() {
		return this.gongjiDao.getGongjiList();
	}

	//조회수 증가+내용보기 => 스프링의 AOP를 통한 트랜잭션 적용대상
	@Transactional(isolation = Isolation.READ_COMMITTED)
	//트랜잭션 격리(트랜잭션이 처리되는 중간에 외부간섭을 배제한다. READ_COMMITTED 옵션은
	//커밋된 데이터에 대해 읽기 허용)
	@Override
	public GongjiVO getGongjiCont(int gongji_no) {
		this.gongjiDao.updateHit(gongji_no);//조회수 증가
		return this.gongjiDao.getGongjiCont(gongji_no);//번호에 해당하는 내용보기
	}
}





