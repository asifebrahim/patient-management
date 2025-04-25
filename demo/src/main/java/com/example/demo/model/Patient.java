package com.example.demo.model;

import com.example.demo.dto.validators.CreatePatientValidationGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
public class Patient {
    @Id
    @Column(name = "Id", columnDefinition = "CHAR(36)",unique = true)
    private String id;

    @PrePersist
    public void generateUUID() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }



    @NotBlank(message= "Name is mandatory")
    @Column(name="Name")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email
    @Column(name="Email",unique = true)
    private String email;

    @NotBlank(message="Address is mandatory")
    @Column(name="Address")
    private String address;

    @NotNull(message="Date of Birth is mandatory")
    @Column(name="DateOfBirth")
    private LocalDate dateOfBirth;

    @NotNull(groups= CreatePatientValidationGroup.class,message="Registered Date is mandatory")
    @Column( name="RegisteredDate")
    private LocalDate registeredDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }
}
