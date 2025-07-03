package com.javaweb.service;

import com.javaweb.model.ListingDTO;
import com.javaweb.model.PropertyDTO;
import com.javaweb.repository.entity.PropertyEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface PropertyService {
    List<PropertyDTO> getAllProperties();
    public List<PropertyDTO> findPropertiesByUserId(Integer userId);
    public boolean updateProperty(Integer userID, MultipartHttpServletRequest request);
    public boolean createProperty(Integer userID, MultipartHttpServletRequest request);
    public boolean deleteProperty(Integer propertyID,Integer userID);
    public List<PropertyDTO> search(String city, String propertyType, Double minPrice, Double maxPrice,
                                    Double minArea, Double maxArea, Integer bedrooms, Integer bathrooms);
}
