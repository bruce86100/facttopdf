<%@page pageEncoding="UTF-8" isErrorPage="true" contentType="text/html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Envoi d'une facture au format CSV</title>

<link href="css/style.css" rel="stylesheet">
<link href="css/bootstrap.min.css" rel="stylesheet">

</head>
<body>

	<header>
		<div class="container-fluid">
			<div class="row zsx">

				<div class="col-lg-1"></div>
				<div class="col-lg-3">
					<img src="img/csv.svg" alt="PDF" class="img" />
				</div>

				<div class="col-lg-4">
					<p class="vcenter">
					<h1>CSV To PDF</h1>
					<h3>Amazing Woowwww !!!</h3>
				</div>

				<div class="col-lg-2"></div>

				<div class="col-lg-2">
					<img src="img/pdf.svg" alt="CSV" class="img" />
				</div>

			</div>
		</div>
	</header>

	<div class="container" style="margin-top: 25px;">
		<div class="row">
			<div class="col-lg-8 col-sm-10 col-xs-12">
				<form method="POST" enctype="multipart/form-data">
					<div class="form-group">
						<label style="margin-bottom: 25px;"><b>Merci de
								sélectionner le fichier CSV correspondant à la facture que vous
								souhaitez sauvegarder :</b></label> <input style="margin-bottom: 25px;"
							type="file" name="file" id="file">
					</div>
					<div class="form-group">
						<input class="form-control btn btn-primary" style="width: 200px;"
							type="submit" name="submit" value="Upload CSV">
					</div>
					<label style="margin-bottom: 25px;"><b> Veuillez
							choisir la tva à appliquer :</b></label> <select name="listetva">
						<c:forEach var="liste" items="${liste}">
							<option value="${liste.id}">${liste.taux}</option>
						</c:forEach>
					</select>
				</form>
				
				<a href="<%=request.getContextPath()+"/Menu.jsp"%>"><button class="btn btn-danger" Style="margin-top: 10px;">Retour</button></a>
			</div>
		</div>
	</div>

	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>

</body>
</html>