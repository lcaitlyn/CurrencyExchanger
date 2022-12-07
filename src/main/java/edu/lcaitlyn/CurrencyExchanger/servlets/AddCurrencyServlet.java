package edu.lcaitlyn.CurrencyExchanger.servlets;

import com.zaxxer.hikari.HikariDataSource;
import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepository;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AddCurrencyServlet", value = "/addCurrency")
public class AddCurrencyServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;
    private HikariDataSource makeHikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("org.postgresql.Driver");
        hikariDataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        hikariDataSource.setUsername("postgres");
        hikariDataSource.setPassword("");
        return hikariDataSource;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyRepository = new CurrencyRepository(makeHikariDataSource());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String code = req.getParameter("code").trim().toUpperCase();
        String fullName = req.getParameter("fullName").trim();
        String sign = req.getParameter("sign").trim();

        if (!checkArgs(code, fullName, sign))
            resp.sendRedirect(req.getContextPath() + "/");

        currencyRepository.save(new Currency(code, fullName, sign.charAt(0)));
        req.setAttribute("currenciesList", currencyRepository.findAll());

        resp.sendRedirect(req.getContextPath() + "/currencies");
    }

    private boolean checkArgs(String code, String fullName, String sign) {
        if (code == null || fullName == null || sign == null
                || code.isEmpty() || fullName.isEmpty() || sign.isEmpty()
                || code.length() > 10 || fullName.length() > 100 || sign.length() != 1)
            return false;

        return true;
    }
}
