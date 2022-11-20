package edu.lcaitlyn.CurrencyExchanger.repositories;

import edu.lcaitlyn.CurrencyExchanger.models.Currency;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class CurrencyRepositoryImpl implements CrudRepository<Currency> {
    private JdbcTemplate jdbcTemplate;

    public CurrencyRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Currency findById(Long id) {
        return jdbcTemplate.query("select * from currencyexchanger.currencies where id = ?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Currency.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Currency> findAll() {
        return jdbcTemplate.query("select * from currencyexchanger.currencies", new BeanPropertyRowMapper<>(Currency.class));
    }

    @Override
    public void save(Currency entity) {
        jdbcTemplate.update("insert into currencyexchanger.currencies (code, fullname, sign) values (?, ?, ?)",
                entity.getCode(), entity.getFullName(), entity.getSign());
    }

    @Override
    public void update(Currency entity) {
        jdbcTemplate.update("update currencyexchanger.currencies set code=?, fullname=?, sign=? where id = ?",
                entity.getCode(), entity.getFullName(), entity.getSign(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from currencyexchanger.currencies where id=?", id);
    }
}
