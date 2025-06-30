<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
	<H3>회원 로그인 창</H3>
	<div id="detail_table">
	<form name="loginForm" id="loginForm" method="post" action="<c:url value='/login.do'/>" >
		${error ? exception : ''}
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<TABLE>
			<TBODY>
				<TR class="dot_line">
					<TD class="fixed_join">아이디</TD>
					<TD><input name="username" type="text" size="20" value="user1@aaa.com"/></TD>
				</TR>
				<TR class="solid_line">
					<TD class="fixed_join">비밀번호</TD>
					<TD><input name="password" type="password" size="20" value="1004"/></TD>
				</TR>
			</TBODY>
		</TABLE>
		<br><br>
		<INPUT	type="submit" value="로그인"> 
		<INPUT type="button" value="초기화">
		
		<br/><br/>
		   <a href="#">아이디 찾기</a>  | 
		   <a href="#">비밀번호 찾기</a> | 
		   <a href="<c:url value='/member/registerForm.do'/>">회원가입</a>    | 
		   <a href="#">고객 센터</a>
	</form>		
	</div>
</body>
</html>