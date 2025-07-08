package com.javaweb.service.impl;

import com.javaweb.model.PropertyDTO;
import com.javaweb.repository.entity.*;
import com.javaweb.repository.impl.*;
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
import java.time.LocalDate;
import java.util.*;
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

    @Autowired
    private MembersRepositoryImpl membersRepository;

    @Autowired
    private MembershipRepositoryImpl membershipRepository;

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
                    .map(image -> image.getImageUrl())
                    .collect(Collectors.toList()));
        }

        Optional<ListingEntity> listingOpt = listRepo.findByPropertyId(entity.getId());
        if (listingOpt.isPresent()) {
            ListingEntity listing = listingOpt.get();
            dto.setListingStatus(String.valueOf(listing.getListingStatus()));
            dto.setListingType(listing.getListingType());
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
    public Map<String, Object> createProperty(Integer userId, MultipartHttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);

        MembersEntity member = membersRepository.findByUserUserId(userId);
        if (member == null) {
            response.put("message", "User with ID " + userId + " is not a member or not found.");
            return response;
        }

        LocalDate currentDate = LocalDate.now();
        if (Boolean.FALSE.equals(member.getStatus()) || member.getEndDate().isBefore(currentDate)) {
            response.put("message", "Membership is inactive or expired. Please renew your membership.");
            return response;
        }

        // Lấy thông tin listingType từ request (giả sử có tham số "listingType" trong request)
        String listingType = request.getParameter("listingType") != null ? request.getParameter("listingType").toLowerCase() : "normal";
        boolean isVip = "vip".equals(listingType);

        // Lấy thông tin giới hạn từ MembershipEntity
        Integer membershipId = member.getMembership().getMembershipId();
        MembershipEntity membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found with ID: " + membershipId));
        int maxTotalProperties = membership.getNumListings() != null ? membership.getNumListings() : 0;
        int maxVipProperties = membership.getNumListingsVip() != null ? membership.getNumListingsVip() : 0;

        // Đếm tổng số property và số property VIP
        long totalPropertyCount = propertyRepo.countByUserUserId(userId);
        long vipPropertyCount = propertyRepo.countByUserUserIdAndListingsListingType(userId, "vip");

        // Kiểm tra giới hạn tổng số property
        if (totalPropertyCount >= maxTotalProperties) {
            response.put("message", "You have reached the maximum number of properties (" + maxTotalProperties + ") for your membership level (" + membershipId + ").");
            return response;
        }

        // Kiểm tra giới hạn tin VIP nếu là tin VIP
        if (isVip && vipPropertyCount >= maxVipProperties) {
            response.put("message", "You have reached the maximum number of VIP properties (" + maxVipProperties + ") for your membership level (" + membershipId + ").");
            return response;
        }

        PropertyEntity property = new PropertyEntity();
        UserEntity user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            response.put("message", "User not found with ID: " + userId);
            return response;
        }
        property.setUser(user);

        updatePropertyDetails(property, request);

        property = propertyRepo.save(property);
        List<PropertyImage> images = updateImages(property, request);
        if (!images.isEmpty()) {
            property.setImgURL(images.get(0).getImageUrl());
        }
        property.setImages(images);
        propertyIMGRepo.saveAll(images);

        ListingEntity listing = new ListingEntity();
        updateListingDetails(listing, request);
        listing.setListingType(listingType); // Gán listingType từ request
        listing.setProperty(property);
        listRepo.save(listing);

        response.put("success", true);
        response.put("message", "Property created successfully.");
        return response;
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
        // Cập nhật mô tả nếu có
        if (request.getParameter("description") != null) {
            listing.setDescription(request.getParameter("description"));
        }

        // Cập nhật trạng thái (boolean) nếu có
        if (request.getParameter("listingStatus") != null) {
            String statusStr = request.getParameter("listingStatus").toLowerCase();
            boolean status = statusStr.equals("true") || statusStr.equals("1");
            listing.setListingStatus(status);
        }

        // Cập nhật loại tin (vip / normal) nếu có
        if (request.getParameter("listingType") != null) {
            String type = request.getParameter("listingType").toLowerCase();
            if (type.equals("vip") || type.equals("normal")) {
                listing.setListingType(type);
            }
        }
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
