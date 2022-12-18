package edu.lcaitlyn.CurrencyExchanger.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Utils {
    public static String getStringFromPartName(HttpServletRequest request, String partName) {
        try {
            Part part = request.getPart(partName);
            return getStringFormInputStream(part.getInputStream());
        } catch (Exception e) {
            return null;
        }
    }

    public static String getStringFormInputStream(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
    }

    public static boolean isStringDouble(String d) {
        try {
            Double.parseDouble(d);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNotValidCurrenciesArgs(String code, String name, String sign) {
        return  (code == null || name == null || sign == null
                || code.isEmpty() || name.isEmpty() || sign.isEmpty()
                || code.length() != 3 || name.length() > 100 || sign.length() > 5);
    }

    public static boolean isNotValidExchangeArgs(String base, String target, String rate) {
        return (base == null || target == null || rate == null
                || base.isEmpty() || target.isEmpty() || rate.isEmpty()
                || base.length() != 3 || target.length() != 3);
    }
}
