package com.example.mybookshopapp.services;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class DateResolverService {

    public LocalDate resolve(String date){
        if (date == null){
            return null;
        }
        try {
            return DateUtils.parseDate(date, "dd.MM.yyyy", "dd-MM-yyyy", "yyyy-MM-dd").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e) {
            return null;
        }
    }
}
