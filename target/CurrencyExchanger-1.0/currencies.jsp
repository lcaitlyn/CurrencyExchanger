<%@ page import="edu.lcaitlyn.CurrencyExchanger.models.Currency" %>
<%@ page import="java.util.List" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Currencies</title>
</head>
<body>
    <h1>Currency Exchanger by lcaitlyn</h1>

    <h2>Все валюты:</h2>

    <%
        for (Currency c : (List<Currency>) request.getAttribute("currenciesList")) {
            out.println("<ul>");
            out.println("<li>Code: " + c.getCode() + "</li>");
            out.println("<li>Name: " + c.getFullName() + "</li>");
            out.println("<li>Sign: " + c.getSign() + "</li>");
            out.println("</ul>");
        }
    %>

    <h2>Добавить валюту:</h2>

    <form method="post" action="/addCurrency">
        <label>Enter code: <input name="code" type="text" value="${code}"></label><br>
        <label>Enter name: <input type="text" name="fullName" value="${fullName}"></label><br>
        <label>Enter code: <input type="text" name="sign" value="${sign}"></label><br>
        <label><input type="submit" value="Добавить" name="Add"></label><br>
    </form>
</body>
</html>
