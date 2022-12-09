package edu.lcaitlyn.CurrencyExchanger.repositories;

import edu.lcaitlyn.CurrencyExchanger.exceptions.CurrencyNotFoundException;
import edu.lcaitlyn.CurrencyExchanger.exceptions.ExchangeRateNotFoundException;
import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import edu.lcaitlyn.CurrencyExchanger.models.ExchangeRate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesRepository implements CrudRepository<ExchangeRate> {
    private DataSource dataSource;
    private CurrencyRepository currencyRepository;

    public ExchangeRatesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        currencyRepository = new CurrencyRepository(dataSource);
    }

    @Override
    public ExchangeRate findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM currencyexchanger.exchangerates WHERE id=" + id);) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            if (!resultSet.next())
                throw new ExchangeRateNotFoundException();

            return new ExchangeRate(
                    resultSet.getLong("id"),
                    currencyRepository.findById(resultSet.getLong("BaseCurrencyId")),
                    currencyRepository.findById(resultSet.getLong("TargetCurrencyId")),
                    resultSet.getFloat("rate")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM currencyexchanger.exchangerates");) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                list.add(new ExchangeRate(
                        resultSet.getLong("id"),
                        currencyRepository.findById(resultSet.getLong("BaseCurrencyId")),
                        currencyRepository.findById(resultSet.getLong("TargetCurrencyId")),
                        resultSet.getFloat("rate")
                ));
            }
            resultSet.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isExchangeRateExist(ExchangeRate entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM currencyexchanger.exchangerates WHERE basecurrencyid=? and targetcurrencyid=?")) {

            statement.setLong(1, entity.getBaseCurrency().getId());
            statement.setLong(2, entity.getTargetCurrency().getId());

            statement.execute();

            return statement.getResultSet().next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(ExchangeRate entity) {
        if (isExchangeRateExist(entity)) return;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO currencyexchanger.exchangerates (basecurrencyid, targetcurrencyid, rate) VALUES (?, ?, ?)")) {

            statement.setLong(1, entity.getId());
            statement.setLong(2, entity.getBaseCurrency().getId());
            statement.setLong(3, entity.getTargetCurrency().getId());

            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(ExchangeRate entity) {
        if (!isExchangeRateExist(entity)) {
            save(entity);
            return;
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE currencyexchanger.exchangerates SET basecurrencyid=?, targetcurrencyid=? WHERE id=?")) {

            statement.setString(1, entity.getBaseCurrency().getCode());
            statement.setString(2, entity.getTargetCurrency().getCode());
            statement.setLong(3, entity.getId());

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM currencyexchanger.exchangerates WHERE id=" + id)) {

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
