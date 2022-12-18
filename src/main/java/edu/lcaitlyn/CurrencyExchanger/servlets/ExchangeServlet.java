package edu.lcaitlyn.CurrencyExchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lcaitlyn.CurrencyExchanger.dto.Exchange;
import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.models.ExchangeRate;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepository;
import edu.lcaitlyn.CurrencyExchanger.repositories.ExchangeRatesRepository;
import edu.lcaitlyn.CurrencyExchanger.utils.Utils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet(name = "ExchangeServlet", value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;
    private ExchangeRatesRepository exchangeRatesRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyRepository = (CurrencyRepository) config.getServletContext().getAttribute("currencyRepository");
        exchangeRatesRepository = (ExchangeRatesRepository) config.getServletContext().getAttribute("exchangeRatesRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String amount = request.getParameter("amount");

        if (Utils.isNotValidExchangeArgs(from, to, amount) || !Utils.isStringDouble(amount)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неправильно введен запрос. Пример: /exchange?from=USD&to=RUB&amount=10");
            return;
        }

        if (!isCurrenciesValid(from, to)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Указана не существующая валюта. Пример: /exchange?from=USD&to=RUB&amount=10");
            return;
        }

        BigDecimal rate = getRate(from, to);

        if (rate == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Не существует курс обмена");
            return;
        }

        new ObjectMapper().writeValue(response.getWriter(), new Exchange(
                currencyRepository.findByName(from).get(),
                currencyRepository.findByName(to).get(),
                rate,
                new BigDecimal(amount),
                rate.multiply(new BigDecimal(amount))
        ));
    }

    private boolean isCurrenciesValid(String from, String to) {
        Optional<Currency> fromCurrency = currencyRepository.findByName(from);
        Optional<Currency> toCurrency = currencyRepository.findByName(to);

        return (fromCurrency.isPresent() && toCurrency.isPresent());
    }

    private BigDecimal getRate(String from, String to) {
        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(from, to);

        if (exchangeRate.isPresent())
            return exchangeRate.get().getRate();

        Optional<ExchangeRate> reverseExchangeRate = exchangeRatesRepository.findByCodes(to, from);

        if (reverseExchangeRate.isPresent())
            return reverseExchangeRate.get().getRate();

        Optional<ExchangeRate> exchangeRateUSD_A = exchangeRatesRepository.findByCodes("USD", from);
        Optional<ExchangeRate> exchangeRateUSD_B = exchangeRatesRepository.findByCodes("USD", to);

        if (exchangeRateUSD_A.isPresent() && exchangeRateUSD_B.isPresent()) {
            return exchangeRateUSD_A.get().getRate().divide(exchangeRateUSD_B.get().getRate());
        }

        return null;
    }
}
