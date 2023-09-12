package com.example.employeeservice.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    private static final DateFormat birthdayDateFormat = new SimpleDateFormat("yyyy-mm-dd");
    public static String toFormattedBirthdayDate(Date date) {
        return birthdayDateFormat.format(date);
    }
}
