package com.javaweb.controller;

import com.javaweb.model.AdminDashboardDTO;
import com.javaweb.model.TopSellingDTO;
import com.javaweb.model.UserDTO;
import com.javaweb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    //Overview
    @GetMapping("/dashboard")
    public AdminDashboardDTO getDashboard() {
        return adminService.getTotalDashboard();
    }

    @GetMapping("/top-selling")
    public List<TopSellingDTO> getTopSelling() {
        return adminService.getTopSelling();
    }
    //User Manager
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }


    @PutMapping("/update-role/{userId}")
    public ResponseEntity<?> updateUserRole(@PathVariable Integer userId, @RequestParam Integer roleId) {
        boolean success = adminService.updateUserRole(userId, roleId);
        return success ? ResponseEntity.ok("Role updated successfully")
                : ResponseEntity.badRequest().body("Failed to update role");
    }

    @PutMapping("/ban-account/{userId}")
    public ResponseEntity<?> banUser(@PathVariable Integer userId) {
        boolean success = adminService.banUser(userId);
        return success ? ResponseEntity.ok("User banned successfully")
                : ResponseEntity.badRequest().body("Failed to ban user");
    }
}