package net.daum.vo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity //엔티티빈 클래스
@Table(name="bbs") //bbs테이블 생성
@EqualsAndHashCode(of="bbs_no")
public class BbsVO {//자료실 엔티티빈 클래스

	@Id //기본키
	private int bbs_no;//번호
	
	private String bbs_name;//글쓴이
	private String bbs_title;//글제목
	private String bbs_pwd;//비번
	
	@Column(length = 4000) //컬럼 크기를 4000으로 설정	
	private String bbs_cont;//글내용
	
	private String bbs_file;//새롭게 생성된 오늘날짜 폴더경로와 변경된 파일명
	
	private int bbs_hit;//조회수
	
	private int bbs_ref;//원본글과 답변글을 묶어주는 글 그룹번호 역할
	private int bbs_step;//원본글이면 0,첫번째 답변글이면 1,두번째 답변글이면 2 =>원본글과
	//답변글을 구분하는 번호값이면서 몇번째 답변글인가를 알려준다.
	private int bbs_level;//답변글 정렬순서
	
	@CreationTimestamp //하이버네이트의 특별한 기능으로 등록시점의 날짜값을 기록한다.mybatis 로 실행할 때는 구동 안됨.
	private Timestamp bbs_date;
}


