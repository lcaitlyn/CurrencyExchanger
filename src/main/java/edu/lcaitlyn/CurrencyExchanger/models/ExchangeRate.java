package edu.lcaitlyn.CurrencyExchanger.models;

public class ExchangeRate {
    private Long id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private Float rate;

    public ExchangeRate(Long id, Currency baseCurrency, Currency targetCurrency, Float rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public ExchangeRate(Currency baseCurrency, Currency targetCurrency, Float rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

//    {
//        "id": 0,
//            "baseCurrency": {
//        "id": 0,
//                "name": "United States dollar",
//                "code": "USD",
//                "sign": "$"
//    },
//        "targetCurrency": {
//        "id": 1,
//                "name": "Euro",
//                "code": "EUR",
//                "sign": "€"
//    },
//        "rate": 0.99
//    }
    @Override
    public String toString() {
        return String.format("{\n" +
                "\"id\": %l,\n" +
                "\"baseCurrency\": %s,\n" +
                "\"targetCurrency\": %s,\n" +
                "\"rate\": %f\n" +
                "}", id, baseCurrency, targetCurrency, rate);
    }
}
