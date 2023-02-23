/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.kitchensink.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
//Two separate unique contraints email and username - ensure no duplicates of email or username in database
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "username")
})
public class Member implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message="Name must not be empty")
    @Size(min = 1, max = 25, message="Name must be within 25 characters")
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String name;

    @NotNull(message="Email must not be empty")
    @NotEmpty(message="Email must not be empty")
    @Email
    private String email;

    @NotNull(message="Phone Number must not be empty")
    //Customized message with the assumption that this web app is launched for targeted users.
    @Size(min = 8, max = 10, message = "Phone number must be between 8 to 10")//Changed the conditions to minimum 8 numbers, and maximum of 10 numbers
    @Digits(fraction = 0, integer = 10, message = "Invalid phone number(exlcude symbols)")//Integer=10(maximum 10 digits), fraction=0(only integers allowed)
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Size(min = 6, max = 10)
    private String username;

    @NotNull(message="Password field must not be empty")
    //Password constraints having at least one uppercase letter, one special character, and one number. (variation for security)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$&*-_])(?=.*[0-9]).{10,}$", message = "Password requirements: min 10 characters, an uppercase letter, special character, and number")
    private String password;

    //getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

     public String getUsername(){
         return username;
     }

     public void setUsername(String username){
         this.username = username;
     }

     public String getPassword() {
         return password;
     }

     public void setPassword(String password){
         this.password = password;
     }    
}

