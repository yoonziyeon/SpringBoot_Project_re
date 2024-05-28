<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 로그인</title>
<link rel="stylesheet" type="text/css" href="./css/admin.css" >
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
 function admin_check(){
	 $admin_id = $.trim($('#admin_id').val());
	 if($admin_id.length == 0){
		 alert('관리자 아이디를 입력하세요!');
		 $('#admin_id').val('').focus();
		 return false;
	 }
	 
	 $admin_pwd = $.trim($('#admin_pwd').val());
	 if($admin_pwd == ''){
		 alert('관리자 비번을 입력하세요!');
		 $('#admin_pwd').val('').focus();
		 return false;
	 }
 }
</script>
</head>
<body>
 <div id="aLogin_wrap">
  <h2 class="aLogin_title">관리자 로그인</h2>
  <form method="post" action="admin_Login_Ok" onsubmit="return admin_check();">
   <table id="aLogin_t">
    <tr>
     <th>관리자 아이디</th>
     <td>
      <input type="text" name="admin_id" id="admin_id" size="14" tabindex="1" >
     </td>
     <th rowspan="2">
      <input type="submit" value="로그인" >
     </th>
    </tr>
    
    <tr>
     <th>관리자 비번</th>
     <td><input type="password" name="admin_pwd" id="admin_pwd" size="14" tabindex="2" ></td>    
    </tr>
   </table>
  </form>
 </div>
</body>
</html>










