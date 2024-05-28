<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> 로그인 폼</title>
<link rel="stylesheet" type="text/css" href="./css/member.css" >
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<%-- jQuery CDN 방식 --%>
<script type="text/javascript">
  
  function login_check(){
	  if($.trim($('#login_id').val()) == ''){
		  alert('로그인 아이디를 입력하세요!');
		  $('#login_id').val('').focus();
		  return false;
	  }
	  
	  if($.trim($('#login_pwd').val()).length == 0){
		  window.alert('로그인 비번을 입력하세요!');//window.은 생략가능함
		  $('#login_pwd').val('').focus();
		  return false;
	  }
  }//login_check() =>로그인 유효성 검증
  
  function pwd_find(){//비번찾기
	  $pwd_url = 'pwd_Find';
      window.open($pwd_url,'비번찾기공지창','width=400px,height=300px,scrollbars=yes');
      /* open(공지창경로,공지창이름,속성) 메서드로 폭이 400픽셀,높이가 300픽셀, 스크롤바가 생성되는 새로운 공지창을 만든다.
      */
  }
</script>
</head>
<body>
 <c:if test="${empty id}"> <%-- 로그인 전 화면 --%>
   <div id="Login_wrap">
    <h2 class="Login_title">로그인 폼</h2>
    <form method="post" action="member_Login_ok" onsubmit="return login_check();">
     <table id="Login_t">
      <tr>
       <th>아이디</th>
       <td><input name="login_id" id="login_id" size="14" tabindex="1" ></td>
       <%--type속성을 생략하면 기본값이 text이다. tabidnex속성을 1로 지정하면 탭키를 눌렀을 때 첫번째로 포커스를 가진다.
        --%>
       <th rowspan="2">
        <input type="submit" value="로그인" >
       </th>
      </tr>
      <tr>
       <th>비밀번호</th>
       <td><input type="password" name="login_pwd" id="login_pwd" size="14" 
       tabindex="2"></td>
      </tr>
     </table>    
     <div id="Login_menu">
      <button type="button" onclick="pwd_find();">비번찾기</button> <input type="button" 
      value="회원가입" onclick="location='member_Join';" > 
     </div>
    </form>
   </div>
 </c:if>
 
 <c:if test="${!empty id}"> <%-- 로그인 이후 화면 --%>
  <div id="Index_wrap">
    <h2 class="Index_title">로그인 후 메인화면</h2>
    <form method="post" action="member_LogOut">
      <table id="Index_t">
        <tr>
         <th>
          <input type="button" value="정보수정" onclick="location='member_Edit';" > <input 
          type="button" value="회원탈퇴" onclick="location='member_Del';" > <input type="submit"
          value="로그아웃" >
         </th>
        </tr>
        <tr>
         <th>${id}님 로그인을 환영합니다.</th>
        </tr>
      </table>
    </form>
  </div>
 </c:if>
</body>
</html>





