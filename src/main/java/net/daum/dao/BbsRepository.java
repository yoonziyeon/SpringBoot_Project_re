package net.daum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.daum.vo.BbsVO;

public interface BbsRepository extends JpaRepository<BbsVO, Integer> {

	@Modifying
	@Query("update BbsVO b set b.bbs_level = b.bbs_level + 1 where b.bbs_ref=?1 and "
			+" b.bbs_level > ?2")
	public void updateLevel(int ref, int level);//답변 레벨 증가
	
	@Modifying
	@Query("update BbsVO b set b.bbs_name=?1,b.bbs_title=?2,b.bbs_cont=?3,b.bbs_file=?4 "
			+" where b.bbs_no=?5")
	public void updateBbs(String name,String title,String cont,String file,int no);//자료실 수정
}
