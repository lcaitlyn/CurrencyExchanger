package edu.lcaitlyn.CurrencyExchanger.servlets;

import com.zaxxer.hikari.HikariDataSource;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepository;
import edu.lcaitlyn.CurrencyExchanger.repositories.ExchangeRatesRepository;

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {

    public ContextListener() {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        CurrencyRepository currencyRepository = new CurrencyRepository(makeHikariDataSource(context));
        ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository(makeHikariDataSource(context));

        context.setAttribute("currencyRepository", currencyRepository);
        context.setAttribute("exchangeRatesRepository", exchangeRatesRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private HikariDataSource makeHikariDataSource(ServletContext context) {
        try {
            Properties properties = new Properties();
            HikariDataSource hikariDataSource = new HikariDataSource();

            properties.load(context.getResourceAsStream("WEB-INF/properties/db.properties"));

            hikariDataSource.setDriverClassName(properties.getProperty("db.driver.name"));
            hikariDataSource.setJdbcUrl(properties.getProperty("db.url"));
            hikariDataSource.setUsername(properties.getProperty("db.username"));
            hikariDataSource.setPassword(properties.getProperty("db.password"));

            return hikariDataSource;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
