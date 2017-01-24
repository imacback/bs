<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Properties,com.duoqu.commons.web.spring.SpringContextHolder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    	<title>爱悦读管理系统</title>
    	<%@ include file="/WEB-INF/jsp/share/head.jsp"%>
    	<c:set var="userName" scope="session" value="${sessionScope.adminUser.nickname}"/>
        <c:set var="roleId" scope="session" value="${sessionScope.adminUser.roleId}"/>
    	<c:set var="imgUrl" scope="session" value="${sessionScope.imgUrl}"/>
    	<c:set var="htmlUrl" scope="session" value="${sessionScope.htmlUrl}"/>
    	<c:set var="frontPrefix" scope="session" value="${sessionScope.frontPrefix}"/>
        <c:set var="noimgSmall" scope="session" value="${sessionScope.noimgSmall}"/>
        <c:set var="mode" scope="session" value="${sessionScope.mode}"/>
        <link rel="icon" type="image/vnd.microsoft.icon" href="${imgPath}/duoqu.ico"/>
        <script type="text/javascript">
    		var userName = '${userName}';
            var roleId = '${roleId}';
    	    var appPath = '${appPath}';
    	    var imgUrl = '${imgUrl}';
    	    var htmlUrl = '${htmlUrl}';
    	    var frontPrefix = '${frontPrefix}';
            var noimgSmall = '${noimgSmall}';
            var mode = '${mode}';
        </script>    	
        <script type="text/javascript" src="${jsPath}/Main.js"></script>
        <script type="text/javascript" src="${jsPath}/changePasswordDialog.js"></script>
  	</head>
    <body>
        <%@ include file="loading.jsp"%>
  	</body>
</html>
