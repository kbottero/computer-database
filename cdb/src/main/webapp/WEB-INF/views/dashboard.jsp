    <%@include file="header.jsp" %>

    <section id="main">
        <div class="container">
            <h1 id="homeTitle">
                ${page.nbElements} <spring:message code="computers.found" text="computers.found" />
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control"
                         <c:if test="${page.param.nameLike == null || page.param.nameLike == ''}" >placeholder="<spring:message code="search.name" text="search.name" />"</c:if>
                         <c:if test="${page.param.nameLike != ''}" >value="${page.param.nameLike}"</c:if> />
                        <input type="submit" id="searchsubmit" value="<spring:message code="filter.name" text="filter.name" />"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="${pageContext.request.contextPath}/addComputer"><spring:message code="add.computer" text="add.computer" /></a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();"><spring:message code="edit" text="edit" /></a>
                </div>
            </div>
        </div>

        <form id="deleteForm" action="${pageContext.request.contextPath}/deleteComputer" method="POST">
            <input type="hidden" name="selection" value="">
        </form>

        <div class="container" style="margin-top: 10px;">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <!-- Variable declarations for passing labels as parameters -->
                        <!-- Table header for Computer Name -->

                        <th class="editMode" style="width: 60px; height: 22px;">
                            <input type="checkbox" id="selectall" /> 
                            <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                        </th>
                        <th>
                        	<c:choose>
	                        	<c:when test="${requestScope.page.param.colToOrderBy!=null && requestScope.page.param.colToOrderBy.get(0)=='name' && requestScope.page.param.order.toString()=='ASC'}" >
									<a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="name" language="fr"
		                        	sortColumnOrder="DESC"/>">
		                            	<spring:message code="computers.name.th" text="computers.name.th" />
		                            </a>
	                        	</c:when>
	                        	<c:otherwise>
									<a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="name" language="fr"
		                        	sortColumnOrder="ASC"/>">
		                            	<spring:message code="computers.name.th" text="computers.name.th" />
		                            </a>
                        		</c:otherwise>
                        	</c:choose>
                        </th>
                        <th>
                        	<c:choose>
	                        	<c:when test="${requestScope.page.param.colToOrderBy!=null && requestScope.page.param.colToOrderBy.get(0)=='introductionDate' && requestScope.page.param.order.toString()=='ASC'}" >
									<a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="introductionDate" language="fr"
		                        	sortColumnOrder="DESC"/>">
                            			<spring:message code="introduced.date.th" text="introduced.date.th" />
		                            </a>
	                        	</c:when>
	                        	<c:otherwise>
									<a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="introductionDate" language="fr"
		                        	sortColumnOrder="ASC"/>">
                            			<spring:message code="introduced.date.th" text="introduced.date.th" />
		                            </a>
                        		</c:otherwise>
                        	</c:choose>
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                        	<c:choose>
	 	                       	<c:when test="${requestScope.page.param.colToOrderBy!=null && requestScope.page.param.colToOrderBy.get(0)=='discontinuedDate' && requestScope.page.param.order.toString()=='ASC'}" >
									<a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="discontinuedDate" language="fr"
		                        	sortColumnOrder="DESC"/>">
                            			<spring:message code="discontinued.date.th" text="discontinued.date.th" />
		                            </a>
	                        	</c:when>
	                        	<c:otherwise>
									<a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="discontinuedDate" language="fr"
		                        	sortColumnOrder="ASC"/>">
                            			<spring:message code="discontinued.date.th" text="discontinued.date.th" />
		                            </a>
                        		</c:otherwise>
                        	</c:choose>
                        </th>
                        <!-- Table header for Company -->
                        <th>
                        	<c:choose>
	 	                       	<c:when test="${requestScope.page.param.colToOrderBy!=null && requestScope.page.param.colToOrderBy.get(0)=='constructor' && requestScope.page.param.order.toString()=='ASC'}" >
									<a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="constructor" language="fr"
		                        	sortColumnOrder="DESC"/>">
                            			<spring:message code="company.th" text="company.th" />
		                            </a>
	                        	</c:when>
	                        	<c:otherwise>
									<a href="<mylib:link target="dashboard" search="${requestScope.page.param.nameLike}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="constructor" language="fr"
		                        	sortColumnOrder="ASC"/>">
                            			<spring:message code="company.th" text="company.th" />
		                            </a>
                        		</c:otherwise>
                        	</c:choose>
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                	<c:forEach var="c" items="${computers}">
						<tr>
							<td class="editMode">
	                            <input type="checkbox" name="cb" class="cb" value="${c.id}">
	                        </td>
	                        <td>
	                            <a href="${pageContext.request.contextPath}/editComputer?id=${c.id}" id="computer_${c.id}" onclick="">${c.name}</a>
	                        </td>
	                        <td>${c.introductionDate}</td>
	                        <td>${c.discontinuedDate}</td>
	                        <td>${c.constructorName}</td>
						</tr>
					</c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
			<mylib:pagination page="${requestScope.page.current}" pageCount="${requestScope.page.count}"/>  
        </div>
    </footer>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>

</body>
</html>