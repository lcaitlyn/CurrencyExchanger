package edu.lcaitlyn.CurrencyExchanger.servlets;

import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.config.ApplicationConfig;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepositoryImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CurrenciesServlet", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyRepositoryImpl currencyRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        currencyRepository = context.getBean(CurrencyRepositoryImpl.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();

        for (Currency c : currencyRepository.findAll()) {
            pw.write("<h1>");
            pw.write("Code = " + c.getCode() +
                    ", FullName = " + c.getFullName() +
                    ", Sign = " + c.getSign());
            pw.write("</h1>");
        }
    }
}
