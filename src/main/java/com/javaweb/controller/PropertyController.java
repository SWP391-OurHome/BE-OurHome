package com.javaweb.controller;

import com.javaweb.model.PropertyDTO;
import com.javaweb.repository.impl.PropertyRepository;
import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.entity.PropertyImage;
import com.javaweb.service.PropertyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.javaweb.repository.impl.PropertyImageRepository;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/listing")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyImageRepository propertyImageRepository;

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        List<PropertyDTO> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> getById(@PathVariable Integer id) {
        Optional<PropertyEntity> optional = propertyRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        PropertyEntity property = optional.get();
        PropertyDTO dto = new PropertyDTO();
        dto.setPropertyID(property.getId());
        dto.setAddressLine1(property.getAddressLine1());
        dto.setAddressLine2(property.getAddressLine2());
        dto.setRegion(property.getRegion());
        dto.setCity(property.getCity());
        dto.setArea(property.getArea());
        dto.setInterior(property.getInterior());
        dto.setPropertyType(property.getPropertyType());
        dto.setNumBedroom(property.getNumBedroom());
        dto.setNumCompares(property.getNumCompares());
        dto.setNumBathroom(property.getNumBathroom());
        dto.setFloor(property.getFloor());
        dto.setPrivatePool(property.getPrivatePool());
        dto.setLandType(property.getLandType());
        dto.setLegalStatus(property.getLegalStatus());
        dto.setImgURL(property.getImgURL());
        dto.setPurpose(property.getPurpose());
        dto.setPrice(property.getPrice());

        // Thêm danh sách image URLs từ bảng property_images
        List<String> imageUrls = propertyImageRepository.findByProperty(property)
                .stream()
                .map(PropertyImage::getImageUrl)
                .collect(Collectors.toList());
        dto.setImages(imageUrls);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{userID}")
    public ResponseEntity<Boolean> create(@PathVariable Integer userID, HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        boolean success = propertyService.createProperty(userID, multipartRequest);
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/{id}/{userID}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id, @PathVariable Integer userID) {
        try {
            boolean success = propertyService.deleteProperty(id, userID);
            if (success) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PropertyDTO>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minArea,
            @RequestParam(required = false) Double maxArea,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) Integer bathrooms
    ) {
        List<PropertyDTO> properties = propertyService.search(
                city, propertyType, minPrice, maxPrice, minArea, maxArea, bedrooms, bathrooms);
        return ResponseEntity.ok(properties);
    }

}
