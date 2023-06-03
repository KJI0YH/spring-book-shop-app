package com.example.mybookshopapp.dto;

public class Author implements Comparable<Author> {
    private Integer id;
    private String firstName;
    private String lastName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int compareTo(Author o) {
        int result = lastName.compareTo(o.getLastName());
        if (result == 0)
            return firstName.compareTo(o.getFirstName());
        return result;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + firstName + '\'' +
                ", surname='" + lastName + '\'' +
                '}';
    }
}
