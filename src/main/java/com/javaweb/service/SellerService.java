package com.javaweb.service;

import com.javaweb.model.ContactOverTimeDTO;
import com.javaweb.model.ListingDTO;
import com.javaweb.model.TopPropertyDTO;
import com.javaweb.model.UserDTO;
import com.javaweb.repository.entity.UserEntity;

import java.util.List;

public interface SellerService {
    public List<UserDTO> getAllSellers();
    public int getContactsCount(Integer userId);
    public String getMembershipType(Integer userId);
    public int getActiveListingsCount(Integer userId);
    public List<TopPropertyDTO> getTopProperties(Integer userId);
    public List<ContactOverTimeDTO> getContactsOverTime(Integer userId);
}
