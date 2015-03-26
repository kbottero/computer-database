<%@ tag body-content="empty" %> 
<%@ attribute name="page" required="true" %> 
<%@ attribute name="pageCount" required="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<ul class="pagination">

	<li>
		<c:if test="${page != 1}">
	    	<a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${page - 1}" aria-label="Previous"> 
			<span aria-hidden="true">&laquo;</span></a> 
	    	
	    </c:if>
	</li>
	<c:if test="${pageCount < 6}">
		<c:forEach var="i" begin="1" end="${pageCount}">
			<li><a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${i}">${i}</a></li>
		</c:forEach>
	</c:if>
	<c:if test="${pageCount >= 6}">
		<c:if test="${page < 4}">
			<c:forEach var="i" begin="1" end="3">
				<li><a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${i}">${i}</a></li>
			</c:forEach>
			<c:if test="${pageCount - page < 3}">
				<c:forEach var="i" begin="4" end="${pageCount}">
					<li><a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${i}">${i}</a></li>
				</c:forEach>
			</c:if>
			<c:if test="${pageCount - page >= 3}">
				<li><a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=4">4</a></li>
				<li><a href="#">...</a></li>
				<li><a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${pageCount}">${pageCount}</a></li>
			</c:if>
		</c:if>
		<c:if test="${page >= 4}">
			<li><a href="${pageContext.request.contextPath}/dashboard?numPage=1">1</a></li>
			<li><a href="#">...</a></li>
			<c:if test="${pageCount - page <= 3}">
				<c:if test="${pageCount == page}">
					<li><a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${page - 2}">${page - 2}</a></li>
				</c:if>
				<c:forEach var="i" begin="${page-1}" end="${pageCount}">
					<li><a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${i}">${i}</a></li>
				</c:forEach>
			</c:if>
			<c:if test="${pageCount - page > 3}">
				<li><a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${page - 1}">${page - 1}</a></li>
				<li><a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${page}">${page}</a></li>
				<li><a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${page + 1}">${page + 1}</a></li>
				<li><a href="#">...</a></li>
				<li><a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${pageCount}">${pageCount}</a></li>
			</c:if>
		</c:if>
	</c:if>

	<li>
	<c:if test="${page != pageCount}">
    	<a href="${pageContext.request.contextPath}/dashboard?nbCompPerPage=${nbCompPerPage}&numPage=${page + 1}" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
    	</a>
    </c:if>
  </li>
</ul>

<div class="btn-group btn-group-sm pull-right" role="group">
	<button type="button" class="btn btn-default"
		onclick="document.location.href='dashboard?nbCompPerPage=10'">10</button>
	<button type="button" class="btn btn-default"
		onclick="document.location.href='dashboard?nbCompPerPage=50'">50</button>
	<button type="button" class="btn btn-default"
		onclick="document.location.href='dashboard?nbCompPerPage=100'">100</button>
</div>