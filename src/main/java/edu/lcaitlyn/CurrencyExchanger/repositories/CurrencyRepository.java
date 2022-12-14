package edu.lcaitlyn.CurrencyExchanger.repositories;

import edu.lcaitlyn.CurrencyExchanger.models.Currency;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository implements CrudRepository<Currency> {
    private final DataSource dataSource;

    public CurrencyRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Currency findById(Long id) {
        final String query = "SELECT * FROM currencyexchanger.currencies WHERE id=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return new Currency(
                        resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("fullname"),
                        resultSet.getString("sign").charAt(0));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Currency findByName(String name) {
        final String query = "SELECT * FROM currencyexchanger.currencies WHERE code=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, name);

            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return new Currency(
                        resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("fullname"),
                        resultSet.getString("sign").charAt(0));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Currency> findAll() {
        final String query = "SELECT * FROM currencyexchanger.currencies";

        List<Currency> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

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

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Currency entity) {
        final String query = "INSERT INTO currencyexchanger.currencies (code, fullname, sign) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

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
        final String query = "UPDATE currencyexchanger.currencies SET code=?, fullname=?, sign=? WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign().toString());
            statement.setLong(4, entity.getId());

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        final String query = "DELETE FROM currencyexchanger.currencies WHERE id=" + id;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
