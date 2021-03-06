<%--
  Created by IntelliJ IDEA.
  User: Alexey
  Date: 29.12.2017
  Time: 10:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../util/localized.jsp" %>
<html>
<head>
    <%--<%@include file="../util/bootstrap.jsp" %>--%>
    <%--<title><fmt:message key="adminZone"/></title>--%>
    <%@include file="../util/header.jsp" %>
</head>
<body>

<div class="container">
    <%@ include file="../util/navbar.jsp" %>
    <h3><a href="/admin/${meta.names.singular}/show_all"><fmt:message key="back"/></a></h3>
    <h3><a href="/admin"><fmt:message key="toMainPage"/></a></h3>
    <c:choose>
        <c:when test="${empty action}">
            <c:set var="action" value="create"/>
        </c:when>
        <c:otherwise>
            <c:set var="action" value="update"/>
        </c:otherwise>
    </c:choose>
    <form action="/admin/${meta.names.singular}/${action}"
          method="post"
          role="form"
          data-toggle="validator"
    >
        <input type="hidden" name="id" value="${entity.id}"/>
        <h2><fmt:message key="editing"/></h2>
        <div class="form-group col-xs-4">
            <c:forEach var="field" items="${meta.fields.keySet()}">
                <label for="${field}"
                       class="control-label col-xs-4"
                >${field}</label>

                <c:set var="fieldType" value="${meta.fields[field].stringType}"/>
                <c:set var="controlName" value="${meta.fields[field].dbName}"/>
                <c:choose>
                    <c:when test="${fieldType == 'string'}">
                        <input type="text"
                               name="${controlName}"
                               id="${field}"
                               class="form-control"
                               value="${entity[field]}"
                        />
                    </c:when>
                    <c:when test="${fieldType == 'integer'}">
                        <input type="number"
                               name="${controlName}"
                               id="${field}"
                               class="form-control"
                               value="${entity[field]}"
                        />
                    </c:when>
                    <c:when test="${fieldType == 'decimal'}">
                        <input type="number"
                               name="${controlName}"
                               id="${field}"
                               class="form-control"
                               value="${entity[field]}"
                        />
                    </c:when>
                    <c:when test="${fieldType == 'foreign'}">
                        <select id="${field}"
                                name="${controlName}"
                                class="form-control"
                        >
                            <c:forEach var="relative" items="${requestScope[meta.fields[field].relatedName]}">
                                <option value="${relative.id}"
                                        name="${controlName}"
                                        <c:if test="${relative.id.value == entity[field].value.id.value}">selected</c:if>
                                >${relative.displayName}</option>
                            </c:forEach>
                        </select>
                    </c:when>
                    <c:when test="${fieldType == 'date'}">
                        <input type="date"
                               class="form-control"
                               value="${entity[field].asLocalDate()}"
                               id="${field}"
                               name="${controlName}"
                        />
                    </c:when>
                    <c:when test="${fieldType == 'boolean'}">
                        <select id="${field}"
                                name="${controlName}"
                                class="form-control"
                        >
                            <option name="${controlName}"
                                    value="true"
                                    <c:if test="${entity[field].value}">selected</c:if>
                            ><fmt:message key="yes"/></option>
                            <option name="${controlName}"
                                    value="false"
                                    <c:if test="${entity[field].value == false}">selected</c:if>
                            ><fmt:message key="no"/></option>
                        </select>
                    </c:when>
                </c:choose>

                <br>
            </c:forEach>

            <button type="submit" class="btn btn-primary  btn-md"><fmt:message key="accept"/></button>
        </div>
    </form>
</div>
</body>
</html>
