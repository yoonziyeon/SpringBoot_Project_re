package net.daum.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Bbs2VO {//실제 첨부되는 파일이 저장되는 클래스

	private MultipartFile uploadFile; //실제 업로드 할 파일정보를 저장,bbs_write.js
	//p의 <input type="file" name="uploadFile" >의 네임피라미터 이름과 멤버변수명을
	//같게 한다.
}
