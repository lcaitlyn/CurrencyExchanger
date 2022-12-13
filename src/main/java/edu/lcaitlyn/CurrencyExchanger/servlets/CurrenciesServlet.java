package edu.lcaitlyn.CurrencyExchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepository;
import edu.lcaitlyn.CurrencyExchanger.utils.Utils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.stream.Collectors;

@MultipartConfig
@WebServlet(name = "indexCurrenciesServlet", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;
    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyRepository = (CurrencyRepository) config.getServletContext().getAttribute("currencyRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(resp.getWriter(), currencyRepository.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String code = Utils.getStringFromPartName(req, "code");
        String name = Utils.getStringFromPartName(req, "name");
        String sign = Utils.getStringFromPartName(req, "sign");

        if (isNotValidArgs(code, name, sign)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не правильно введены данные. Пример: code = 'USD', name = 'US Dollar', sign = '$'");
            return;
        }

        if (currencyRepository.findByName(code) != null) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Валюта с таким кодом уже существует");
            return;
        }

        currencyRepository.save(new Currency(code, name, sign.charAt(0)));

        doGet(req, resp);
    }

    public boolean isNotValidArgs(String code, String name, String sign) {
        return  (code == null || name == null || sign == null
                || code.isEmpty() || name.isEmpty() || sign.isEmpty()
                || code.length() != 3 || name.length() > 100 || sign.length() > 3);
    }
}
