package edu.lcaitlyn.CurrencyExchanger.models;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class ExchangeRate {
    private Long id;
    @NonNull
    private Currency baseCurrency;
    @NonNull
    private Currency targetCurrency;
    @NonNull
    private BigDecimal rate;

    //  {
//      "id": 1,
//      "baseCurrency": {"id": 1, "name": "Euro", "code": "EUR", "sign": "€"},
//      "targetCurrency": {"id": 6, "name": "Pound Sterling", "code": "GBP", "sign": "£"},
//      "rate": 1.05
//  }
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
