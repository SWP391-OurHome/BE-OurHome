package com.javaweb.service;

import com.javaweb.model.ListingDTO;
import com.javaweb.repository.ListingRepository;
import com.javaweb.repository.entity.ListingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListingService {

    @Autowired
    private ListingRepository listingRepository;

    public Optional<ListingDTO> getListingByPropertyId(Long propertyId) {
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
}
