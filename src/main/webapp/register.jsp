<%@ include file="fragments/header.jsp" %>
<c:set var="pageRole" value="" scope="request"/>
<div class="row justify-content-center">
  <div class="col-md-6">
    <div class="card shadow-sm">
      <div class="card-body">
        <h3 class="mb-3">Register</h3>
        <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/auth">
          <input type="hidden" name="action" value="register"/>
          <div class="mb-3">
            <label class="form-label">Full Name</label>
            <input type="text" name="fullName" class="form-control" required/>
          </div>
          <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" name="email" class="form-control" required/>
          </div>
          <div class="mb-3">
            <label class="form-label">Password</label>
            <input type="password" name="password" class="form-control" required/>
          </div>
          <button class="btn btn-primary">Create Account</button>
        </form>
      </div>
    </div>
  </div>
</div>
<%@ include file="fragments/footer.jsp" %>
