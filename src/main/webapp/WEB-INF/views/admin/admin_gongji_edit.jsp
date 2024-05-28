<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:include page="../include/admin_header.jsp" />

<%--관리자 메인본문 --%>
<div id="aMain_cont">
  <div id="aBw_wrap">
   <h2 class="aBw_title">관리자 공지수정</h2>
   <form method="post" action="admin_gongji_edit_ok?no=${gc.gongji_no}&page=${page}"
    onsubmit="return gw_check();">
    <%-- ?no=번호값&page=쪽번호 2개 피라미터 값은 get방식으로 전달되고 나머지는 post로 전달된다. --%>
    <table id="aBw_t">
     <tr>
      <th>공지작성자</th>
      <td><input name="gongji_name" id="gongji_name" size="14" value="${gc.gongji_name}"></td>
     </tr>
     <tr>
      <th>공지제목</th>
      <td><input name="gongji_title" id="gongji_title" size="34" value="${gc.gongji_title}"></td>
     </tr>
     <tr>
      <th>비밀번호</th>
      <td><input type="password" name="gongji_pwd" id="gongji_pwd" size="14" ></td>
     </tr>
     <tr>
      <th>공지내용</th>
      <td><textarea name="gongji_cont" id="gongji_cont" rows="9" 
      cols="36">${gc.gongji_cont}</textarea></td>
     </tr>
    </table>
    <div id="aBw_menu">
      <input type="submit" value="수정" /> <input type="reset" value="취소" 
      onclick="$('#gongji_name').focus();" > <input type="button" value="공지목록"
      onclick="location='admin_gongji_list?page=${page}';" >
    </div>
   </form>
  </div>
</div>

<jsp:include page="../include/admin_footer.jsp" />