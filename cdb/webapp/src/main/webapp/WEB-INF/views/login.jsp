    <%@include file="header.jsp" %>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <form:form action="login" method="POST">
						<spring:message code="log" var="Log"/>
                        <fieldset>
                            <div class="form-group">
                                <label for="username"><spring:message code="login" text="login" /></label>
                                <input type="text" class="form-control <c:if test="${not empty error}">error</c:if>" name="username" id="username" placeholder="${login}" required/>
                            </div>
                            <div class="form-group">
                                <label for="password"><spring:message code="password" text="password" /></label>
                                <input type="password" class="form-control <c:if test="${not empty error}">error</c:if>" name="password" id="password" placeholder="${password}" required/>
                            </div>
								<input class="btn btn-primary" name="submit" type="submit" value="${Log}" />
                        </fieldset>	
                    </form:form>
                </div>
            </div>
        </div>
    </section>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
</body>
</html>