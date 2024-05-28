<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 폼</title>
<link rel="stylesheet" type="text/css" href="./css/member.css" >
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="./js/member.js"></script>
</head>
<body>
 <div id="mJoin_wrap">
  <h2 class="mJoin_title">회원가입</h2>
  <form name="m" method="post" action="member_Join_ok" onsubmit="return join_check();">
   <table id="mJoin_t">
    <tR>
     <th>아이디</th>
     <td><input name="mem_id" id="mem_id" size="14" class="input_box" > <input type="button"
     value="아이디중복체크" class="input_b" onclick="id_check();" ><br> <span id="idcheck"></span>
     </td>
     </tR>
     
     <tr>
      <th>비밀번호</th>
      <td><input type="password" name="mem_pwd" id="mem_pwd" size="14" class="input_box" ></td> 
     </tr>
     
     <tr>
      <th>비밀번호 확인</th>
      <td><input type="password" name="mem_pwd2" id="mem_pwd2" size="14" class="input_box"></td>
     </tr>
    
      <tr>
       <th>회원이름</th>
       <td><input name="mem_name" id="mem_name" size="14" class="input_box" ></td>
      </tr>
      
      <tr>
       <th>우편번호</th>
       <td>
        <input name="mem_zip" id="mem_zip" size="5" class="input_box" readonly>-<input 
        name="mem_zip2" id="mem_zip2" size="5" class="input_box" readonly >
        <%--readonly 속성은 단지 읽기만 가능 --%>
        <input type="button" value="우편검색" class="input_b" onclick="post_check();" >
       </td>
      </tr>
      
      <tr>
       <th>주소</th>
       <td><input name="mem_addr" id="mem_addr" size="38" class="input_box" readonly></td> 
      </tr>
      
      <tr>
       <th>나머지 주소</th>
       <td><input name="mem_addr2" id="mem_addr2" size="30" class="input_box" ></td>
      </tr>
      
      <tr>
       <th>폰번호</th>
       <td>
        <select name="mem_phone01" id="mem_phone01">
         <c:forEach var="p" items="${phone}">
          <option value="${p}">${p}</option>
         </c:forEach>
        </select>-<input name="mem_phone02" id="mem_phone02" size="4" maxlength="4"
        class="input_box">-<input name="mem_phone03" id="mem_phone03" size="4" maxlength="4" 
        class="input_box"> <%-- maxlength 속성을 사용하면 최대 4자까지만 입력가능함 --%>       
       </td>
      </tr>
      
      <tr>
       <th>이메일</th>
       <td>
        <input name="mail_id" id="mail_id" size="14" class="input_box" >@<input 
        name="mail_domain" id="mail_domain" size="18" class="input_box" readonly > <select
        name="mail_list" onchange="domain_list();" >
         <c:forEach var="mail" items="${email}">
          <option value="${mail}">${mail}</option>
         </c:forEach>
        </select>
       </td>
      </tr>
   </table>
   
   <div id="mJoin_menu">
     <input type="submit" value="가입" class="input_b" >&nbsp;&nbsp;&nbsp;<input 
     type="reset" value="취소" class="input_b" onclick="$('#mem_id').focus();" >
   </div>
  </form>
 </div>
</body>
</html>




