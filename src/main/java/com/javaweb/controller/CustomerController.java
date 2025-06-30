package com.javaweb.controller;

import com.javaweb.model.UserDTO;
import com.javaweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class CustomerController {
    @Autowired
    private UserService userService;

    // Lấy thông tin người dùng
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserInformation(@PathVariable Integer id) {
        try {
            UserDTO user = userService.getInformation(id);
            System.out.println(user);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false,"message", "Not found user " + id ));
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false,"message", "Error in BackEnd " + e.getMessage()
                    ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody(required = false) UserDTO dto) {
        boolean updated = userService.updateInformation(id, dto);
        if(updated){
            return ResponseEntity.ok(Map.of("success", true, "message", "User Information updated successfully."));
        }
        return  ResponseEntity.ok(Map.of("success", false, "message", "User Information not updated."));
    }

    @PutMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAvatar(
            @PathVariable Integer id,
            @RequestParam("avatar") MultipartFile avatarFile) {
        try {
            // userService.updateAvatar trả về đường dẫn mới của ảnh, ví dụ "/uploads/xxx.png"
            String avatarUrl = userService.updateAvatar(id, avatarFile);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "avatarUrl", avatarUrl,
                    "message", "Avatar updated successfully."
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", "Không thể cập nhật avatar: " + e.getMessage()
                    ));
        }
    }


}
