<%@page import="javax.ws.rs.core.Request"%>
<%@page import="javax.ws.rs.core.Response"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="fr">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <title>Menu</title>

	<link href="css/style.css" rel="stylesheet">
    <link href="css/bootstrap.min.css" rel="stylesheet">
  </head>
  <body>
    
	<header>
       <div class="container-fluid">
           <div class="row zsx">
           
          	<div class="col-lg-1"></div>
	  	  	<div class="col-lg-3">
	  			<img src="img/csv.svg" alt="PDF" class="img"/>
	  	    </div>

	  		<div class="col-lg-4">
                 <p class="vcenter">
                 <h1>CSV To PDF</h1>
                 <h3>Amazing Woowwww !!!</h3>
            </div>

			<div class="col-lg-2"></div>
			
		 	<div class="col-lg-2">
	  			<img src="img/pdf.svg" alt="CSV" class="img"/>
	  		</div>
 			
           </div>
       </div>
	</header>


    <div class="container">
   	
	    <div class="row">
		 	<p class="bg-success messageInfo"><c:out value="${message}"></c:out></p>
			<p class="messageInfo" style="color:red; margin-bottom:30px"><c:out value="${message2}"></c:out></p>
		</div>
	 
  		<div class="row">
  			<div class="col-lg-2"></div>
	  		<div class="col-lg-4">
		  		<a href="<c:url value="/upload"/>"><button type="button" class="btn btn-default bouton">Uploader une facture en CSV</button></a>
			</div>
			<div class="col-lg-2"></div>
			<div class="col-lg-4">
			 	<a href="<c:url value="/listeFacture"/>"><button type="button" class="btn btn-default bouton">Voir les factures existantes</button></a>
			</div>
		</div>
		  
	</div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    
  </body>
</html>