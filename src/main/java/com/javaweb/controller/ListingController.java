package com.javaweb.controller;

import com.javaweb.model.ListingDTO;
import com.javaweb.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})

public class ListingController {

    @Autowired
    private ListingService listingService;

    @GetMapping("/{propertyId}")
    public ResponseEntity<ListingDTO> getListingByPropertyId(@PathVariable Long propertyId) {
        return listingService.getListingByPropertyId(propertyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
