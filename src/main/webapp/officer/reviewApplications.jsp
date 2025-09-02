<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../fragments/header.jsp" %>
<c:set var="pageRole" value="role-officer" scope="request"/>

<h3 class="mb-3">Pending / In Review Applications</h3>

<c:if test="${empty applications}">
  <div class="alert alert-info">No applications to review.</div>
</c:if>

<c:forEach var="a" items="${applications}">
  <div class="card mb-4 shadow-sm">
    <div class="card-body">
      <!-- Application Header -->
      <div class="d-flex justify-content-between align-items-start">
        <div>
          <h5 class="fw-bold mb-1">${a.title}</h5>
          <p class="text-muted small mb-0">${a.description}</p>
        </div>
        <span class="badge status-badge fs-6" data-status="${a.status}">${a.status}</span>
      </div>

      <!-- Status Update -->
      <form class="mt-3" method="post" action="${pageContext.request.contextPath}/applications">
        <input type="hidden" name="action" value="updateStatus"/>
        <input type="hidden" name="id" value="${a.id}"/>
        <div class="input-group">
          <select class="form-select" name="status" required>
            <option value="PENDING"   ${a.status=='PENDING' ? 'selected' : ''}>PENDING</option>
            <option value="IN_REVIEW" ${a.status=='IN_REVIEW' ? 'selected' : ''}>IN REVIEW</option>
            <option value="APPROVED"  ${a.status=='APPROVED' ? 'selected' : ''}>APPROVED</option>
            <option value="REJECTED"  ${a.status=='REJECTED' ? 'selected' : ''}>REJECTED</option>
          </select>
          <button type="submit" class="btn btn-primary">Update</button>
        </div>
      </form>

      <!-- Officer Document Upload -->
      <form class="mt-3" method="post" action="${pageContext.request.contextPath}/documents" enctype="multipart/form-data">
        <input type="hidden" name="action" value="upload"/>
        <input type="hidden" name="applicationId" value="${a.id}"/>
        <div class="input-group">
          <input type="file" name="file" class="form-control" accept=".jpg,.jpeg,image/jpeg" required/>
          <button type="submit" class="btn btn-outline-secondary">Upload Report/Letter</button>
        </div>
      </form>

      <!-- Applicant Documents -->
      <h6 class="mt-4">Applicant Documents</h6>
      <c:choose>
        <c:when test="${empty a.documents}">
          <p class="text-muted">No applicant documents uploaded yet.</p>
        </c:when>
        <c:otherwise>
          <div class="table-responsive">
            <table class="table table-sm table-bordered align-middle">
              <thead class="table-light">
                <tr>
                  <th>File</th>
                  <th>Type</th>
                  <th>Uploaded On</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="d" items="${a.documents}">
                  <c:if test="${d.role eq 'APPLICANT'}">
                    <tr>
                      <td>${d.fileName}</td>
                      <td>${d.contentType}</td>
                      <td>${d.uploadedAt}</td>
                    </tr>
                  </c:if>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </c:otherwise>
      </c:choose>

      <!-- Officer Documents -->
      <h6 class="mt-4">Officer Documents</h6>
      <c:choose>
        <c:when test="${empty a.documents}">
          <p class="text-muted">No officer documents uploaded yet.</p>
        </c:when>
        <c:otherwise>
          <div class="table-responsive">
            <table class="table table-sm table-bordered align-middle">
              <thead class="table-light">
                <tr>
                  <th>File</th>
                  <th>Type</th>
                  <th>Uploaded On</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="d" items="${a.documents}">
                  <c:if test="${d.role eq 'OFFICER'}">
                    <tr>
                      <td>${d.fileName}</td>
                      <td>${d.contentType}</td>
                      <td>${d.uploadedAt}</td>
                    </tr>
                  </c:if>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</c:forEach>

<!-- Back button -->
<p class="mt-3">
  <a href="${pageContext.request.contextPath}/${sessionScope.user.role=='OFFICER' ? 'officer/dashboard.jsp' : 'applicant/dashboard.jsp'}" 
     class="btn btn-light">⬅ Back</a>
</p>	

<%@ include file="../fragments/footer.jsp" %>
