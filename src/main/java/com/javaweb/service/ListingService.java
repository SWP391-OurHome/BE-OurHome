package com.javaweb.service;

import com.javaweb.model.ListingDTO;
import com.javaweb.model.PropertyDTO;

import java.util.List;
import java.util.Optional;

public interface ListingService {
    public Optional<ListingDTO> getListingByPropertyId(Integer propertyId);
    public List<ListingDTO> getAll();
    public List<ListingDTO> getListingsByProperties(List<PropertyDTO> propertyDTOs);
}
