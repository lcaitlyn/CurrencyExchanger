package edu.lcaitlyn.CurrencyExchanger.models;

public class Currency {
    private Long id;
    private String code;
    private String fullName;
    private Character sign;

    public Currency() {
    }

    public Currency(String code, String fullName, Character sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Character getSign() {
        return sign;
    }

    public void setSign(Character sign) {
        this.sign = sign;
    }


//    {
//        "id": 0,
//        "name": "Euro",
//        "code": "EUR",
//        "sign": "€"
//    }
    @Override
    public String toString() {
        return "\n{\"id\": " + id + ",\n" +
                "\t\"name\": " + "\"" + fullName + "\",\n" +
                "\t\"code\": " + "\"" + code + "\",\n" +
                "\t\"sign\": " + "\"" + sign +
                "\"\n}";
    }

    // {"id": 0, "name": "Euro", "code": "EUR", "sign": "€"}
//    @Override
//    public String toString() {
//        return "{\"id\": " + id + ", " +
//                "\"name\": " + "\"" + fullName + "\", " +
//                "\"code\": " + "\"" + code + "\", " +
//                "\"sign\": " + "\"" + sign + "\"}";
//    }
}
