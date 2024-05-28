package net.daum.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@SequenceGenerator( //오라클 시퀀스 생성기
		  name="zip_noseq_gename" , //시퀀스 제너레이터 이름
		  sequenceName="zip_no_seq",//시퀀스 이름
		  initialValue= 1 , //시퀀스 시작값
		  allocationSize = 1 //1씩 증가
		)
@Table(name="zipcode") //zipcode테이블 생성
@EqualsAndHashCode(of="no")
public class ZipCodeVO {//우편번호,주소 저장 엔티티빈 클래스

	@Id //기본키
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE, //사용할 전략을 시퀀스로 선택
			generator = "zip_noseq_gename" //시퀀스 생성기에서 설정해 놓은 시퀀스 제너레이터이름			
			)
	private int no;
	
	private String zipcode;//우편번호
    private String sido;//시도
    private String gugun;//구군
    private String dong;//도로명 주소 또는 지번(읍면동)
    private String bunji;//번지
}










