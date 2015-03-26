    <%@include file="header.jsp" %>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                        id: ${computer.id}
                    </div>
                    <h1>Edit Computer</h1>

                    <form action="saveComputer" method="GET" id="formEditComputer">
                        <input type="hidden" name ="computerId"value="${computer.id}"/>
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <input type="text" class="form-control" name ="computerName" id="computerName" placeholder="Computer name" value="${computer.name}">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="date" class="form-control" name ="introduced" id="introduced" placeholder="Introduced date" value="${computer.introductionDate}">
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="date" class="form-control" name ="discontinued" id="discontinued" placeholder="Discontinued date" value="${computer.discontinuedDate}">
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select name ="companyId" class="form-control" id="companyId" >
                                   	<option value="0" <c:if test="${computer.constructorName == null}">selected</c:if>>--</option>
                                    <c:forEach var="c" items="${companies}">
                                    	<option value="${c.id}" <c:if test="${c.name == computer.constructorName}">selected</c:if> >${c.name}</option>
									</c:forEach>
                                </select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Edit" class="btn btn-primary" id="formEditComputerSubmit">
                            or
                            <a href="${prev}" class="btn btn-default">Cancel</a>
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