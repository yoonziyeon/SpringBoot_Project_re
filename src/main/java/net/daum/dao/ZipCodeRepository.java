package net.daum.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.daum.vo.ZipCodeVO;


public interface ZipCodeRepository extends JpaRepository<ZipCodeVO, Integer> {

	@Query("select z from ZipCodeVO z where z.dong like ?1 and z.no > 0 order by z.no desc")
	/* JPQL(JPA에서 사용하는 쿼리 언어 =>Java Persistence Query Language의 약어)은 실제 테이블명 대신 엔티티빈 클
	 * 래스명을 사용하고, 실제 테이블 컬럼명 대신 엔티티빈 클래스의 멤버변수인 속성명을 이용한다. ?1은 첫번째로 전달되는 인자값.	 * 
	 */
	public List<ZipCodeVO> findByDong(String dong);
}
