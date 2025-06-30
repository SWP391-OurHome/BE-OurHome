package com.javaweb.service.impl;

import com.javaweb.model.ChangPasswordRequest;
import com.javaweb.model.LoginRequest;
import com.javaweb.model.AuthResponse;
import com.javaweb.model.UserDTO;
import com.javaweb.repository.entity.RoleEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepositoryImpl userRepo;


    @Override
    public AuthResponse login(LoginRequest request) {
        Optional<UserEntity> userOpt = userRepo.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            String passDb = user.getPassword();
            String passFe = request.getPassword();

            if (passDb != null && passDb.equals(passFe)) {
                return new AuthResponse(user.getUserId(),true, "Login success", user.getRole().getRoleName()
                        ,user.getImgPath(),user.getEmail(), user.getFirstName() +" "+user.getLastName(),user.getBirthday());
            } else {
                return new AuthResponse(false, "Invalid credentials");
            }
        }

        return new AuthResponse(false, "User not found");
    }

    @Override
    public AuthResponse registerUser(UserDTO request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "Email already exists");
        }
        if (userRepo.existsByPhone(request.getPhone())) {
            return new AuthResponse(false, "Phone already exists");
        }

        RoleEntity defaultRole = new RoleEntity();
        defaultRole.setRoleId(2);

        UserEntity user = new UserEntity(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getPassword(),
                defaultRole
        );

        userRepo.save(user);
        return new AuthResponse(true, "Register success", "Customer");
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        String covertPhone =convertInternationalToLocal(phoneNumber);
        return userRepo.existsByPhone(covertPhone);
    }


    public String convertInternationalToLocal(String phoneNumber) {
        if (phoneNumber == null) return null;


        if (phoneNumber.startsWith("+84")) {
            return "0" + phoneNumber.substring(3);
        }


        return phoneNumber;
    }

    @Override
    public boolean changePassword(ChangPasswordRequest request) {

        Optional<UserEntity> optionalUser = userRepo.findByEmail(request.getEmail());

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            if (request.getCurrentPassword().equals(user.getPassword())) {
                user.setPassword(request.getNewPassword());
                userRepo.save(user);
                return true;
            }
        }

        return false;
    }

}
