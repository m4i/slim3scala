<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<jsp:include page="/header.jsp">
  <jsp:param name="title" value="twitter Index"/>
</jsp:include>

<p>What are you doing?</p>
<form method="post" action="tweet">
  <div><textarea name="content"></textarea></div>
  <div><input type="submit" value="tweet"/></div>
</form>

<c:forEach var="e" items="${tweetList}">
  <p>${f:h(e.content)}</p>
  <hr />
</c:forEach>

<jsp:include page="/footer.jsp"/>
