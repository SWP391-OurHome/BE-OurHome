package com.javaweb.service.impl;

import com.javaweb.model.PropertyDTO;
import com.javaweb.repository.ListingRepository;
import com.javaweb.repository.PropertyImageRepository;
import com.javaweb.repository.PropertyRepository;
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
    public List<PropertyEntity> getAllProperties() {
        return List.of();
    }

    @Override
    public List<PropertyDTO> findPropertiesByUserId(Integer userId) {
        List<PropertyEntity> propertyEntities = propertyRepo.findByUserId(userId);
        return propertyEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PropertyDTO convertToDTO(PropertyEntity entity) {
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
        // Create new entities
        PropertyEntity property = new PropertyEntity();
        ListingEntity listing = new ListingEntity();

        // Set user ID and associate entities
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        property.setUser(user);
        listing.setProperty(property); // Liên kết tạm thời

        // Populate property and listing details
        updatePropertyDetails(property, request);
        updateListingDetails(listing, request);

        // Handle image uploads
        List<PropertyImage> images = updateImages(property, request);

        // Save property first to generate PropertyID
        property.setImages(images);
        property = propertyRepo.save(property); // Lưu để lấy PropertyID

        listing.setProperty(property); // Gán PropertyID cho listing
        listRepo.save(listing); // Lưu listing với propertyId hợp lệ

        // Save images
        propertyIMGRepo.saveAll(images);

        return true;
    }

    private void updatePropertyDetails(PropertyEntity property, MultipartHttpServletRequest request) {
        if (request.getParameter("addressLine1") != null) property.setAddressLine1(request.getParameter("addressLine1"));
        if (request.getParameter("addressLine2") != null) property.setAddressLine2(request.getParameter("addressLine2"));
        if (request.getParameter("region") != null) property.setRegion(request.getParameter("region"));
        if (request.getParameter("city") != null) property.setCity(request.getParameter("city"));
        if (request.getParameter("type") != null) property.setPropertyType(request.getParameter("type"));
        if (request.getParameter("purpose") != null) property.setPurpose(request.getParameter("purpose"));
        if (request.getParameter("area") != null) property.setArea(Double.parseDouble(request.getParameter("area")));
        if (request.getParameter("price") != null) property.setPrice(request.getParameter("price"));
        if (request.getParameter("bedrooms") != null) property.setNumBedroom(Integer.parseInt(request.getParameter("bedrooms")));
        if (request.getParameter("bathrooms") != null) property.setNumBathroom(Integer.parseInt(request.getParameter("bathrooms")));
        if (request.getParameter("floor") != null) property.setFloor(Integer.parseInt(request.getParameter("floor")));
        if (request.getParameter("privatePool") != null) property.setPrivatePool(Boolean.parseBoolean(request.getParameter("privatePool")));
        if (request.getParameter("landType") != null) property.setLandType(request.getParameter("landType"));
        if (request.getParameter("legalStatus") != null) property.setLegalStatus(request.getParameter("legalStatus"));
    }

    private void updateListingDetails(ListingEntity listing, MultipartHttpServletRequest request) {
        if (request.getParameter("description") != null) listing.setDescription(request.getParameter("description"));
        if (request.getParameter("listingStatus") != null) listing.setListingStatus(request.getParameter("listingStatus"));
    }

    @Transactional
    public List<PropertyImage> updateImages(PropertyEntity property, MultipartHttpServletRequest request) {
        List<PropertyImage> images = new ArrayList<>();

        // Retain existing images
        if (property.getImages() != null) {
            images.addAll(property.getImages());
        }

        // Handle removed images
        Iterator<String> removedIterator = request.getParameterNames().asIterator();
        List<PropertyImage> imagesToRemove = new ArrayList<>();
        while (removedIterator.hasNext()) {
            String paramName = removedIterator.next();
            if (paramName.startsWith("removedImages[")) {
                String imageUrlToRemove = request.getParameter(paramName);
                imagesToRemove.addAll(images.stream()
                        .filter(image -> image.getImageUrl().equals(imageUrlToRemove))
                        .collect(Collectors.toList()));
                images.removeIf(image -> image.getImageUrl().equals(imageUrlToRemove));
            }
        }

        // Delete removed images from the PropertyImage table
        if (!imagesToRemove.isEmpty()) {
            propertyIMGRepo.deleteAll(imagesToRemove); // Xóa khỏi bảng PropertyImage
        }

        // Add or update existing image URLs
        Iterator<String> paramIterator = request.getParameterNames().asIterator();
        while (paramIterator.hasNext()) {
            String paramName = paramIterator.next();
            if (paramName.startsWith("images[")) {
                String imageUrl = request.getParameter(paramName);
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    PropertyImage image = images.stream()
                            .filter(img -> img.getImageUrl().equals(imageUrl))
                            .findFirst()
                            .orElse(new PropertyImage(imageUrl, property));
                    images.add(image);
                }
            }
        }

        // Handle new image uploads
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!directory.canWrite()) {
            throw new RuntimeException("No write permission for directory: " + uploadDir);
        }

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
                            PropertyImage image = new PropertyImage("http://localhost:8082/uploads/" + fileName, property);
                            images.add(image);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to save image: " + e.getMessage());
                        }
                    }
                }
            }
        }

        // Update the images list in the property
        images.forEach(image -> image.setProperty(property));
        property.setImages(images);

        // Save the updated property to the database
        propertyRepo.save(property);

        return images;
    }


    @Override
    @Transactional
    public boolean deleteProperty(Integer propertyId, Integer userId) {
        try {
            PropertyEntity property = propertyRepo.findById(propertyId)
                    .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

            if (!property.getUser().getUserId().equals(userId)) {
                return false;
            }

            Optional<ListingEntity> listingOpt = listRepo.findByPropertyId(propertyId);
            if (listingOpt.isPresent()) {
                listRepo.delete(listingOpt.get());
            }

            propertyRepo.delete(property);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
