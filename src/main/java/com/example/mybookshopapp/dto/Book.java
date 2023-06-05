package com.example.mybookshopapp.dto;

import lombok.Builder;

@Builder
public class Book {
    private Integer id;
    private String author;
    private String title;
    private Integer price;
    private Integer discount;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPrice(){
        return price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getPriceWithDiscount(){
        if (discount == 0){
            return price;
        } else {
            return price * discount / 100;
        }
    }
}
