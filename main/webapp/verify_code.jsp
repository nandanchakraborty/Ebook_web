<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Verify Code</title>
    <%@include file="all_component/allCss.jsp"%>
</head>
<body>
<div class="container p-5">
    <h3 class="text-center">Enter the OTP sent to your email</h3>
    <form method="post" action="verify-code" class="text-center mt-4">
        <input type="text" name="code" placeholder="Enter OTP" required>
        <button type="submit" class="btn btn-primary ml-2">Verify</button>
    </form>

    <c:if test="${not empty failedMsg}">
        <p class="text-danger text-center mt-3">${failedMsg}</p>
        <c:remove var="failedMsg" scope="session"/>
    </c:if>
</div>
</body>
</html>
