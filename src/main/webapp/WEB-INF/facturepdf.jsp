<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="javax.ws.rs.core.Request"%>
<%@page import="javax.enterprise.context.RequestScoped"%>
<%@page import="facture.modele.Facture"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>facture</title>
</head>
<body>
<object data="<c:out value="${fichierpdf}"/>" type="application/pdf" width="100%" height="100%">
</object>
</body>
</html>