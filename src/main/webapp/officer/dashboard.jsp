<%@ include file="../fragments/header.jsp" %>
<c:set var="pageRole" value="role-officer" scope="request"/>
<h3 class="mb-3">Officer Dashboard</h3>
<a href="${pageContext.request.contextPath}/applications?action=pending" class="btn btn-primary">Review Applications</a>
<%@ include file="../fragments/footer.jsp" %>
