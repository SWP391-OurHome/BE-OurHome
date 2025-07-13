package com.javaweb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Date;

public class UserDTO {

        private Integer userID;
        private String firstName;


        private String lastName;

        private String email;

        private String phone;

        private String password;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate birthday;

        private String ImgPath;
        private String roleName;
        private Boolean isActive;
        private LocalDate createDate;
//        private Date createdArt;

        public LocalDate getCreateDate() {
                return createDate;
        }

        public void setCreateDate(LocalDate createDate) {
                this.createDate = createDate;
        }

        public UserDTO() {

        }

        public Integer getUserID() {
                return userID;
        }

        public void setUserID(Integer userID) {
                this.userID = userID;
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

        public LocalDate getBirthday() {
                return birthday;
        }

        public void setBirthday(LocalDate birthday) {
                this.birthday = birthday;
        }

        public String getImgPath() {
                return ImgPath;
        }

        public void setImgPath(String imgPath) {
                ImgPath = imgPath;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getRoleName() {
                return roleName;
        }

        public void setRoleName(String roleName) {
                this.roleName = roleName;
        }

        public Boolean getActive() {
                return isActive;
        }

        public void setActive(Boolean active) {
                isActive = active;
        }

        @Override
        public String toString() {
                return "UserDTO{" +
                        "firstName='" + firstName + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", email='" + email + '\'' +
                        ", phone='" + phone + '\'' +
                        ", birthday=" + birthday +
                        ", ImgPath='" + ImgPath + '\'' +
                        '}';
        }
}
