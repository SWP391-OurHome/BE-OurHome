package com.javaweb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

public class UserDTO {


        private String firstName;


        private String lastName;

        private String email;

        private String phone;

        private String password;

//        private Date birthday;



//        private String role;


//        private Date createdArt;

        public UserDTO() {

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

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPhone() {
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }
//        public Date getCreatedArt() {
//                return createdArt;
//        }
//
//        public void setCreatedArt(Date createdArt) {
//                this.createdArt = createdArt;
//        }

//        public String getRole() {
//                return role;
//        }

//        public void setRole(String role) {
//                this.role = role;
//        }
//

//
//        public Date getBirthday() {
//                return birthday;
//        }
//
//        public void setBirthday(Date birthday) {
//                this.birthday = birthday;
//        }


        @Override
        public String toString() {
                return "UserDTO{" +
                        "firstName='" + firstName + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", email='" + email + '\'' +
                        ", phone='" + phone + '\'' +
                        ", password='" + password + '\'' +
                        '}';
        }
}
