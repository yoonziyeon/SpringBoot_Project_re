package net.daum.vo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@SequenceGenerator( //시퀀스 생성기
		 name="gongji_no_seq_gename",//시퀀스 제너레이터이름
		 sequenceName = "gongji_no_seq",//시퀀스 이름
		 initialValue = 1,//시퀀스 시작값
		 allocationSize = 1 //1씩 증가,메모리에 할당된 기본값은 50
		)
@Table(name="gongji") //gongji테이블 생성
@EqualsAndHashCode(of="gongji_no")
public class GongjiVO {//공지사항 엔티티빈 클래스

	@Id //기본키
	@GeneratedValue(
			 strategy = GenerationType.SEQUENCE, //사용할 전략을 시퀀스로 선택
			 generator = "gongji_no_seq_gename" //시퀀스 생성기에서 설정해 놓은 시퀀스 제너레이터 이름
			)
	private int gongji_no;//공지 번호
	
	private String gongji_name;//공지작성자
	private String gongji_title;//공지제목
	private String gongji_pwd;//공지비번
	
	@Column(length=4000) //테이블 컬럼 크기를 4000으로 설정
	private String gongji_cont;//공지내용
	
	private int gongji_hit;//조회수
	
	@CreationTimestamp //하이버네이트의 기능으로 등록시점 날짜/시간값을 기록,mybatis로 실행할때는 구동 안됨.
	private Timestamp gongji_date;//등록날짜
}



