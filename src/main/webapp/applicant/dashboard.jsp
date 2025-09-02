<%@ include file="../fragments/header.jsp" %>
<c:set var="pageRole" value="role-applicant" scope="request"/>
<h3 class="mb-3">Applicant Dashboard</h3>
<div class="mb-3">
  <a href="${pageContext.request.contextPath}/applicant/submitApplication.jsp" class="btn btn-light border">Submit New Application</a>
  <a href="${pageContext.request.contextPath}/applications?action=list" class="btn btn-primary">View My Applications</a>
</div>
<%@ include file="../fragments/footer.jsp" %>
