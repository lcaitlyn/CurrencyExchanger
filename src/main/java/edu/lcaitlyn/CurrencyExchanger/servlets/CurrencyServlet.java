package edu.lcaitlyn.CurrencyExchanger.servlets;

import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.repositories.CrudRepository;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepositoryImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CurrencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyRepositoryImpl currencyRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyRepository = (CurrencyRepositoryImpl) config.getServletContext().getAttribute("currencyRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currency = request.getPathInfo().replaceFirst("/", "");

        System.out.println(currency);
//        for (Currency c : currencyRepository.findAll()) {
//            if (c.getCode().equals(request.getPathInfo()))
//
//        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
