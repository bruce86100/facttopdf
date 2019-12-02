<%@page pageEncoding="UTF-8" isErrorPage="true" contentType="text/html" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
  <head>
  	<meta charset="UTF-8">
    <title>Java EE</title>
  </head>
  <body>
	<div>Le fichier a bien été upload
		 <fmt:formatDate type="date" dateStyle="long" value="${facture.numero}"/> à 
		 <fmt:formatDate type="time" value="${facture.date}"/> 
	</div>
	<div>
	  	<a href="<c:url value="/facture"/>">Retour à l'accueil</a>  
	</div>
  </body>
</html>
