package edu.lcaitlyn.CurrencyExchanger.repositories;

import edu.lcaitlyn.CurrencyExchanger.exceptions.CurrencyNotFoundException;
import edu.lcaitlyn.CurrencyExchanger.models.Currency;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository implements CrudRepository<Currency> {
    private DataSource dataSource;

    public CurrencyRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Currency findById(Long id) {
        Currency currency = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM currencyexchanger.currencies WHERE id=?");) {

            statement.setLong(1, id);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                currency = new Currency(
                        resultSet.getString("code"),
                        resultSet.getString("fullname"),
                        resultSet.getString("sign").charAt(0));
                currency.setId(resultSet.getLong("id"));
            }
            resultSet.close();
            return currency;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency findByName(String name) {
        Currency currency = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM currencyexchanger.currencies WHERE code=?");) {

            statement.setString(1, name);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                currency = new Currency(
                        resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("fullname"),
                        resultSet.getString("sign").charAt(0));
            }
            resultSet.close();
            return currency;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Currency> findAll() {
        List<Currency> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM currencyexchanger.currencies");) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Currency currency = new Currency(
                        resultSet.getString("code"),
                        resultSet.getString("fullname"),
                        resultSet.getString("sign").charAt(0));
                currency.setId(resultSet.getLong("id"));
                list.add(currency);
            }
            resultSet.close();
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Currency entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO currencyexchanger.currencies (code, fullname, sign) VALUES (?, ?, ?)")) {

            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign().toString());
            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(Currency entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE currencyexchanger.currencies SET code=?, fullname=?, sign=? WHERE id=?")) {

            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign().toString());
            statement.execute();

            entity.setId(statement.getGeneratedKeys().getLong("id"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM currencyexchanger.currencies WHERE id=" + id)) {

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM currencyexchanger.currencies WHERE code=?")) {

            statement.setString(1, name);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
