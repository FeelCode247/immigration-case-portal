<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../fragments/header.jsp" %>
<c:set var="pageRole" value="role-admin" scope="request"/>
<h3 class="mb-3">Audit Logs</h3>
<table class="table table-sm table-hover">
  <thead><tr><th>ID</th><th>User</th><th>Action</th><th>Details</th><th>IP</th><th>At</th></tr></thead>
  <tbody>
  <c:forEach var="l" items="${logs}">
    <tr>
      <td>${l.id}</td>
      <td><c:out value="${l.userId}"/></td>
      <td>${l.action}</td>
      <td>${l.details}</td>
      <td>${l.ipAddress}</td>
      <td>${l.createdAt}</td>
    </tr>
  </c:forEach>
  </tbody>
</table>

<p class="mt-3">
  <a href="${pageContext.request.contextPath}/${sessionScope.user.role=='ADMIN' ? 'admin/dashboard.jsp' : 'admin/dashboard.jsp'}" 
     class="btn btn-light">⬅ Back</a>
</p>
<%@ include file="../fragments/footer.jsp" %>
