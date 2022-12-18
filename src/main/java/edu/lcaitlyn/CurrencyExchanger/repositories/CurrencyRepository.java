package edu.lcaitlyn.CurrencyExchanger.repositories;

import edu.lcaitlyn.CurrencyExchanger.models.Currency;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepository implements CrudRepository<Currency> {
    private final DataSource dataSource;

    public CurrencyRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Currency createNewCurrency(ResultSet resultSet) {
        try {
            return new Currency(
                    resultSet.getLong("id"),
                    resultSet.getString("code"),
                    resultSet.getString("fullname"),
                    resultSet.getString("sign"));
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Optional<Currency> findById(Long id) {
        final String query = "SELECT * FROM currencyexchanger.currencies WHERE id=" + id;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            Currency currency = null;

            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                currency = createNewCurrency(resultSet);
            }

            return Optional.ofNullable(currency);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Currency> findByName(String name) {
        final String query = "SELECT * FROM currencyexchanger.currencies WHERE code='" + name + "'";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            Currency currency = null;

            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                currency = createNewCurrency(resultSet);
            }

            return Optional.ofNullable(currency);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
                list.add(createNewCurrency(resultSet));
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
            statement.setString(3, entity.getSign());
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
