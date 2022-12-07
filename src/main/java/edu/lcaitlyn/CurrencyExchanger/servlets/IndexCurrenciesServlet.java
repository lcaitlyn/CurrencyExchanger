package edu.lcaitlyn.CurrencyExchanger.servlets;

import com.zaxxer.hikari.HikariDataSource;
import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepositoryImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "indexCurrenciesServlet", value = "/currencies")
public class IndexCurrenciesServlet extends HttpServlet {
    private CurrencyRepositoryImpl currencyRepository;
    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyRepository = (CurrencyRepositoryImpl) config.getServletContext().getAttribute("currencyRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("currenciesList", currencyRepository.findAll());
        req.getRequestDispatcher("/currencies.jsp").forward(req, resp);
    }
}
