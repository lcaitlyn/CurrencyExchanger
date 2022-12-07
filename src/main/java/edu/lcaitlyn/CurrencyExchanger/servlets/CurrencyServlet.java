package edu.lcaitlyn.CurrencyExchanger.servlets;

import edu.lcaitlyn.CurrencyExchanger.exceptions.CurrencyNotFoundException;
import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.repositories.CrudRepository;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepositoryImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CurrencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyRepositoryImpl currencyRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyRepository = (CurrencyRepositoryImpl) config.getServletContext().getAttribute("currencyRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo().equals("/currency.jsp"))
            return;

        String currencyCode = request.getPathInfo().replaceFirst("/", "").toUpperCase();

        request.setAttribute("currencyCode", currencyCode);
        request.setAttribute("currencyRepository", currencyRepository);

        request.getRequestDispatcher("/currency.jsp").forward(request, response);
    }
}
