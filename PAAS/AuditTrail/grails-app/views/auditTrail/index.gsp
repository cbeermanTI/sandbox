<%--
  Created by IntelliJ IDEA.
  User: jonathanshultis
  Date: 11/23/15
  Time: 11:34 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Audit Trail</title>
</head>
<div class="body"></div>
<g:form action="search">
  <g:select name="facilityRef" from="${facilities}" optionValue="facilityName" optionKey="facilityRef"/>
  <g:submitButton name="List Events"/>
</g:form>
</div>
</html>