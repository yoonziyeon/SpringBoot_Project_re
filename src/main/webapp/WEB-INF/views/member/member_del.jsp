<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> 회원탈퇴</title>
<link rel="stylesheet" type="text/css" href="./css/member.css" >
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
 function del_check(){
	 if($.trim($('#del_pwd').val()).length == 0){
		 window.alert('비번을 입력하세요!');
		 $("#del_pwd").val("").focus();
		 return false;
	 }
	 
	 if($.trim($("#del_cont").val()) == ""){
		 alert("탈퇴사유를 입력하세요!");
		 $("#del_cont").val("").focus();
		 return false;
	 }
 }
</script>
</head>
<body>
<div id="mDel_wrap">
 <h2 class="mDel_title">회원탈퇴</h2>
 <form method="post" action="member_Del_Ok" onsubmit="return del_check();">
   <table id="mDel_t">
    <tr>
     <th>회원아이디</th>
     <td>${id}</td>
    </tr>
    <tr>
     <th>회원이름</th>
     <td>${dm.mem_name}</td>
    </tr>
    <tr>
     <th>비밀번호</th>
     <td><input type="password" name="del_pwd" id="del_pwd" size="14" ></td>
    </tr>
    <tr>
     <th>탈퇴사유</th>
     <td>
      <textarea name="del_cont" id="del_cont" rows="9" cols="36"></textarea>
     </td>
    </tr>
   </table> 
   <div id="mDel_menu">
    <button type="submit">탈퇴</button>
    <button type="reset" onclick="location='member_Login';">취소</button>
   </div>
 </form>
</div>
</body>
</html>





