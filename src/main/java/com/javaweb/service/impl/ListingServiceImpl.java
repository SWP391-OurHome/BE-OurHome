package com.javaweb.service.impl;

import com.javaweb.model.ListingDTO;
import com.javaweb.model.PropertyDTO;
import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.ListingRepository;
import com.javaweb.repository.entity.ListingEntity;
import com.javaweb.repository.impl.PropertyRepository;
import com.javaweb.service.ListingService;
import com.javaweb.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListingServiceImpl implements ListingService {

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Optional<ListingDTO> getListingByPropertyId(Integer propertyId) {
        Optional<ListingEntity> entity = listingRepository.findByPropertyId(propertyId);
        return entity.map(this::convertToDTO);
    }

    @Override
    public List<ListingDTO> getAll() {
        return listingRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListingDTO> getListingsByProperties(List<PropertyDTO> propertyDTOs) {
        if (propertyDTOs == null || propertyDTOs.isEmpty()) {
            System.out.println("Danh sách PropertyDTO rỗng");
            return List.of();
        }
        List<Integer> propertyIds = propertyDTOs.stream()
                .map(PropertyDTO::getPropertyID)
                .collect(Collectors.toList());
        System.out.println("Số lượng propertyIds: " + propertyIds.size() + ", IDs: " + propertyIds);

        List<ListingEntity> listingEntities = listingRepository.findByPropertyIdIn(propertyIds);
        System.out.println("Số lượng ListingEntity tìm thấy: " + listingEntities.size());
        if (listingEntities.size() < propertyIds.size()) {
            System.out.println("Cảnh báo: Số lượng ListingEntity ít hơn propertyIds. Kiểm tra dữ liệu!");
        }

        return listingEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void toggleStatusByPropertyId(Integer propertyId) {
        ListingEntity listing = listingRepository.findByProperty_Id(propertyId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        boolean newStatus = !listing.getListingStatus(); // Toggle
        listing.setListingStatus(newStatus);
        listingRepository.save(listing);

        // If new status is false (banned), send notification
        if (!newStatus) {
            PropertyEntity property = listing.getProperty();
            UserEntity owner = property.getUser(); // Assume PropertyEntity has getOwner() returning UserEntity
            Integer userId = owner.getUserId();
            Integer listingId = listing.getListingId();
            String reason = "Violation of posting guidelines"; // Customize reason as needed
            notificationService.notifyPostBanned(userId, listingId, reason);
        }
    }




    private ListingDTO convertToDTO(ListingEntity entity) {
        return new ListingDTO(
                entity.getListingId(),
                entity.getDescription(),
                entity.getListingStatus(),
                entity.getProperty().getId(),
                entity.getListingType()
        );
    }
}
