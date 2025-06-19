package com.javaweb.service;


import com.javaweb.model.ListingDTO;
import com.javaweb.repository.ListingRepository;
import com.javaweb.repository.PropertyRepository;
import com.javaweb.repository.entity.ListingEntity;
import com.javaweb.repository.entity.PropertyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ListingService {


    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private PropertyRepository propertyRepository;


    public Optional<ListingDTO> getListingByPropertyId(Integer propertyId) {
        Optional<ListingEntity> entity = listingRepository.findByPropertyId(propertyId);
        return entity.map(this::convertToDTO);
    }


    public List<ListingDTO> getAll() {
        return listingRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private ListingDTO convertToDTO(ListingEntity entity) {
        return new ListingDTO(
                entity.getListingId(),
                entity.getDescription(),
                entity.getListingStatus(),
                entity.getPropertyId()
        );
    }
    public List<PropertyEntity> search(String city, String propertyType, Double minPrice, Double maxPrice, Double minArea, Double maxArea,
                                       Integer bedrooms, Integer bathrooms) {


        if (city == null && propertyType == null && minPrice == null && maxPrice == null &&
                minArea == null && maxArea == null && bedrooms == null && bathrooms == null) {
            return propertyRepository.findAll();
        }


        return propertyRepository.search(
                city,
                propertyType,
                minPrice,
                maxPrice,
                minArea,
                maxArea,
                bedrooms,
                bathrooms
        );
    }
}

