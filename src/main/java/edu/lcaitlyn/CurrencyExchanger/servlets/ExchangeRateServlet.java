package edu.lcaitlyn.CurrencyExchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lcaitlyn.CurrencyExchanger.models.ExchangeRate;
import edu.lcaitlyn.CurrencyExchanger.repositories.ExchangeRatesRepository;
import edu.lcaitlyn.CurrencyExchanger.utils.Utils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

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

        String currenciesCodes = request.getPathInfo().replaceFirst("/", "").toUpperCase();

        if (currenciesCodes.length() != 6) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Указана не корректная пара валют. Пример: .../exchangeRate/USDRUB");
            return;
        }

        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(
                currenciesCodes.substring(0, 3), currenciesCodes.substring(3, 6));

        if (!exchangeRate.isPresent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Обменный курс для пары не найден");
            return;
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(exchangeRate.get()));
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(
                currenciesCodes.substring(0, 3), currenciesCodes.substring(3, 6));

        if (exchangeRate.isPresent()) {
            exchangeRate.get().setRate(BigDecimal.valueOf(Double.parseDouble(rate)));
            exchangeRatesRepository.update(exchangeRate.get());
        }

        doGet(request, response);
    }
}
