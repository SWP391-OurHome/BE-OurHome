package com.javaweb.controller;


import com.javaweb.model.PropertyDTO;
import com.javaweb.repository.PropertyRepository;
import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.entity.PropertyImageEnitity;
import com.javaweb.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.javaweb.repository.PropertyImageRepository;










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
    private ListingService listingService;


    @GetMapping
    public ResponseEntity<List<PropertyEntity>> getAll() {
        return ResponseEntity.ok(propertyRepository.findAll(PageRequest.of(0, 6)).getContent());
    }


    @GetMapping("/search")
    public ResponseEntity<List<PropertyEntity>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minArea,
            @RequestParam(required = false) Double maxArea,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) Integer bathrooms
    ) {
        List<PropertyEntity> properties = listingService.search(
                city, propertyType, minPrice, maxPrice, minArea, maxArea, bedrooms, bathrooms);
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
        dto.setPropertyID(property.getPropertyId());
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
                .map(PropertyImageEnitity::getImageUrl)
                .collect(Collectors.toList());
        dto.setImages(imageUrls);


        return ResponseEntity.ok(dto);
    }


    @PostMapping
    public ResponseEntity<PropertyEntity> create(@RequestBody PropertyEntity property) {
        PropertyEntity saved = propertyRepository.save(property);
        return ResponseEntity.ok(saved);
    }
}
