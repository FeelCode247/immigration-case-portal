<%@ include file="fragments/header.jsp" %>
<c:set var="pageRole" value="" scope="request"/>
<div class="row justify-content-center">
  <div class="col-md-5">
    <div class="card shadow-sm">
      <div class="card-body">
        <h3 class="mb-3">Login</h3>
        <c:if test="${not empty param.msg}"><div class="alert alert-success">${param.msg}</div></c:if>
        <c:if test="${not empty param.error or not empty error}"><div class="alert alert-danger">${param.error}${error}</div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/auth">
          <input type="hidden" name="action" value="login"/>
          <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" name="email" class="form-control" required/>
          </div>
          <div class="mb-3">
            <label class="form-label">Password</label>
            <input type="password" name="password" class="form-control" required/>
          </div>
          <button class="btn btn-primary">Login</button>
        </form>
      </div>
    </div>
  </div>
</div>
<%@ include file="fragments/footer.jsp" %>
