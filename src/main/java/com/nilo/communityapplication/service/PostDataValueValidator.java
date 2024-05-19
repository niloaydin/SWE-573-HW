package com.nilo.communityapplication.service;

import com.nilo.communityapplication.globalExceptionHandling.WrongInputTypeException;
import com.nilo.communityapplication.model.PostDataField;
import com.nilo.communityapplication.model.PostTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostDataValueValidator {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String URL_REGEX = "^(https?|ftp):\\/\\/[A-Za-z0-9]+([\\-\\.]{1}[A-Za-z0-9]+)*\\.[A-Za-z]{2,5}(:[0-9]{1,5})?(\\/[^\\s]*)?$";

    public boolean validateFieldTypes(PostTemplate template, Map<String, String> requestData) {
        List<PostDataField> dataFields = template.getDatafields();

        for (PostDataField field : dataFields) {
            String fieldType = field.getType();

            if (fieldType.equals("String") || fieldType.equals("Boolean")) {
                continue; // Skip validation for String and Boolean fields
            } else if (fieldType.equals("Integer")) {
                if (!PostDataValueValidator.isNumber(requestData.get(field.getName()))) {
                    return false; // Validation failed, return false immediately
                }
            } else if (fieldType.equals("Date")) {
                if (!PostDataValueValidator.isValidDate(requestData.get(field.getName()))) {
                    return false;
                }
            } else if (fieldType.equals("URL")) {
                if (!PostDataValueValidator.isValidUrl(requestData.get(field.getName()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNumber(String value) {

        try {
            Integer number = Integer.valueOf(value);
            return true;

        } catch (NumberFormatException e) {
            new WrongInputTypeException("Invalid integer input");

        }
        return false;

    }

    public static boolean isValidDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false); // Disable lenient parsing

        try {
            Date date = sdf.parse(dateString);
            return true; // Date is valid
        } catch (ParseException e) {
            return false; // Date is not valid
        }


    }


    public static boolean isValidUrl(String urlString) {
        Pattern pattern = Pattern.compile(URL_REGEX);
        Matcher matcher = pattern.matcher(urlString);
        return matcher.matches();
    }
}


