<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="<%=request.getContextPath() %>/css/top.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function changeDomain(domain) {
		// 선택된 도메인 값을 email2 의 value 값으로 변경
		document.joinForm.email2.value = domain;
		
		// 단, 선택된 도메인이 "직접입력" 이 아닐 경우 email2 입력창 잠금(readOnly 속성 적용)
		// 아니면, email2 입력창 잠금 해제
		if(domain == "") { // 직접입력 선택 시(document.joinForm.selectDomain[0].selected 동일)
			document.joinForm.email2.readOnly = false; // 입력창 잠금 해제
			document.joinForm.email2.focus(); // 입력창 포커스 요청
		} else {
			document.joinForm.email2.readOnly = true; // 입력창 잠금
		}
	} 
</script>
</head>
<body>
	<!-- 세션 아이디가 null 이 아닐 경우 메인페이지로 돌려보내기 -->
	<c:if test="${sessionScope.sId ne null }">
		<script>
			alert("잘못된 접근입니다!");
			location.href = "./";
		</script>
	</c:if>
	<header>
		<!-- Login, Join 링크 표시 영역(inc/top.jsp 페이지 삽입) -->
		<jsp:include page="/inc/top.jsp"></jsp:include>
	</header>
	<h1>회원 가입</h1>
	<form action="MemberJoinPro.me" method="post" name="joinForm">
		<table border="1">
			<tr>
				<td>이름</td>
				<td><input type="text" name="name" required="required" size="20"></td>
			</tr>
			<tr>
				<td>성별</td>
				<td>
					<input type="radio" name="gender" value="남">남&nbsp;&nbsp;
					<input type="radio" name="gender" value="여" checked="checked">여
				</td>
			</tr>
			<tr>
				<td>E-Mail</td>
				<td>
					<input type="text" name="email1" required="required" size="10">@
					<input type="text" name="email2" required="required" size="10">
					<select name="selectDomain" onchange="changeDomain(this.value)">
						<option value="">직접입력</option>	
						<option value="naver.com">naver.com</option>
						<option value="nate.com">nate.com</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>아이디</td>
				<td>
					<input type="text" name="id" required="required" size="20" placeholder="4-16자리 영문자,숫자 조합">
					<span id="checkIdResult"><!-- 자바스크립트에 의해 메세지가 표시될 공간 --></span>
				</td>
			</tr>
			<tr>
				<td>패스워드</td>
				<td>
					<input type="password" name="passwd" required="required" size="20" placeholder="8-20자리 영문자,숫자,특수문자 조합">
					<span id="checkPasswdResult"><!-- 자바스크립트에 의해 메세지가 표시될 공간 --></span>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="submit" value="회원가입">
					<input type="button" value="취소" onclick="history.back()">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>