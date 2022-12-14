package edu.lcaitlyn.CurrencyExchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.models.ExchangeRate;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepository;
import edu.lcaitlyn.CurrencyExchanger.repositories.ExchangeRatesRepository;
import edu.lcaitlyn.CurrencyExchanger.utils.Utils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig
@WebServlet(name = "exchangeRates", value = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRatesRepository exchangeRatesRepository;
    private CurrencyRepository currencyRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        exchangeRatesRepository = (ExchangeRatesRepository) config.getServletContext().getAttribute("exchangeRatesRepository");
        currencyRepository = (CurrencyRepository) config.getServletContext().getAttribute("currencyRepository");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        new ObjectMapper().writeValue(resp.getWriter(), exchangeRatesRepository.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = Utils.getStringFromPartName(req, "baseCurrencyCode");
        String targetCurrencyCode = Utils.getStringFromPartName(req, "targetCurrencyCode");
        String rate = Utils.getStringFromPartName(req, "rate");

        if (Utils.isNotValidExchangeArgs(baseCurrencyCode, targetCurrencyCode, rate)
                || !Utils.isStringDouble(rate)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не правильно введены данные. Пример: baseCurrency = 'USD', targetCurrency = 'EUR', rate = '1.05'");
            return;
        }

        Currency baseCurrency = currencyRepository.findByName(baseCurrencyCode);
        Currency targetCurrency = currencyRepository.findByName(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Валюта не найдена");
            return;
        }

        if (exchangeRatesRepository.findByCodes(baseCurrencyCode, targetCurrencyCode) != null) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Валютная пара с таким кодом уже существует");
            return;
        }

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, Double.parseDouble(rate));

        exchangeRatesRepository.save(exchangeRate);

        // проверка, если есть такой же, только в другом порядке, то его обновляю
        exchangeRate = exchangeRatesRepository.findByCodes(targetCurrencyCode, baseCurrencyCode);

        if (exchangeRate != null) {
            exchangeRate.setRate(1 / Double.parseDouble(rate));
            exchangeRatesRepository.update(exchangeRate);
        }

        doGet(req, resp);
    }
}
