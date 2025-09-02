<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="fragments/header.jsp" %>

<c:set var="u" value="${sessionScope.user}" />

<h3 class="mb-2">Application #${application.id} — ${application.title}</h3>
<p class="text-muted">${application.description}</p>

<c:if test="${param.msg == 'uploaded'}">
    <div class="alert alert-success">File uploaded successfully!</div>
</c:if>

<c:if test="${param.msg == 'replaced'}">
    <div class="alert alert-info">File replaced successfully!</div>
</c:if>

<c:if test="${param.msg == 'deleted'}">
    <div class="alert alert-warning">File deleted successfully!</div>
</c:if>

<c:if test="${not empty param.error}">
    <div class="alert alert-danger">❌ ${param.error}</div>
</c:if>

<!-- Officer can update status -->
<c:if test="${u.role == 'OFFICER'}">
  <form class="row g-2 mb-3" method="post" action="${pageContext.request.contextPath}/applications">
    <input type="hidden" name="action" value="updateStatus" />
    <input type="hidden" name="id" value="${application.id}" />
    <div class="col-auto">
      <select class="form-select" name="status">
        <option value="PENDING"   ${application.status=='PENDING'?'selected':''}>PENDING</option>
        <option value="IN_REVIEW" ${application.status=='IN_REVIEW'?'selected':''}>IN REVIEW</option>
        <option value="APPROVED"  ${application.status=='APPROVED'?'selected':''}>APPROVED</option>
        <option value="REJECTED"  ${application.status=='REJECTED'?'selected':''}>REJECTED</option>
      </select>
    </div>
    <div class="col-auto">
      <button class="btn btn-primary">Update Status</button>
    </div>
  </form>
</c:if>

<div class="row">
  <!-- Applicant Documents -->
  <div class="col-md-6">
    <h5>Applicant Documents</h5>

    <c:if test="${empty documents}">
      <div class="text-muted small">No documents yet.</div>
    </c:if>

    <table class="table table-sm align-middle">
      <thead>
        <tr><th>File</th><th>Uploaded</th><th>Actions</th></tr>
      </thead>
      <tbody>
        <c:forEach var="d" items="${documents}">
          <c:if test="${d.uploaderRole == 'APPLICANT'}">
            <tr>
              <td>${d.fileName}</td>
              <td><fmt:formatDate value="${d.uploadedAt}" pattern="yyyy-MM-dd HH:mm" /></td>
              <td>
                <a class="btn btn-link btn-sm"
                   href="${pageContext.request.contextPath}/documents?action=download&id=${d.id}">View</a>

                <c:if test="${u.role == 'APPLICANT'}">
                  <!-- Replace -->
                  <form class="d-inline" method="post" action="${pageContext.request.contextPath}/documents" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="replace" />
                    <input type="hidden" name="applicationId" value="${application.id}" />
                    <input type="hidden" name="id" value="${d.id}" />
                    <input type="file" name="file" class="form-control form-control-sm d-inline" style="width:auto;display:inline-block" required />
                    <button class="btn btn-sm btn-outline-secondary">Update</button>
                  </form>

                  <!-- Delete -->
                  <form class="d-inline" method="post" action="${pageContext.request.contextPath}/documents">
                    <input type="hidden" name="action" value="delete" />
                    <input type="hidden" name="applicationId" value="${application.id}" />
                    <input type="hidden" name="id" value="${d.id}" />
                    <button class="btn btn-sm btn-outline-danger" onclick="return confirm('Delete this file?')">Delete</button>
                  </form>
                </c:if>
              </td>
            </tr>
          </c:if>
        </c:forEach>
      </tbody>
    </table>

    <!-- Applicant can upload new docs -->
    <c:if test="${u.role == 'APPLICANT'}">
      <form class="mt-2" method="post" action="${pageContext.request.contextPath}/documents" enctype="multipart/form-data">
        <input type="hidden" name="action" value="upload" />
        <input type="hidden" name="applicationId" value="${application.id}" />
        <div class="input-group">
          <input type="file" name="file" class="form-control" required />
          <button class="btn btn-outline-secondary">Upload</button>
        </div>
      </form>
    </c:if>
  </div>

  <!-- Officer Documents -->
  <div class="col-md-6">
    <h5>Officer Documents</h5>

    <table class="table table-sm align-middle">
      <thead>
        <tr><th>File</th><th>Uploaded</th><th>Actions</th></tr>
      </thead>
      <tbody>
        <c:forEach var="d" items="${documents}">
          <c:if test="${d.uploaderRole == 'OFFICER'}">
            <tr>
              <td>${d.fileName}</td>
              <td><fmt:formatDate value="${d.uploadedAt}" pattern="yyyy-MM-dd HH:mm" /></td>
              <td>
                <a class="btn btn-link btn-sm"
                   href="${pageContext.request.contextPath}/documents?action=download&id=${d.id}">View</a>

                <c:if test="${u.role == 'OFFICER'}">
                  <!-- Replace -->
                  <form class="d-inline" method="post" action="${pageContext.request.contextPath}/documents" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="replace" />
                    <input type="hidden" name="applicationId" value="${application.id}" />
                    <input type="hidden" name="id" value="${d.id}" />
                    <input type="file" name="file" class="form-control form-control-sm d-inline" style="width:auto;display:inline-block" required />
                    <button class="btn btn-sm btn-outline-secondary">Update</button>
                  </form>

                  <!-- Delete -->
                  <form class="d-inline" method="post" action="${pageContext.request.contextPath}/documents">
                    <input type="hidden" name="action" value="delete" />
                    <input type="hidden" name="applicationId" value="${application.id}" />
                    <input type="hidden" name="id" value="${d.id}" />
                    <button class="btn btn-sm btn-outline-danger" onclick="return confirm('Delete this file?')">Delete</button>
                  </form>
                </c:if>
              </td>
            </tr>
          </c:if>
        </c:forEach>
      </tbody>
    </table>

    <!-- Officer can upload new docs -->
    <c:if test="${u.role == 'OFFICER'}">
      <form class="mt-2" method="post" action="${pageContext.request.contextPath}/documents" enctype="multipart/form-data">
        <input type="hidden" name="action" value="upload" />
        <input type="hidden" name="applicationId" value="${application.id}" />
        <div class="input-group">
          <input type="file" name="file" class="form-control" required />
          <button class="btn btn-outline-secondary">Upload</button>
        </div>
      </form>
    </c:if>
  </div>
</div>

<!-- Back button -->
<p class="mt-3">
  <c:choose>
     <c:when test="${u.role == 'OFFICER'}">
    <a href="${pageContext.request.contextPath}/applications?action=pending" class="btn btn-light">Back</a>
  </c:when>
    <c:when test="${u.role == 'APPLICANT'}">
      <a href="${pageContext.request.contextPath}/applications?action=list" class="btn btn-light">Back</a>
    </c:when>
  </c:choose>
</p>

<%@ include file="fragments/footer.jsp" %>
