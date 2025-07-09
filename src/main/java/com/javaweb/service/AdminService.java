package com.javaweb.service;

import com.javaweb.model.UserDTO;

import java.util.List;

public interface AdminService {
    boolean updateUserRole(Integer userId, Integer roleId);
    boolean banUser(Integer userId);
    public List<UserDTO> getAllUsers();
}