package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "contact_change_confirmation")
@Data
public class ContactChangeConfirmationEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String contact;
    private Boolean isConfirmed = false;
}
