package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.models.auth.In;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "balance_transaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    @JsonIgnore
    private LocalDateTime time;

    @JsonGetter("time")
    public Long getTimeJson(){
        return time.toEpochSecond(ZoneOffset.UTC);
    }

    @JsonIgnore
    private Integer value;

    @JsonGetter("value")
    public String getValueJson(){
        int valueAbs = Math.abs(value);
        return (value < 0 ? "-" : "") + valueAbs / 100 + "." + String.format("%02d",valueAbs % 100);
    }

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnore
    private BookEntity book;
    private String description;

    @JsonIgnore
    public String getTimeValue(){
        return String.valueOf(String.format("%02d", time.getDayOfMonth())) + '.' +
                String.format("%02d", time.getMonthValue()) + '.' +
                time.getYear() + ' ' +
                String.format("%02d", time.getHour()) + ':' +
                String.format("%02d", time.getMinute()) + ':' +
                String.format("%02d", time.getSecond());
    }
}

