<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="facture.modele.Facture"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
<!DOCTYPE html>
<html>
  <head>
  	<meta charset="UTF-8">
    <title>Liste des factures</title>
    
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
		<div class="row justify-content-center"> 
			<table class="table table-sm table-bordered table-striped table-responsive" style="width:40%; margin:0 auto;">
			 		<thead>
			 			<tr class="row">
			 				<th colspan="3"><h3 style="text-align: center;">Liste des factures</h3></th>
			 			</tr>
			 		</thead>
			 		<tbody>	
						<c:forEach items="${liste}" var="f">
							<tr class="row">
								<td class="col-md-8" style="vertical-align:middle; ">
				  					<img style="max-height:60px; width:30px" src="img/pdf.svg" alt="PDF" class="img" />
				  					<span> Facture n° <c:out       value="${f}"/></span>
				  				</td>
				  				<td class="col-md-2 center-block" style="vertical-align:middle; ">
				  					<a href="<%=request.getContextPath()+"/facturepdf?id="%><c:out value="${f}"/>"><button class="btn btn-primary btn-sm">Visionner</button></a>
				  				</td>
				  				<td class="col-md-2 center-block" style="vertical-align:middle; ">
				  					<a href="<%=request.getContextPath()+"/download?id="%><c:out value="${f}"/>"><button class="btn btn-primary btn-sm">Télécharger</button></a>
				  				</td>
				  			</tr>
			  			</c:forEach>
			  		</tbody>
			  			<tfoot>
			  			<tr>
			  				<td colspan="4" style="vertical-align:middle; ">
			 					<a href="<%=request.getContextPath()+"/Menu.jsp"%>"><button class="btn btn-danger">Retour</button></a>
			 				</td>
			 			</tr>
			 			</tfoot>
			</table>
		</div>
	</div>	

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    
  </body>
</html>