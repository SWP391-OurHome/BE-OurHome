package com.javaweb.controller;


import com.javaweb.model.FavouriteListingDTO;
import com.javaweb.service.FavouriteListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/favourites")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class FavouriteListingController {


    @Autowired
    private FavouriteListingService favouriteListingService;


    // API POST để thêm bài đăng vào favourite list
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToFavourite(@RequestBody(required = false) FavouriteListingDTO request) {
        try {
            boolean success = favouriteListingService.addToFavourite(request);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("success", true, "message", "Added successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("success", false, "message", "Failed to add to favourites"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Server error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Map<String, Object>> removeFromFavourite(@RequestBody(required = false)  FavouriteListingDTO request) {
        try {
            boolean success = favouriteListingService.removeFromFavourite(request);
            if (success) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("success", true, "message", "Removed successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("success", false, "message", "Failed to remove from favourites"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Server error: " + e.getMessage()));
        }
    }


    // API GET để xem danh sách favourite list
    @GetMapping("/list")
    public ResponseEntity<List<FavouriteListingDTO>> getFavouriteList(@RequestParam Integer userId) {
        List<FavouriteListingDTO> response = favouriteListingService.getFavouriteList(userId);
        if (response == null || response.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(response);
    }
}

