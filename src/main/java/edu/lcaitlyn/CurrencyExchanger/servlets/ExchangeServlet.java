package edu.lcaitlyn.CurrencyExchanger.servlets;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.models.ExchangeRate;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepository;
import edu.lcaitlyn.CurrencyExchanger.repositories.ExchangeRatesRepository;
import edu.lcaitlyn.CurrencyExchanger.utils.Utils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.StringWriter;

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

        if (Utils.isNotValidExchangeArgs(from, to, amount) || !Utils.isStringInteger(amount)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неправильно введен запрос. Пример: /exchange?from=USD&to=RUB&amount=10");
            return;
        }

        Currency fromCurrency = currencyRepository.findByName(from);
        Currency toCurrency = currencyRepository.findByName(to);

        if (fromCurrency == null || toCurrency == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Указана не существующая валюта. Пример: /exchange?from=USD&to=RUB&amount=10");
            return;
        }

        ExchangeRate exchangeRate;
        Double rate;
        Integer value = Integer.parseInt(amount);

        if ((exchangeRate = exchangeRatesRepository.findByCodes(from, to)) != null)
            rate = exchangeRate.getRate();
        else if ((exchangeRate = exchangeRatesRepository.findByCodes(to, from)) != null)
            rate = 1 / exchangeRate.getRate();
        else {
            ExchangeRate exchangeRateUSD_A = exchangeRatesRepository.findByCodes("USD", from);
            ExchangeRate exchangeRateUSD_B = exchangeRatesRepository.findByCodes("USD", to);

            if (exchangeRateUSD_A == null || exchangeRateUSD_B == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Не существует курс обмена");
                return;
            }

            rate = exchangeRateUSD_B.getRate() / exchangeRateUSD_A.getRate();
        }

        response.getWriter().write(makeJSON(fromCurrency, toCurrency, rate, value));
    }

//    {
//        "baseCurrency": {
//                "id": 1,
//                "code": "USD",
//                "fullName": "US Dollar",
//                "sign": "$"
//        },
//        "targetCurrency": {
//                "id": 3,
//                "code": "RUB",
//                "fullName": "Russian Ruble",
//                "sign": "₽"
//        },
//        "rate": 63.75,
//        "amount": 10,
//        "convertedAmount": 637.5
//    }
    private String makeJSON(Currency from, Currency to, Double rate, Integer amount) {
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter stringWriter = new StringWriter();
        try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter)) {
            jsonGenerator.setCodec(new ObjectMapper());
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("baseCurrency");
            jsonGenerator.writeObject(from);
            jsonGenerator.writeFieldName("targetCurrency");
            jsonGenerator.writeObject(to);
            jsonGenerator.writeNumberField("rate", rate);
            jsonGenerator.writeNumberField("amount", amount);
            jsonGenerator.writeNumberField("convertedAmount", rate * amount);
            jsonGenerator.writeEndObject();
            jsonGenerator.flush();
            return stringWriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
