package edu.lcaitlyn.CurrencyExchanger.servlets;

import edu.lcaitlyn.CurrencyExchanger.repositories.ExchangeRatesRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "exchangeRates", value = "/exchangeRates")
public class IndexExchangeRates extends HttpServlet {
    private ExchangeRatesRepository exchangeRatesRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        exchangeRatesRepository = (ExchangeRatesRepository) config.getServletContext().getAttribute("exchangeRatesRepository");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("exchangeRatesList", exchangeRatesRepository.findAll());
        req.getRequestDispatcher("/exchangeRates.jsp").forward(req, resp);
    }
}
