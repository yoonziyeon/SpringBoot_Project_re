package net.daum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.daum.dao.AdminBbsDAO;
import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Service
public class AdminBbsServiceImpl implements AdminBbsService {

	@Autowired
	private AdminBbsDAO adminBbsDao;

	@Override
	public int getListCount(PageVO p) {
		return this.adminBbsDao.getListCount(p);
	}

	@Override
	public List<BbsVO> getBbsList(PageVO p) {
		return this.adminBbsDao.getBbsList(p);
	}

	@Override
	public void insertBbs(BbsVO b) {
		this.adminBbsDao.insertBbs(b);		
	}

	@Override
	public BbsVO getAdminBbsCont(int no) {
		return this.adminBbsDao.getAdminBbsCont(no);
	}

	@Override
	public void adminUpdateBbs(BbsVO b) {
		this.adminBbsDao.adminUpdateBbs(b);
	}

	@Override
	public void adminBbsDel(int no) {
		this.adminBbsDao.adminBbsDel(no);		
	}	
}









