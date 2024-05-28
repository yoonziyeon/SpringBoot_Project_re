package net.daum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.daum.dao.AdminGongjiDAO;
import net.daum.vo.GongjiVO;
import net.daum.vo.PageVO;

@Service /* 관리자 공지 서비스 => 스프링의 AOP를 통한 트랜잭션을 적용함으로써 데이터 일관성을 유지(사이트 신뢰도 보장) */
public class AdminGongjiServiceImpl implements AdminGongjiService {

	@Autowired
	private AdminGongjiDAO adminGongjiDao;

	@Override
	public int getListCount(PageVO p) {
		return this.adminGongjiDao.getListCount(p);
	}

	@Override
	public List<GongjiVO> getGongjiList(PageVO p) {
		return this.adminGongjiDao.getGongjiList(p);
	}

	@Override
	public void insertGongji(GongjiVO g) {
		this.adminGongjiDao.insertGongji(g);		
	}

	@Override
	public GongjiVO getGongjiCont(int no) {
		return this.adminGongjiDao.getGongjiCont(no);
	}

	@Override
	public void editGongji(GongjiVO eg) {
		this.adminGongjiDao.editGontji(eg);		
	}

	@Override
	public void delGongji(int no) {
	   this.adminGongjiDao.delGongji(no);	
	}	
}
