<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Email Verification</title>
    <%@include file="all_component/allCss.jsp"%>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-6 offset-md-3">
                <div class="card">
                    <div class="card-body">
                        <h3 class="text-center">Verify Your Email</h3>
                        
                        <c:if test="${not empty failedMsg}">
                            <p class="text-danger">${failedMsg}</p>
                            <c:remove var="failedMsg" scope="session" />
                        </c:if>
                        
                        <form action="verifyLoginEmail" method="post">
                            <div class="form-group">
                                <label>Enter Verification Code (Sent to ${emailForVerification})</label>
                                <input type="text" name="verificationCode" class="form-control" required>
                            </div>
                            <input type="hidden" name="email" value="${emailForVerification}">
                            <button type="submit" class="btn btn-primary">Verify</button>
                        </form>
                        
                        <div class="mt-3">
                            <p>Didn't receive the code? <a href="resendVerification?email=${emailForVerification}">Resend</a></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>