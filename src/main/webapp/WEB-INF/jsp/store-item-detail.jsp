<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
  <h2>item detail</h2>
  <p>item name: ${itemEntity.name}</p>
  <p>sku: ${itemEntity.sku}</p>
  <p>unit price: ${itemEntity.price}</p>
  <p>inventory: ${itemEntity.inventory}</p>
  <p>manufacturer: ${itemEntity.manufacturer}</p>
  <p>description: ${itemEntity.description}</p>

  <c:forEach var="url" items="${itemEntity.url2media}">
    <p><img src="${url}" alt="alternatetext"></p>
  </c:forEach>

  <p><a href="/cart/newItem/${itemEntity.id}">
        <button width="400%" height="500%">add to cart</button>
  </p></a>

  <c:forEach var="comment" items="${itemEntity.comments}">
    <p>date: ${comment.dateCreated}</p>
    <p>user: ${comment.userEntity.nickName}</p>
    <p> ${comment.text} </p>
    <br>
  </c:forEach>

  <p><a href="/store-page">store home page</a></p>
</body>
</html>