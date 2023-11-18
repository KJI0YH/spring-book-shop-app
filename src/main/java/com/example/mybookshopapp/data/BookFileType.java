package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book_file_type")
@Data
public class BookFileType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;

    public static String getExtensionStringByTypeId(Integer typeId) {
        return switch (typeId) {
            case 1 -> ".pdf";
            case 2 -> ".epub";
            case 3 -> ".fb2";
            default -> "";
        };
    }
}

