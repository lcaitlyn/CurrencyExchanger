package edu.lcaitlyn.CurrencyExchanger.servlets;

import com.zaxxer.hikari.HikariDataSource;
import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepositoryImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {


    public ContextListener() {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        CurrencyRepositoryImpl currencyRepository = new CurrencyRepositoryImpl(makeHikariDataSource(context));

        context.setAttribute("currencyRepository", currencyRepository);
        System.out.println("init context");
        currencyRepository = (CurrencyRepositoryImpl) context.getAttribute("currencyRepository");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("destroy context");
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
