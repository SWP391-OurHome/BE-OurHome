package com.javaweb.service.impl;

import com.javaweb.model.*;
import com.javaweb.repository.entity.RoleEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepositoryImpl userRepo;

    @Value("${app.upload.dir:${user.home}/eyespire/uploads}")
    private String uploadDir;

    @Override
    public AuthResponse login(AuthRequest request) {
        Optional<UserEntity> userOpt = userRepo.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            String passDb = user.getPassword();
            String passFe = request.getPassword();

            if (passDb != null && passDb.equals(passFe)) {
                return new AuthResponse(user.getUserId(),true, "Login success", user.getRole().getRoleName()
                        ,user.getImgPath(),user.getEmail(), user.getFirstName() ,user.getLastName(),user.getBirthday());
            } else {
                return new AuthResponse(false, "Invalid credentials");
            }
        }

        return new AuthResponse(false, "User not found");
    }

    @Override
    public AuthResponse registerUser(UserDTO request) {
        System.out.println("Received UserDTO: " + request);
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
        user.setBirthday(request.getBirthday()); // Log this value
        System.out.println("Setting birthday: " + request.getBirthday());
        user.setImgPath(" "); // Set default avatar path

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

    @Override
    public boolean resetPassword(AuthRequest request) {
        Optional<UserEntity> optionalUser = Optional.empty();
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            optionalUser = userRepo.findByEmail(request.getEmail().trim());
        } else if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            optionalUser = userRepo.findByPhone(request.getPhone().trim());
        }
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setPassword(request.getPassword());
            userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public UserDTO getInformation(Integer ID) {
        Optional<UserEntity> optionalUser = userRepo.findByUserId(ID);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            UserDTO dto = new UserDTO();
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setBirthday(user.getBirthday());
            dto.setPassword(user.getPassword());
            dto.setPhone(user.getPhone());
            dto.setImgPath(user.getImgPath());
            return dto;
        }
        return null;
    }

    @Override
    public GoogleLoginResponse getInformation(String email) {
        Optional<UserEntity> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            GoogleLoginResponse response = new GoogleLoginResponse();
            response.setRole(user.getRole().getRoleName());
            response.setSuccess(true);
            response.setUserId(user.getUserId());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setEmail(user.getEmail());
            response.setBirthday(user.getBirthday());
            response.setPhone(user.getPhone());
            response.setPicture(user.getImgPath());
            return response;
        }
        return null;
    }


    @Override
    public boolean updateInformation(Integer ID, UserDTO user) {
        Optional<UserEntity> optionalUser = userRepo.findByUserId(ID);
        if (optionalUser.isPresent()) {
            UserEntity existingUser = optionalUser.get();

            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setBirthday(user.getBirthday());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setImgPath(user.getImgPath());

            userRepo.save(existingUser);
            return true;
        }
        return false;
    }

    @Override
    public String updateAvatar(Integer id, MultipartFile file)throws IOException {

        Optional<UserEntity> optionalUser = userRepo.findByUserId(id);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            // Tạo thư mục nếu chưa có
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Lấy tên gốc và tách extension bằng if thay vì ternary
            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.'));
            }
            String fileName = UUID.randomUUID().toString() + ext;

            // Ghi file ra disk
            Path target = Paths.get(uploadDir).resolve(fileName);
            Files.write(target, file.getBytes());



            // Lưu đường dẫn vào entity
            String avatarPath = "http://localhost:8082/uploads/" + fileName;
            user.setImgPath(avatarPath);
            userRepo.save(user);

            // Trả về URL đầy đủ
            return avatarPath;
        }
        return null;


    }


    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf);
    }
}
