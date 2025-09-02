<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Case Portal</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    .role-applicant .navbar { background-color:#0d6efd; }
    .role-officer .navbar { background-color:#198754; }
    .role-admin .navbar { background-color:#dc3545; }
    .status-badge[data-status="PENDING"] { background:#ffc107; }
    .status-badge[data-status="IN_REVIEW"] { background:#0d6efd; color:#fff; }
    .status-badge[data-status="APPROVED"] { background:#198754; color:#fff; }
    .status-badge[data-status="REJECTED"] { background:#dc3545; color:#fff; }
  </style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css"/>
</head>
<body class="<c:out value='${pageRole}'/>">
<nav class="navbar navbar-dark mb-4">
  <div class="container-fluid">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">Case Portal</a>
    <div class="d-flex">
      <c:choose>
        <c:when test="${not empty sessionScope.user}">
          <span class="navbar-text me-3">${sessionScope.user.fullName} (${sessionScope.user.role})</span>
          <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn btn-light btn-sm">Logout</a>
        </c:when>
        <c:otherwise>
          <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-light btn-sm">Login</a>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</nav>
<div class="container">
