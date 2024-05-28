package net.daum.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ZipCode2VO {//DB로 부터 검색된 우편주소를 한번더 가공해서 저장할 데이터 저장빈 클래스

	private String zipcode;//우편번호
	private String addr;//시도 구군 동(도로명주소)
}
