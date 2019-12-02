<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<div id="menu">
    <p><a href="<c:url value="/upload"/>">Uploader une facture en CSV</a></p>
    <p><a href="<c:url value="/listeFacture"/>">Voir les factures existantes</a></p>
</div>
</body>
</html>