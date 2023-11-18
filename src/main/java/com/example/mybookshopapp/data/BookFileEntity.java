package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "book_file")
@Data
public class BookFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String hash;
    private Integer typeId;
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @ToString.Exclude
    private BookEntity book;

    public String getBookFileExtensionString(){
        return BookFileType.getExtensionStringByTypeId(typeId);
    }
}
