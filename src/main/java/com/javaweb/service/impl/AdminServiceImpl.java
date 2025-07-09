package com.javaweb.service.impl;


import com.javaweb.model.UserDTO;
import com.javaweb.repository.entity.RoleEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.RoleRepositoryImpl;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepositoryImpl userRepo;

    @Autowired
    private RoleRepositoryImpl roleRepo;

    //User Manager
    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepo.findAll();
        return users.stream().map(user -> {
            UserDTO dto = new UserDTO();
            dto.setUserID(user.getUserId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setBirthday(user.getBirthday());
            dto.setRoleName(user.getRole().getRoleName());
            dto.setActive(user.getIsActive());
            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    public boolean updateUserRole(Integer userId, Integer roleId) {
        Optional<UserEntity> userOpt = userRepo.findById(userId);
        Optional<RoleEntity> roleOpt = roleRepo.findById(roleId);
        if (userOpt.isPresent() && roleOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setRole(roleOpt.get());
            userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean banUser(Integer userId) {
        Optional<UserEntity> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setIsActive(false);
            userRepo.save(user);
            return true;
        }
        return false;
    }
}