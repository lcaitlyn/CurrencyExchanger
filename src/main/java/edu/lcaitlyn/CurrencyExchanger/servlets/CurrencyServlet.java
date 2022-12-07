package edu.lcaitlyn.CurrencyExchanger.servlets;

import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepository;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CurrencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyRepository = (CurrencyRepository) config.getServletContext().getAttribute("currencyRepository");
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
