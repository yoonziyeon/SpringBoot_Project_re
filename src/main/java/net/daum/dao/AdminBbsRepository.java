package net.daum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.daum.vo.BbsVO;

public interface AdminBbsRepository extends JpaRepository<BbsVO, Integer> {

	//@Query 애노테이션은 select문만 가능하지만 @Modifying을 이용해서 DML(insert,update,delete)문 작업 처리가 가능하다.
    @Modifying
	@Query("update BbsVO b set b.bbs_name=?1,bbs_title=?3,b.bbs_cont=?4,bbs_file=?5 where b.bbs_no=?2 ")
    //?1은 첫번째로 전달되는 피라미터 값 ,?2은 두번째로 전달된  피라미터 값
	//JPQL(JPA에서 사용하는 Query Language => Java Persistence Query Language의 약어)이다.
	//JPQL은 테이블 대신 엔티빈 클래스를 이용하고,테이블 컬럼대신 엔티티빈 클래스의 변수 즉 속성을 이용한다.    
    public void adminUpdateBbs(String name,int no,String title,String cont,String file);   
    
}
