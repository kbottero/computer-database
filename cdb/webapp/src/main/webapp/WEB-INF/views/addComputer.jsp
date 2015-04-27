    <%@include file="header.jsp" %>

    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1><spring:message code="add.computer" text="add.computer" /></h1>
                    <form:form action="add" method="POST" commandName="newComputer">
								<spring:message code="computers.name.th" var="computerName"/>
								<spring:message code="date.format" var="dateFormat" />
                        <fieldset>
                            <div class="form-group">
                                <form:label path="name" for="name"><spring:message code="computers.name.th" text="computers.name.th" /></form:label>
                                <form:errors path="name" cssClass="error" />
                                <form:input path="name" type="text" cssClass="form-control" name="name" id="name" placeholder="${computerName}"/>
                            </div>
                            <div class="form-group">
                                <form:label path="introduced" for="introduced"><spring:message code="introduced.date.th" text="introduced.date.th" /></form:label>
                                <form:errors path="introduced" cssClass="error" />
                                <form:input path="introduced" type="date" cssClass="form-control" name="introduced" id="introduced" placeholder="${dateFormat}" />
                            </div>
                            <div class="form-group">
                                <form:label path="discontinued" for="discontinued"><spring:message code="discontinued.date.th" text="discontinued.date.th" /></form:label>
                                <form:errors path="discontinued" cssClass="error" />
                                <form:input path="discontinued" type="date" cssClass="form-control" name="discontinued" id="discontinued" placeholder="${dateFormat}"/>
                            </div>
                            <div class="form-group">
                                <form:label path="companyId" for="companyId"><spring:message code="company.th" text="company.th" /></form:label>
                                <form:errors path="companyId" cssClass="error" />
                               	<form:select path="companyId" name ="companyId" cssClass="form-control" id="companyId" >
                                   	<form:option value="0" >--</form:option>
									<form:options items="${companies}" itemValue="id" itemLabel="name" />
                                </form:select>
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<spring:message code="add" text="add" />" class="btn btn-primary">
                            <spring:message code="or" text="or" />
                            <a href="${prev}" class="btn btn-default"><spring:message code="cancel" text="cancel" /></a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </section>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/computerValidator.js"></script>
</body>
</html>