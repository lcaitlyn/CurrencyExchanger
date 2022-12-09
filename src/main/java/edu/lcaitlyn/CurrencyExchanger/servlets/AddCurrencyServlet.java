package edu.lcaitlyn.CurrencyExchanger.servlets;

import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepository;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AddCurrencyServlet", value = "/addCurrency")
public class AddCurrencyServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyRepository = (CurrencyRepository) config.getServletContext().getAttribute("currencyRepository");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String code = req.getParameter("code").trim().toUpperCase();
        String fullName = req.getParameter("fullName").trim();
        String sign = req.getParameter("sign").trim();

        if (isValidArgs(code, fullName, sign)) {
            currencyRepository.save(new Currency(code, fullName, sign.charAt(0)));
            req.setAttribute("currenciesList", currencyRepository.findAll());
        }

        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/currencies");
    }

    private boolean isValidArgs(String code, String fullName, String sign) {
        if (code == null || fullName == null || sign == null
                || code.isEmpty() || fullName.isEmpty() || sign.isEmpty()
                || code.length() > 10 || fullName.length() > 100 || sign.length() != 1)
            return false;

        return true;
    }
}
