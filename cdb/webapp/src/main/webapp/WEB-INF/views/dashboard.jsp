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
                         <c:if test="${page.search == null || page.search == ''}" >placeholder="<spring:message code="search.name" text="search.name" />"</c:if>
                         <c:if test="${page.search != ''}" >value="${page.search}"</c:if> />
                        <input type="submit" id="searchsubmit" value="<spring:message code="filter.name" text="filter.name" />"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="${pageContext.request.contextPath}/Computer/add"><spring:message code="add.computer" text="add.computer" /></a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();"><spring:message code="edit" text="edit" /></a>
                </div>
            </div>
        </div>

        <form id="deleteForm" action="${pageContext.request.contextPath}/Computer/delete" method="POST">
            <input type="hidden" name="selection" value="">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
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
	                        	<c:when test="${requestScope.page.sort!=null && requestScope.page.sort.getOrderFor('name')!=null && requestScope.page.sort.getOrderFor('name').getDirection().name()=='ASC'}" >
									<a href="<mylib:link target="dashboard" search="${requestScope.page.search}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="name" language="fr"
		                        	sortColumnOrder="DESC"/>">
		                            	<spring:message code="computers.name.th" text="computers.name.th" />
		                            </a>
	                        	</c:when>
	                        	<c:otherwise>
									<a href="<mylib:link target="dashboard" search="${requestScope.page.search}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="name" language="fr"
		                        	sortColumnOrder="ASC"/>">
		                            	<spring:message code="computers.name.th" text="computers.name.th" />
		                            </a>
                        		</c:otherwise>
                        	</c:choose>
                        </th>
                        <th>
                        	<c:choose>
	                        	<c:when test="${requestScope.page.sort!=null && requestScope.page.sort.getOrderFor('introduced')!=null && requestScope.page.sort.getOrderFor('introduced').getDirection().name()=='ASC'}" >
									<a href="<mylib:link target="dashboard" search="${requestScope.page.search}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="introduced" language="fr"
		                        	sortColumnOrder="DESC"/>">
                            			<spring:message code="introduced.date.th" text="introduced.date.th" />
		                            </a>
	                        	</c:when>
	                        	<c:otherwise>
									<a href="<mylib:link target="dashboard" search="${requestScope.page.search}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="introduced" language="fr"
		                        	sortColumnOrder="ASC"/>">
                            			<spring:message code="introduced.date.th" text="introduced.date.th" />
		                            </a>
                        		</c:otherwise>
                        	</c:choose>
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                        	<c:choose>
	 	                       	<c:when test="${requestScope.page.sort!=null && requestScope.page.sort.getOrderFor('discontinued')!=null && requestScope.page.sort.getOrderFor('discontinued').getDirection().name()=='ASC'}" >
									<a href="<mylib:link target="dashboard" search="${requestScope.page.search}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="discontinued" language="fr"
		                        	sortColumnOrder="DESC"/>">
                            			<spring:message code="discontinued.date.th" text="discontinued.date.th" />
		                            </a>
	                        	</c:when>
	                        	<c:otherwise>
									<a href="<mylib:link target="dashboard" search="${requestScope.page.search}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="discontinued" language="fr"
		                        	sortColumnOrder="ASC"/>">
                            			<spring:message code="discontinued.date.th" text="discontinued.date.th" />
		                            </a>
                        		</c:otherwise>
                        	</c:choose>
                        </th>
                        <!-- Table header for Company -->
                        <th>
                        	<c:choose>
	 	                       	<c:when test="${requestScope.page.sort!=null && requestScope.page.sort.getOrderFor('constructor')!=null && requestScope.page.sort.getOrderFor('constructor').getDirection().name()=='ASC'}" >
									<a href="<mylib:link target="dashboard" search="${requestScope.page.search}"
		                        	numPage="${1}" nbCompPerPage="${requestScope.page.pageSize}" sortColumn="constructor" language="fr"
		                        	sortColumnOrder="DESC"/>">
                            			<spring:message code="company.th" text="company.th" />
		                            </a>
	                        	</c:when>
	                        	<c:otherwise>
									<a href="<mylib:link target="dashboard" search="${requestScope.page.search}"
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
	                            <a href="${pageContext.request.contextPath}/Computer/edit?id=${c.id}" id="computer_${c.id}" onclick="">${c.name}</a>
	                        </td>
	                        <td>${c.introduced}</td>
	                        <td>${c.discontinued}</td>
	                        <td>${c.companyName}</td>
						</tr>
					</c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
			<mylib:pagination page="${requestScope.page.pageNumber}" pageCount="${requestScope.page.numberOfPages}"/>  
        </div>
    </footer>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>

</body>
</html>