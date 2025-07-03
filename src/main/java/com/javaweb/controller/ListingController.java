package com.javaweb.controller;

import com.javaweb.model.ListingDTO;
import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.service.ListingService;
import com.javaweb.service.PropertyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})

public class ListingController {

    @Autowired
    private ListingService listingService;

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/{propertyId}")
    public ResponseEntity<ListingDTO> getListingByPropertyId(@PathVariable Integer propertyId) {
        return listingService.getListingByPropertyId(propertyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{userID}")
    public boolean updateProperty(@PathVariable Integer userID, HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        return propertyService.updateProperty(userID, multipartRequest);
    }



}
