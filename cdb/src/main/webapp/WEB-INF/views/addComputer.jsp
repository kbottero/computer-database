    <%@include file="header.jsp" %>

    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1><spring:message code="add.computer" text="add.computer" /></h1>
                    <form action="insertComputer" method="POST">
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName"><spring:message code="computers.name.th" text="computers.name.th" /></label>
                                <input type="text" class="form-control" name="computerName" id="computerName" placeholder="Computer name">
                            </div>
                            <div class="form-group">
                                <label for="introduced"><spring:message code="introduced.date.th" text="introduced.date.th" /></label>
                                <input type="date" class="form-control" name="introduced" id="introduced" placeholder="Introduced date">
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><spring:message code="discontinued.date.th" text="discontinued.date.th" /></label>
                                <input type="date" class="form-control" name="discontinued" id="discontinued" placeholder="Discontinued date">
                            </div>
                            <div class="form-group">
                                <label for="companyId"><spring:message code="company.th" text="company.th" /></label>
                                <select class="form-control" name="companyId" id="companyId" >
                                  	<option value="0" selected>--</option>
                                    <c:forEach var="c" items="${companies}">
                                    	<option value="${c.id}">${c.name}</option>
									</c:forEach>
                                </select>
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<spring:message code="add" text="add" />" class="btn btn-primary">
                            <spring:message code="or" text="or" />
                            <a href="${prev}" class="btn btn-default"><spring:message code="cancel" text="cancel" /></a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/computerValidator.js"></script>
</body>
</html>