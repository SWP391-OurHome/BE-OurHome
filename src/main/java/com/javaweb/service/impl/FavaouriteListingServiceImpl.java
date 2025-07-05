package com.javaweb.service.impl;

import com.javaweb.model.FavouriteListingDTO;
import com.javaweb.model.PropertyDTO;
import com.javaweb.repository.entity.FavouriteListingEntity;
import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.FavouriteListingRepositoryImpl;
import com.javaweb.service.FavouriteListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavaouriteListingServiceImpl implements FavouriteListingService{

    @Autowired
    private FavouriteListingRepositoryImpl favouriteListingRepository;

    @Autowired
    private PropertyServiceImpl propertyService;

    @Override
    public boolean addToFavourite(FavouriteListingDTO request) {
        // Kiểm tra nếu đã tồn tại favourite
        if (favouriteListingRepository.existsByUser_UserIdAndProperty_Id(request.getUserId(), request.getPropertyId())) {
            return false;
        }

        // Tạo mới entity
        FavouriteListingEntity entity = new FavouriteListingEntity();

        // Tạo UserEntity và PropertyEntity với ID
        UserEntity user = new UserEntity();
        user.setUserId(request.getUserId());

        PropertyEntity property = new PropertyEntity();
        property.setId(request.getPropertyId());

        entity.setUser(user);
        entity.setProperty(property);

        favouriteListingRepository.save(entity);
        return true;
    }

    @Override
    public boolean removeFromFavourite(FavouriteListingDTO request) {
        FavouriteListingEntity entity = favouriteListingRepository
                .findByUser_UserIdAndProperty_Id(request.getUserId(), request.getPropertyId());

        if (entity != null) {
            favouriteListingRepository.delete(entity);
            return true;
        }
        return false;
    }

    @Override
    public List<FavouriteListingDTO> getFavouriteList(Integer userId) {
        List<FavouriteListingEntity> entities = favouriteListingRepository.findByUser_UserId(userId);

        if (entities == null || entities.isEmpty()) {
            System.out.println("No favourite listings found for userId: " + userId);
            return new ArrayList<>();
        }

        return entities.stream()
                .map(entity -> {
                    FavouriteListingDTO dto = new FavouriteListingDTO();
                    dto.setFavouriteId(entity.getId());
                    dto.setUserId(entity.getUser().getUserId());
                    dto.setPropertyId(entity.getProperty().getId());

                    // Sử dụng convertTo từ PropertyService để ánh xạ PropertyEntity sang PropertyDTO
                    PropertyDTO propertyDTO = null;
                    if (entity.getProperty() != null) {
                        propertyDTO = propertyService.convertToDTO(entity.getProperty());
                    }
                    dto.setProperty(propertyDTO);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
