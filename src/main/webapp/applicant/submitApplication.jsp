<%@ include file="../fragments/header.jsp" %> 
<c:set var="pageRole" value="role-applicant" scope="request"/> 

<h3 class="mb-3">Submit Application</h3> 

<form method="post" action="${pageContext.request.contextPath}/applications"> 
    <input type="hidden" name="action" value="create"/> 

    <div class="mb-3"> 
        <label class="form-label">Title</label> 
        <input type="text" name="title" class="form-control" required/> 
    </div> 

    <div class="mb-3"> 
        <label class="form-label">Description</label> 
        <textarea name="description" rows="4" class="form-control"></textarea> 
    </div> 

    <button class="btn btn-primary">Submit</button> 
</form> 

<%@ include file="../fragments/footer.jsp" %>

