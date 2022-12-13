package edu.lcaitlyn.CurrencyExchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lcaitlyn.CurrencyExchanger.models.ExchangeRate;
import edu.lcaitlyn.CurrencyExchanger.repositories.ExchangeRatesRepository;
import edu.lcaitlyn.CurrencyExchanger.utils.Utils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@MultipartConfig
@WebServlet(name = "exchangeRate", value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRatesRepository exchangeRatesRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        exchangeRatesRepository = (ExchangeRatesRepository) config.getServletContext().getAttribute("exchangeRatesRepository");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getMethod());
        if (req.getMethod().equals("PATCH"))
            doPatch(req, resp);
        else
            super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Коды валют пары отсутствуют в адресе. Пример: .../exchangeRate/USDRUB");
            return;
        }

        // ну и тут такой же костыль как в currency
        if (request.getPathInfo().equals("/jsp/exchangeRate.jsp"))
            return;

        String currenciesCodes = request.getPathInfo().replaceFirst("/", "").toUpperCase();

        if (currenciesCodes.length() != 6) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Указана не корректная пара валют. Пример: .../exchangeRate/USDRUB");
            return;
        }

        String baseCurrencyCode = currenciesCodes.substring(0, 3);
        String targetCurrencyCode = currenciesCodes.substring(3, 6);

        ExchangeRate exchangeRate = exchangeRatesRepository.findByCodes(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Обменный курс для пары не найден");
            return;
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(exchangeRate));
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String rate = Utils.getStringFromPartName(request, "rate");

        if (rate == null || rate.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует обменный курс");
            return;
        }

        if (!Utils.isStringDouble(rate)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Введите значение. Пример: 1.05");
            return;
        }


        String currenciesCodes = request.getPathInfo().replaceFirst("/", "").toUpperCase();

        String baseCurrencyCode = currenciesCodes.substring(0, 3);
        String targetCurrencyCode = currenciesCodes.substring(3, 6);

        ExchangeRate exchangeRate = exchangeRatesRepository.findByCodes(baseCurrencyCode, targetCurrencyCode);

        exchangeRate.setRate(Double.parseDouble(rate));
        exchangeRatesRepository.update(exchangeRate);

        exchangeRate = exchangeRatesRepository.findByCodes(targetCurrencyCode, baseCurrencyCode);
        if (exchangeRate != null) {
            exchangeRate.setRate(1 / Double.parseDouble(rate));
            exchangeRatesRepository.update(exchangeRate);
        }

        doGet(request, response);
    }
}
