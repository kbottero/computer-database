    <%@include file="header.jsp" %>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                        id: ${computer.id}
                    </div>
                    <h1><spring:message code="edit.computer" text="edit.computer" /></h1>

                    <form action="save" method="GET" id="formEditComputer">
                        <input type="hidden" name ="id" value="${computer.id}"/>
                        <fieldset>
                            <div class="form-group">
                                <label for="name"><spring:message code="computers.name.th" text="computers.name.th" /></label>
                                <input type="text" class="form-control" name ="name" id="name" placeholder="Computer name" value="${computer.name}">
                            </div>
                            <div class="form-group">
                                <label for="introduced"><spring:message code="introduced.date.th" text="introduced.date.th" /></label>
                                <input type="date" class="form-control" name ="introduced" id="introduced" placeholder="dd/mm/yyyy" value="${computer.introduced}">
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><spring:message code="discontinued.date.th" text="discontinued.date.th" /></label>
                                <input type="date" class="form-control" name ="discontinued" id="discontinued" placeholder="dd/mm/yyyy" value="${computer.discontinued}">
                            </div>
                            <div class="form-group">
                                <label for="companyId"><spring:message code="company.th" text="company.th" /></label>
                                <select name ="companyId" class="form-control" id="companyId" >
                                   	<option value="0" <c:if test="${computer.companyName == null}">selected</c:if>>--</option>
                                    <c:forEach var="c" items="${companies}">
                                    	<option value="${c.id}" <c:if test="${c.name == computer.companyName}">selected</c:if> >${c.name}</option>
									</c:forEach>
                                </select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<spring:message code="edit" text="edit" />" class="btn btn-primary" id="formEditComputerSubmit">
                            <spring:message code="or" text="or" />
                            <a href="${prev}" class="btn btn-default"><spring:message code="cancel" text="cancel" /></a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
<script src="${pageContext.request.contextPath}/js/computerValidator.js"></script>
</body>
</html>