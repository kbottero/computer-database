<%@ tag body-content="empty" %> 
<%@ attribute name="page" required="true" %> 
<%@ attribute name="pageCount" required="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="mylib" tagdir="/WEB-INF/tags/mytags"%>

<ul class="pagination">

	<li>
		<c:if test="${page != 1}">
			<a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${requestScope.page.current - 1}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>
	    </c:if>
	</li>
	<c:if test="${pageCount < 6}">
		<c:forEach var="i" begin="1" end="${pageCount}">
			<li><a <c:if test="${page == i}">class="selectedLi"</c:if> href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${i}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">${i}</a></li>
		</c:forEach>
	</c:if>
	<c:if test="${pageCount >= 6}">
		<c:if test="${page < 4}">
			<c:forEach var="i" begin="1" end="3">
				<li><a <c:if test="${page == i}">class="selectedLi"</c:if> href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${i}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">${i}</a></li>
			</c:forEach>
			<c:if test="${pageCount - page < 3}">
				<c:forEach var="i" begin="4" end="${pageCount}">
					<li><a <c:if test="${page == i}">class="selectedLi"</c:if> href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${i}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">${i}</a></li>
				</c:forEach>
			</c:if>
			<c:if test="${pageCount - page >= 3}">
				<li><a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="4" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">4</a></li>
				<li><a href="#">...</a></li>
				<li><a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${pageCount}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">${pageCount}</a></li>
			</c:if>
		</c:if>
		<c:if test="${page >= 4}">
			<li><a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">1</a></li>
			<li><a href="#">...</a></li>
			<c:if test="${pageCount - page <= 3}">
				<c:if test="${pageCount == page}">
					<li><a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${page - 2}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">${page - 2}</a></li>
				</c:if>
				<c:forEach var="i" begin="${page-1}" end="${pageCount}">
					<li><a <c:if test="${page == i}">class="selectedLi"</c:if> href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${i}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">${i}</a></li>
				</c:forEach>
			</c:if>
			<c:if test="${pageCount - page > 3}">
				<li><a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${page - 1}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">${page - 1}</a></li>
				<li><a  class="selectedLi" href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${page}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">${page}</a></li>
				<li><a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${page + 1}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">${page + 1}</a></li>
				<li><a href="#">...</a></li>
				<li><a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${pageCount}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>">${pageCount}</a></li>
			</c:if>
		</c:if>
	</c:if>

	<li>
	<c:if test="${page != pageCount}">
    	<a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
			numPage="${requestScope.page.current + 1}" nbCompPerPage="${requestScope.page.pageSize}"
			sortColumn="${requestScope.page.getSortColumn()}" sortColumnOrder="${requestScope.page.sortColumnOrder()}" language="fr"
			/>" aria-label="Next"><span aria-hidden="true">&raquo;</span></a>
    </c:if>
  </li>
</ul>

<div class="btn-group btn-group-sm pull-right" role="group">
	<button type="button" class="btn btn-default"
		onclick="document.location.href='dashboard?nbCompPerPage=10&numPage=1&search=${requestScope.page.param.nameLike}&sortColumn=${requestScope.page.getSortColumn()}&sortColumnOrder=${requestScope.page.sortColumnOrder()}&language='">10</button>
	<button type="button" class="btn btn-default"
		onclick="document.location.href='dashboard?nbCompPerPage=50&numPage=1&search=${requestScope.page.param.nameLike}&sortColumn=${requestScope.page.getSortColumn()}&sortColumnOrder=${requestScope.page.sortColumnOrder()}&language='">50</button>
	<button type="button" class="btn btn-default"
		onclick="document.location.href='dashboard?nbCompPerPage=100&numPage=1&search=${requestScope.page.param.nameLike}&sortColumn=${requestScope.page.getSortColumn()}&sortColumnOrder=${requestScope.page.sortColumnOrder()}&language='">100</button>
</div>