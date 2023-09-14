package com.example.employeeservice.services;

import lombok.SneakyThrows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    private static final DateFormat birthdayDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String toFormattedBirthdayDate(Date date) {
        return birthdayDateFormat.format(date);
    }

    @SneakyThrows
    public static Date toDate(String date) {
        return birthdayDateFormat.parse(date);
    }
}
