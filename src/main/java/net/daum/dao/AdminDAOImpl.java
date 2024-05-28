package net.daum.dao;

import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.daum.vo.AdminVO;

@Repository
public class AdminDAOImpl implements AdminDAO {

	@Autowired
	private SqlSession sqlSession;//mybatis 쿼리문을 수행할 sqlSession 생성	
	
	@Autowired
	private AdminRepository adminRepo;

	@Override
	public void insertAdmin(AdminVO ab) {
		//this.sqlSession.insert("admin_in",ab);	
		
		System.out.println(" \n 관리자 정보 저장(JPA) ================>");
		this.adminRepo.save(ab);
	}//관리자 정보 저장	

	@Override
	public AdminVO adminLogin(String admin_id) {
		//return this.sqlSession.selectOne("admin_login", admin_id);
		
		System.out.println(" \n 관리자 로그인 인증(JPA) ===============>");
		Optional<AdminVO> adminResult = this.adminRepo.findById(admin_id);
		AdminVO admin;
		if(adminResult.isPresent()){//관리자 정보가 있다면
			admin = adminResult.get();
		}else {
			admin = null;
		}
		return admin;
	}//관리자 로그인 인증
}






