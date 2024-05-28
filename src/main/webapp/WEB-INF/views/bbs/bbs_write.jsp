<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사용자 자료실 입력폼</title>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<%--jQuery CDN(Content Delivery Network의 약어)이란 인터넷 온라인으로 원격으로 연결해서
원격 서버 컴퓨터에 있는 jQuery라이브러리를 다운받아서 사용하는 방식이다. 그러므로 jQuery라이브러리
파일을 로컴 컴에 실제 다운받지 않는다. --%>
<script src="./js/bbs.js"></script><%-- 유효성 검증 javascript & jQuery --%>
<link rel="stylesheet" type="text/css" href="./css/bbs.css" >
</head>
<body>
<div id="bsW_wrap">
 <h2 class="bsW_title">자료실 입력</h2>
 <form method="post" action="bbs_write_ok"
  onsubmit="return write_check();" enctype="multipart/form-data">
 <%--
     자료실 기능을 만들때 필요한 사항)
      1.method=post방식으로 해야 한다. get은 안된다.
      2.form태그내에서 enctype="multipart/form-data" 속성을 지정해야 한다.
  --%> 
  <table id="bsW_t">
   <tr>
    <th>글쓴이</th>
    <td><input name="bbs_name" id="bbs_name" size="14" ></td>
    <%--type속성을 생략하면 기본값이 text이다. --%>
   </tr>  
   <tr>
    <th>글제목</th>
    <td><input name="bbs_title" id="bbs_title" size="33" ></td>
   </tr>
   <tr>
    <th>비밀번호</th>
    <td><input type="password" name="bbs_pwd" id="bbs_pwd" size="14"></td>
   </tr>
   <tr>
    <th>글내용</th>
    <td>
     <textarea name="bbs_cont" id="bbs_cont" rows="8" cols="34"></textarea>
    </td>   
   </tr>
   <tr>
    <th>파일첨부</th>
    <td>
     <input type="file" name="uploadFile" >
    </td>
   </tr>
  </table>
  <div id="bsW_menu">
    <input type="submit" value="입력" >  
    <input type="reset" value="취소" onclick="$('#bbs_name').focus();">
    <input type="button" value="목록" onclick="location=
    	'bbs_list?page=${page}';" >
  </div>
 </form>
</div>
</body>
</html>






