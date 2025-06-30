package com.javaweb.service;

import com.javaweb.model.ChangPasswordRequest;
import com.javaweb.model.LoginRequest;
import com.javaweb.model.AuthResponse;
import com.javaweb.model.UserDTO;

public interface UserService {
    public AuthResponse login(LoginRequest request);
    public AuthResponse registerUser(UserDTO user);
    public boolean existsByPhoneNumber(String phoneNumber);
    public boolean changePassword(ChangPasswordRequest request);
}
