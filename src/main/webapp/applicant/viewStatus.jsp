<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../fragments/header.jsp" %>

<h3 class="mb-3">Application Status & Documents</h3>

<c:if test="${empty applications}">
  <div class="alert alert-info">
    No applications found. 
    <a href="submitApplication.jsp">Submit one now</a>.
  </div>
</c:if>

<c:if test="${not empty applications}">
  <c:forEach var="a" items="${applications}">
    <div class="card mb-4">
      <div class="card-header d-flex justify-content-between align-items-center">
        <div>
          <h5 class="mb-0">${a.title}</h5>
          <small class="text-muted">${a.description}</small>
        </div>
        <span class="badge status-badge" data-status="${a.status}">
          ${a.status}
        </span>
      </div>

      <div class="card-body">
        <!-- Upload new documents (KEEP THIS PART) -->
        <form method="post" action="${pageContext.request.contextPath}/documents" 
              enctype="multipart/form-data" class="mb-3">
          <input type="hidden" name="action" value="upload"/>
          <input type="hidden" name="applicationId" value="${a.id}"/>
          <div class="input-group">
            <input type="file" name="file" class="form-control" required/>
            <button class="btn btn-outline-secondary">Upload</button>
          </div>
        </form>

        <!-- Documents table -->
        <h6>Uploaded Documents</h6>
        <c:if test="${empty a.documents}">
          <div class="alert alert-secondary">No documents uploaded yet.</div>
        </c:if>
        <c:if test="${not empty a.documents}">
          <table class="table table-bordered table-sm">
            <thead class="table-light">
              <tr>
                <th>File Name</th>
                <th>Uploader</th>
                <th>Role</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="doc" items="${a.documents}">
                <tr>
                  <td>${doc.fileName}</td>
                  <td>${doc.uploadedBy}</td>
                  <td>${doc.role}</td>
                  <td>${doc.uploadedAt}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:if>
      </div>
    </div>
  </c:forEach>
</c:if>

<!-- Back button -->
<p class="mt-3">
  <a href="${pageContext.request.contextPath}/${sessionScope.user.role=='OFFICER' ? 'officer/dashboard.jsp' : 'applicant/dashboard.jsp'}" 
     class="btn btn-light">⬅ Back</a>
</p>

<%@ include file="../fragments/footer.jsp" %>
