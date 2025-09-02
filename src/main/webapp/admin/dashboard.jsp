<%@ include file="../fragments/header.jsp" %>
<c:set var="pageRole" value="role-admin" scope="request"/>
<h3 class="mb-3">Admin Dashboard</h3>
<div class="d-flex gap-2">
  <a href="${pageContext.request.contextPath}/admin?action=users" class="btn btn-light border">Manage Users</a>
  <a href="${pageContext.request.contextPath}/admin?action=audit" class="btn btn-primary">View Audit Logs</a>
</div>

<%@ include file="../fragments/footer.jsp" %>
