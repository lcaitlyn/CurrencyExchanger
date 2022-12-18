package edu.lcaitlyn.CurrencyExchanger.models;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@JsonPropertyOrder({"id", "code", "fullName", "sign"})
public class Currency {
    private Long id;
    @NonNull
    private String code;
    @NonNull
    private String fullName;
    @NonNull
    private String sign;

    //     {"id": 0, "name": "Euro", "code": "EUR", "sign": "€"}
    @Override
    public String toString() {
        return "{\"id\": " + id + ", " +
                "\"name\": " + "\"" + fullName + "\", " +
                "\"code\": " + "\"" + code + "\", " +
                "\"sign\": " + "\"" + sign + "\"}";
    }
}

