package com.javaweb.controller;

import com.javaweb.model.ListingDTO;
import com.javaweb.model.PropertyDTO;
import com.javaweb.model.UserDTO;
import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.service.ListingService;
import com.javaweb.service.PropertyService;
import com.javaweb.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seller")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class SellerController {

    @Autowired
    private  SellerService sellerService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ListingService listingService;
    //Lay danh sach cac agent
    @GetMapping
    public List<UserDTO> getAllSellers() {

        return sellerService.getAllSellers();
    }

    // Get list of listings by userId
//    @GetMapping("/{userId}")
//    public ResponseEntity<Map<String, Object>> getListingsByUserId(@PathVariable Integer userId) {
//        //khai bao 1 map <String, Object> tren la response
//
//        //Tim property theo userID ==> Tra ve PropertyList ==>Pusting list do vao map
//
//        // SAu do lay 1 list la cac listing map vs propertyList
//
//        // Cuoi cung ve return ResponseEntity.ok(response);
//        //Doi lau
//        List<ListingDTO> listings = sellerService.findByUserId(userId);
//        Map<String, Object> response = new HashMap<>();
//        response.put("listings", listings);
//
//        // Fetch and include PropertyDTOs using PropertyService with ListingDTO
//        List<PropertyDTO> properties = propertyService.mapPropertiesFromListings(listings);
//        response.put("properties", properties);
//
//
//    }


    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getListingsByUserId(@PathVariable Integer userId) {
        Map<String, Object> response = new HashMap<>();

        // Lấy danh sách bất động sản
        List<PropertyDTO> propertyList = propertyService.findPropertiesByUserId(userId);
        System.out.println("Số lượng PropertyDTO trước khi lấy listing: " + propertyList.size());
        if (propertyList.isEmpty()) {
            System.out.println("Danh sách PropertyDTO rỗng cho userId: " + userId);
        } else {
            propertyList.forEach(p -> System.out.println("PropertyID: " + p.getPropertyID() + ", UserID: " +userId));
        }

        // Lấy danh sách listing
        List<ListingDTO> listingList = listingService.getListingsByProperties(propertyList);
        System.out.println("Số lượng ListingDTO sau khi ánh xạ: " + listingList.size());

        response.put("properties", propertyList);
        response.put("listings", listingList);

        return ResponseEntity.ok(response);
    }

    //Edit Listing

}
