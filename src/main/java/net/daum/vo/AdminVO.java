package net.daum.vo;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter //setter() 메서드 자동제공
@Getter //getter() 메서드 자동제공
@ToString //toString()메서드 자동제공
@Entity
@Table(name="admin") //admin 관리자 테이블 생성
@EqualsAndHashCode(of="admin_id") //equals(),hashCode(), canEqual() 메서드 자동제공
public class AdminVO {//관리자 엔티티빈 클래스

	private int admin_no;
	
	@Id //기본키
	private String admin_id;//관리자 아이디
    private String admin_pwd;//관리자 비번
    private String admin_name;//관리자 이름
    
    @CreationTimestamp //하이버네이트의 특별한 기능으로 등록시점 날짜값을 기록, mybatis로 실행할 때는 구동안됨.
    private Timestamp admin_date;//등록날짜
}








