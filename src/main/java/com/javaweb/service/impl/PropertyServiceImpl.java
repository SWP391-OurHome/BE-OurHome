package com.javaweb.service.impl;

import com.javaweb.model.PropertyDTO;
import com.javaweb.repository.impl.ListingRepository;
import com.javaweb.repository.impl.PropertyImageRepository;
import com.javaweb.repository.impl.PropertyRepository;
import com.javaweb.repository.entity.ListingEntity;
import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.entity.PropertyImage;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private ListingRepository listRepo;

    @Autowired
    private PropertyImageRepository propertyIMGRepo;

    @Autowired
    private UserRepositoryImpl userRepo;

    @Value("${app.upload.dir:D:/SWP391-Probjects/BE-DreamHouse/upload}")
    private String uploadDir;

    // Nếu FormatUtils cần thiết, hãy tiêm phụ thuộc
    // @Autowired
    // private FormatUtils formatUtils;

    @Override
    @Transactional(readOnly = true)
    public List<PropertyDTO> getAllProperties() {
        List<PropertyEntity> propertyEntities = propertyRepo.findAll();
        return propertyEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyDTO> findPropertiesByUserId(Integer userId) {
        List<PropertyEntity> propertyEntities = propertyRepo.findByUserId(userId);
        return propertyEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PropertyDTO convertToDTO(PropertyEntity entity) {
        PropertyDTO dto = new PropertyDTO();
        dto.setPropertyID(entity.getId());
        dto.setAddressLine1(entity.getAddressLine1());
        dto.setAddressLine2(entity.getAddressLine2());
        dto.setRegion(entity.getRegion());
        dto.setCity(entity.getCity());
        dto.setArea(entity.getArea());
        dto.setInterior(entity.getInterior());
        dto.setPropertyType(entity.getPropertyType());
        dto.setNumBedroom(entity.getNumBedroom());
        dto.setNumCompares(entity.getNumCompares());
        dto.setNumBathroom(entity.getNumBathroom());
        dto.setFloor(entity.getFloor());
        dto.setPrivatePool(entity.getPrivatePool());
        dto.setLandType(entity.getLandType());
        dto.setLegalStatus(entity.getLegalStatus());
        dto.setImgURL(entity.getImgURL());
        dto.setPurpose(entity.getPurpose());
        dto.setPrice(entity.getPrice());
        // Convert PropertyImage list to List<String>
        if (entity.getImages() != null) {
            dto.setImages(entity.getImages().stream()
                    .map(image -> image.getImageUrl()) // Assuming PropertyImage has getImageUrl()
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    @Override
    @Transactional
    public boolean updateProperty(Integer userID, MultipartHttpServletRequest request) {
        // Retrieve existing entities
        PropertyEntity property = propertyRepo.findById(userID)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        ListingEntity listing = listRepo.findByPropertyId(userID)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        // Update property and listing details
        updatePropertyDetails(property, request);
        updateListingDetails(listing, request);

        // Update images (existing URLs and new uploads)
        List<PropertyImage> updatedImages = updateImages(property, request);

        // Save changes
        propertyIMGRepo.saveAll(updatedImages);
        propertyRepo.save(property);
        listRepo.save(listing);
        return true;
    }

    @Override
    @Transactional
    public boolean createProperty(Integer userId, MultipartHttpServletRequest request) {
        // Tạo property
        PropertyEntity property = new PropertyEntity();
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        property.setUser(user);

        updatePropertyDetails(property, request);

        // Lưu property trước để có ID
        property = propertyRepo.save(property);

        // Gán ảnh và URL sau khi property có ID
        List<PropertyImage> images = updateImages(property, request);
        if (!images.isEmpty()) {
            property.setImgURL(images.get(0).getImageUrl());
        }
        property.setImages(images);
        propertyIMGRepo.saveAll(images);

        // Tạo listing sau khi có property
        ListingEntity listing = new ListingEntity();
        updateListingDetails(listing, request);
        listing.setProperty(property);
        listRepo.save(listing);

        return true;
    }


    private void updatePropertyDetails(PropertyEntity property, MultipartHttpServletRequest request) {
        if (request.getParameter("addressLine1") != null)
            property.setAddressLine1(request.getParameter("addressLine1"));
        if (request.getParameter("addressLine2") != null)
            property.setAddressLine2(request.getParameter("addressLine2"));
        if (request.getParameter("region") != null) property.setRegion(request.getParameter("region"));
        if (request.getParameter("city") != null) property.setCity(request.getParameter("city"));
        if (request.getParameter("type") != null) property.setPropertyType(request.getParameter("type"));
        if (request.getParameter("purpose") != null) property.setPurpose(request.getParameter("purpose"));
        if (request.getParameter("area") != null) property.setArea(Double.parseDouble(request.getParameter("area")));
        if (request.getParameter("price") != null) property.setPrice(request.getParameter("price"));
        if (request.getParameter("bedrooms") != null)
            property.setNumBedroom(Integer.parseInt(request.getParameter("bedrooms")));
        if (request.getParameter("bathrooms") != null)
            property.setNumBathroom(Integer.parseInt(request.getParameter("bathrooms")));
        if (request.getParameter("floor") != null) property.setFloor(Integer.parseInt(request.getParameter("floor")));
        if (request.getParameter("privatePool") != null)
            property.setPrivatePool(Boolean.parseBoolean(request.getParameter("privatePool")));
        if (request.getParameter("landType") != null) property.setLandType(request.getParameter("landType"));
        if (request.getParameter("legalStatus") != null) property.setLegalStatus(request.getParameter("legalStatus"));
    }

    private void updateListingDetails(ListingEntity listing, MultipartHttpServletRequest request) {
        if (request.getParameter("description") != null) listing.setDescription(request.getParameter("description"));
        if (request.getParameter("listingStatus") != null)
            listing.setListingStatus(request.getParameter("listingStatus"));
    }

    @Transactional
    public List<PropertyImage> updateImages(PropertyEntity property, MultipartHttpServletRequest request) {
        List<PropertyImage> images = new ArrayList<>();

        // 1. Lấy ảnh hiện có từ DB (nếu có)
        if (property.getImages() != null) {
            images.addAll(property.getImages());
        }

        // 2. Xử lý xóa ảnh theo request (các ảnh đã tồn tại nhưng bị yêu cầu xóa)
        List<PropertyImage> imagesToRemove = new ArrayList<>();
        request.getParameterMap().forEach((paramName, values) -> {
            if (paramName.startsWith("removedImages[")) {
                String imageUrlToRemove = values[0];
                images.stream()
                        .filter(img -> img.getImageUrl().equals(imageUrlToRemove))
                        .findFirst()
                        .ifPresent(img -> {
                            imagesToRemove.add(img);
                            images.remove(img);
                        });
            }
        });

        // Xóa ảnh bị yêu cầu remove
        if (!imagesToRemove.isEmpty()) {
            propertyIMGRepo.deleteAll(imagesToRemove);
        }

        // 3. Xử lý các ảnh URL được giữ lại (dạng: images[0]=url)
        request.getParameterMap().forEach((paramName, values) -> {
            if (paramName.startsWith("images[")) {
                String imageUrl = values[0];
                boolean exists = images.stream().anyMatch(img -> img.getImageUrl().equals(imageUrl));
                if (!exists) {
                    PropertyImage image = new PropertyImage(imageUrl, property);
                    images.add(image);
                }
            }
        });

        // 4. Xử lý ảnh mới upload (dạng: newImages[0])
        Iterator<String> fileIterator = request.getFileNames();
        while (fileIterator.hasNext()) {
            String fileParamName = fileIterator.next();
            if (fileParamName.startsWith("newImages[")) {
                List<MultipartFile> files = request.getFiles(fileParamName);
                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        try {
                            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                            Path filePath = Paths.get(uploadDir).resolve(fileName);
                            Files.write(filePath, file.getBytes());

                            String imageUrl = "http://localhost:8082/uploads/" + fileName;
                            PropertyImage image = new PropertyImage(imageUrl, property);
                            images.add(image);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to save image: " + e.getMessage());
                        }
                    }
                }
            }
        }

        // 5. Gán lại quan hệ property cho từng ảnh (bảo đảm chắc chắn)
        images.forEach(img -> img.setProperty(property));

        // 6. Gán danh sách ảnh vào property
        property.setImages(images);

        // 7. Lưu tất cả ảnh mới vào bảng property_images
        propertyIMGRepo.saveAll(images); // <--- BẮT BUỘC

        return images;
    }

    @Override
    public boolean deleteProperty(Integer propertyId, Integer userID) {
        Optional<PropertyEntity> optional = propertyRepo.findById(propertyId);
        if (optional.isEmpty()) {
            return false;
        }

        PropertyEntity property = optional.get();
        // Check if the property belongs to the user
        if (property.getUser() == null || !property.getUser().getUserId().equals(userID)) {
            throw new RuntimeException("Unauthorized: User does not own this property");
        }

        // Delete the property (cascades to PropertyImage and ListingEntity)
        propertyRepo.delete(property);
        return true;
    }

    @Override
    public List<PropertyDTO> search(String city, String propertyType, Double minPrice, Double maxPrice,
                                    Double minArea, Double maxArea, Integer bedrooms, Integer bathrooms) {
        List<PropertyEntity> entities;

        if (city == null && propertyType == null && minPrice == null && maxPrice == null &&
                minArea == null && maxArea == null && bedrooms == null && bathrooms == null) {
            entities = propertyRepo.findAll();
        } else {
            entities = propertyRepo.search(
                    city, propertyType, minPrice, maxPrice, minArea, maxArea, bedrooms, bathrooms);
        }

        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


}
