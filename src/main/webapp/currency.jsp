<%@ page import="edu.lcaitlyn.CurrencyExchanger.models.Currency" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepository" %>
<%@ page import="edu.lcaitlyn.CurrencyExchanger.exceptions.CurrencyNotFoundException" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Currency</title>
    </head>
    <body>

    <%
        String currencyCode = (String) request.getAttribute("currencyCode");
        CurrencyRepository currencyRepository = (CurrencyRepository) request.getAttribute("currencyRepository");

        try {
            out.println(currencyRepository.findByName(currencyCode));
        } catch (CurrencyNotFoundException e) {
            out.println(e.getMessage());
        }
    %>

    </body>
</html>