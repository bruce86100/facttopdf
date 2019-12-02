<%@page pageEncoding="UTF-8" isErrorPage="true" contentType="text/html" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
  	<meta charset="UTF-8">
    <title>Envoi d'une facture au format CSV</title>
  </head>
  <body>
  	
	<form method="POST" enctype="multipart/form-data">
	    Upload du fichier CSV:
	    <input type="file" name="file" id="file">
	    <br/><br/>
	    <input type="submit" name="submit" value="Upload CSV">
	</form>

  </body>
</html>