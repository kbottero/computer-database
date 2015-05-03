<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="search" required="false" %>
<%@ attribute name="numPage" required="false" %>
<%@ attribute name="nbCompPerPage" required="false" %>
<%@ attribute name="sortColumn" required="false" %>
<%@ attribute name="sortColumnOrder" required="false" %>
<c:url value="${target}?search=${search}&numPage=${numPage}&nbCompPerPage=${nbCompPerPage}&sortColumn=${sortColumn}&sortColumnOrder=${sortColumnOrder}" />