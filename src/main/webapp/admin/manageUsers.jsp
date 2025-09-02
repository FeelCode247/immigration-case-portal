<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../fragments/header.jsp" %>
<c:set var="pageRole" value="role-admin" scope="request"/>
<h3 class="mb-3">Manage Users</h3>
<table class="table table-striped">
  <thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Change</th></tr></thead>
  <tbody>
  <c:forEach var="u" items="${users}">
    <tr>
      <td>${u.id}</td>
      <td>${u.fullName}</td>
      <td>${u.email}</td>
      <td><span class="badge bg-secondary">${u.role}</span></td>
      <td>
        <form method="post" action="${pageContext.request.contextPath}/admin">
          <input type="hidden" name="action" value="role"/>
          <input type="hidden" name="id" value="${u.id}"/>
          <select class="form-select d-inline-block w-auto" name="role">
            <option ${u.role=='APPLICANT'?'selected':''}>APPLICANT</option>
            <option ${u.role=='OFFICER'?'selected':''}>OFFICER</option>
            <option ${u.role=='ADMIN'?'selected':''}>ADMIN</option>
          </select>
          <button class="btn btn-sm btn-primary">Save</button>
        </form>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>

<p class="mt-3">
  <a href="${pageContext.request.contextPath}/${sessionScope.user.role=='ADMIN' ? 'admin/dashboard.jsp' : 'admin/dashboard.jsp'}" 
     class="btn btn-light">⬅ Back</a>
</p>
<%@ include file="../fragments/footer.jsp" %>
