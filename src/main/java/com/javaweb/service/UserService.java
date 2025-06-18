package com.javaweb.service;

import com.javaweb.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    public AuthResponse login(AuthRequest request);
    public AuthResponse registerUser(UserDTO user);
    public boolean existsByPhoneNumber(String phoneNumber);
    public boolean changePassword(ChangPasswordRequest request);
    public boolean resetPassword(AuthRequest request);
    public UserDTO getInformation(Integer ID);
    public GoogleLoginResponse getInformation(String email);
    public boolean updateInformation(Integer ID,UserDTO user);
    public String updateAvatar(Integer id, MultipartFile file)throws IOException;
}
