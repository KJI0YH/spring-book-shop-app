package com.example.mybookshopapp.dto;

public class Author implements Comparable<Author> {
    private Integer id;
    private String name;
    private String surname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public int compareTo(Author o) {
        int result = surname.compareTo(o.getSurname());
        if (result == 0)
            return name.compareTo(o.getName());
        return result;
    }
}
