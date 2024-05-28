package net.daum.dao;

import java.util.List;
import java.util.Optional;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.daum.vo.GongjiVO;

@Repository
public class GongjiDAOImpl implements GongjiDAO {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private GongjiRepository gongjiRepo;

	@Override
	public List<GongjiVO> getGongjiList() {
		return this.sqlSession.selectList("g_list");
	}//최신 공지글 5개 목록보기

	@Override
	public void updateHit(int gongji_no) {
		//this.sqlSession.update("g_hit", gongji_no);		
		
		System.out.println(" \n 자료 검색후 조회수 증가(JPA) ==============> ");
		Optional<GongjiVO> gHit = this.gongjiRepo.findById(gongji_no);//번호를 기준으로 레코드검색
		
		gHit.ifPresent(gHit2 -> {//해당 자료가 있다면 실행
			gHit2.setGongji_hit(gHit2.getGongji_hit() + 1);//조회수 증가 +1
			this.gongjiRepo.save(gHit2);//JPA로 조회수 1 증가 
		});
	}//조회수 증가

	@Override
	public GongjiVO getGongjiCont(int gongji_no) {
		//return this.sqlSession.selectOne("g_cont", gongji_no);
		
        System.out.println(" \n 번호에 해당하는 내용보기(JPA) =================>");
        GongjiVO gc = this.gongjiRepo.getReferenceById(gongji_no);
        return gc;
	}//내용보기
}





