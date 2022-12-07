package edu.lcaitlyn.CurrencyExchanger;

import com.zaxxer.hikari.HikariDataSource;
import edu.lcaitlyn.CurrencyExchanger.repositories.CurrencyRepository;

public class Program {
    private static HikariDataSource makeHikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        hikariDataSource.setUsername("postgres");
        hikariDataSource.setPassword("");
        return hikariDataSource;
    }

    public static void main(String[] args) {
        CurrencyRepository currencyRepository = new CurrencyRepository(makeHikariDataSource());

        System.out.println(currencyRepository.findById(1L));
    }
}
