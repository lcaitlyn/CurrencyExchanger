<%@ page import="edu.lcaitlyn.CurrencyExchanger.models.ExchangeRate" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ExchangeRates</title>
</head>
<body>
    <%
        out.println(request.getAttribute("exchangeRatesList"));
        for (ExchangeRate rate : (List<ExchangeRate>) request.getAttribute("exchangeRatesList")) {
            out.println("<li>");
            out.println("Base Currency: " + rate.getBaseCurrency().toString().replaceAll("\n", "<br>"));
            out.println("Target Currency: " + rate.getTargetCurrency().toString().replaceAll("\n", "<br>"));
            out.println("Rate: " + rate.getRate());
            out.println("</li>");
        }
    %>
</body>
</html>
