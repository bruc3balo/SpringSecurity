package com.security.spring.utils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Enumeration;
import org.apache.commons.lang3.StringUtils;
import java.util.List;

public class DataOps {

    public static List<String> filterRequestParams(HttpServletRequest request, List<String> knownParams) {
        Enumeration<String> query = request.getParameterNames();
        List<String> list = Collections.list(query);
        list.removeAll(knownParams);
        return list;
    }

    public static boolean isNumeric(String text) {
        return StringUtils.isNumeric(text);
    }


    public static String exactStringValue(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    public static BigDecimal strToBigDecimal(String value){
        try {
            return (value == null || value.trim().isEmpty() ? new BigDecimal("0") : ((new BigDecimal(value.trim()))));
        } catch (Exception ex) {
            return null;
        }
    }

    public static Integer strToInteger(String value){
        try {
            return (value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim()));
        }
        catch (Exception ex){
            return null;
        }
    }
}
