<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사용자 자료실 답변폼</title>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<%--jQuery CDN(Content Delivery Network의 약어)이란 인터넷 온라인으로 원격으로 연결해서
원격 서버 컴퓨터에 있는 jQuery라이브러리를 다운받아서 사용하는 방식이다. 그러므로 jQuery라이브러리
파일을 로컴 컴에 실제 다운받지 않는다. --%>
<script src="./js/bbs.js"></script><%-- 유효성 검증 javascript & jQuery --%>
<link rel="stylesheet" type="text/css" href="./css/bbs.css" >
</head>
<body>
<div id="bsW_wrap">
 <h2 class="bsW_title">자료실 답변</h2>
 <form method="post" action="bbs_reply_ok"
  onsubmit="return write_check();" >
 <%-- 답변글 히든값 --%>
 <input type="hidden" name="bbs_ref" value="${b.bbs_ref}" >
 <%-- 원본글과 답변글을 묶어주는 글 그룹번호, hidden은 브라우저 출력창에서는 만들어 지지 않는다.
 하지만 페이지 소스보기에서는 노출된다. 그러므로 보안상 중요한 데이터는 히든으로 전달하는 것이 아니다.
  --%>
 <input type="hidden" name="bbs_step" value="${b.bbs_step}" >
 <%-- 원본글과 답변글을 구분하는 번호값이면서 몇번째 답변글 인가를 알려준다. --%>
 <input type="hidden" name="bbs_level" value="${b.bbs_level}" >
 <%-- 답변글 정렬순서 --%>
 
 <%-- 페이징에서 책갈피 기능 페이지 번호 히든값 --%>
 <input type="hidden" name="page" value="${page}" >
  
  <table id="bsW_t">
   <tr>
    <th>글쓴이</th>
    <td><input name="bbs_name" id="bbs_name" size="14" ></td>
    <%--type속성을 생략하면 기본값이 text이다. --%>
   </tr>  
   <tr>
    <th>글제목</th>
    <td><input name="bbs_title" id="bbs_title" size="33" 
          value="Re:${b.bbs_title}" ></td>
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
   
  </table>
  <div id="bsW_menu">
    <input type="submit" value="답변" >  
    
    <input type="reset" value="취소" onclick="location=
    	'bbs_cont?bbs_no=${b.bbs_no}&page=${page}&state=cont';" >
    	
    <input type="button" value="목록" onclick="location=
    	'bbs_list?page=${page}';" >
  </div>
 </form>
</div>
</body>
</html>