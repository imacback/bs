<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    	<title>爱悦读管理系统</title>
    	<%@ include file="/WEB-INF/jsp/share/head.jsp"%>
        <link rel="icon" type="image/vnd.microsoft.icon" href="${imgPath}/duoqu.ico"/>
        <c:set var="mode" scope="session" value="${sessionScope.mode}"/>
    	<script type="text/javascript">
    	    var appPath = '${appPath}';
            var userNameInCookie = '${userName}';
            var userPassInCookie = '${userPass}';
            var mode = '${mode}';
        </script>
        <script type="text/javascript" src="${jsPath}/LoginDialog.js"></script>
        <script type="text/javascript" src="${jsPath}/App.js"></script>
  	</head>
    <body>
        <%@ include file="loading.jsp"%>
  	</body>
</html>
