package com.javaweb.controller;

import com.javaweb.model.PropertyDTO;
import com.javaweb.repository.PropertyRepository;
import com.javaweb.repository.entity.PropertyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/listing")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping
    public ResponseEntity<List<PropertyEntity>> getAll() {
        return ResponseEntity.ok(propertyRepository.findAll());
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
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PropertyEntity> create(@RequestBody PropertyEntity property) {
        PropertyEntity saved = propertyRepository.save(property);
        return ResponseEntity.ok(saved);
    }
}
