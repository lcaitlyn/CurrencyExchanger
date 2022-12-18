package edu.lcaitlyn.CurrencyExchanger.repositories;

import edu.lcaitlyn.CurrencyExchanger.models.ExchangeRate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesRepository implements CrudRepository<ExchangeRate> {
    private final DataSource dataSource;
    private final CurrencyRepository currencyRepository;

    public ExchangeRatesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        currencyRepository = new CurrencyRepository(dataSource);
    }

    private ExchangeRate createNewExchangeRate(ResultSet resultSet) {
        try {
            return new ExchangeRate(
                    resultSet.getLong("id"),
                    currencyRepository.findById(resultSet.getLong("BaseCurrencyId")).get(),
                    currencyRepository.findById(resultSet.getLong("TargetCurrencyId")).get(),
                    BigDecimal.valueOf(resultSet.getDouble("rate")));
        } catch (SQLException e) {
            return null;
        }
    }


    @Override
    public Optional<ExchangeRate> findById(Long id) {
        ExchangeRate exchangeRate = null;
        final String query = "SELECT * FROM currencyexchanger.exchangerates WHERE id=" + id;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                exchangeRate = createNewExchangeRate(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(exchangeRate);
    }

    public Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        ExchangeRate exchangeRate = null;
        final String query = "SELECT * FROM currencyexchanger.exchangerates WHERE " +
                "basecurrencyid=? AND targetcurrencyid=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1,
                    currencyRepository.findByName(baseCurrencyCode).get().getId());
            statement.setLong(2,
                    currencyRepository.findByName(targetCurrencyCode).get().getId());

            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                exchangeRate = createNewExchangeRate(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(exchangeRate);
    }

    @Override
    public List<ExchangeRate> findAll() {
        final String query = "SELECT * FROM currencyexchanger.exchangerates";

        List<ExchangeRate> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                list.add(createNewExchangeRate(resultSet));
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(ExchangeRate entity) {
        final String query = "INSERT INTO currencyexchanger.exchangerates (basecurrencyid, targetcurrencyid, rate) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, entity.getBaseCurrency().getId());
            statement.setLong(2, entity.getTargetCurrency().getId());
            statement.setBigDecimal(3, entity.getRate());

            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        updateReverseExchangeRate(entity);
    }

    @Override
    public void update(ExchangeRate entity) {
        final String query = "UPDATE currencyexchanger.exchangerates SET basecurrencyid=?, targetcurrencyid=?, rate=? WHERE id=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, entity.getBaseCurrency().getId());
            statement.setLong(2, entity.getTargetCurrency().getId());
            statement.setBigDecimal(3, entity.getRate());
            statement.setLong(4, entity.getId());

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        updateReverseExchangeRate(entity);
    }

    @Override
    public void delete(Long id) {
        final String query = "DELETE FROM currencyexchanger.exchangerates WHERE id=" + id;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateReverseExchangeRate(ExchangeRate exchangeRate) {
        Optional<ExchangeRate> reverseExchangeRate = findByCodes(
                exchangeRate.getTargetCurrency().getCode(), exchangeRate.getBaseCurrency().getCode());

        if (reverseExchangeRate.isPresent()) {
            reverseExchangeRate.get().setRate(new BigDecimal(1).divide(exchangeRate.getRate()));
            update(reverseExchangeRate.get());
        }
    }
}
