<%@ include file="fragments/header.jsp" %>
<c:set var="pageRole" value="" scope="request"/>
<div class="row g-4">
  <div class="col-md-6">
    <div class="card shadow-sm">
      <div class="card-body">
        <h3 class="card-title">Welcome to Case Portal</h3>
        <p>Submit and track applications with ease.</p>
        <a href="register.jsp" class="btn btn-primary">Get Started</a>
        <a href="login.jsp" class="btn btn-outline-secondary">Login</a>
      </div>
    </div>
  </div>
</div>
<%@ include file="fragments/footer.jsp" %>
