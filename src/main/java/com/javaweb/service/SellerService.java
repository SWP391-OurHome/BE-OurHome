package com.javaweb.service;

import com.javaweb.model.ListingDTO;
import com.javaweb.model.UserDTO;
import com.javaweb.repository.entity.UserEntity;

import java.util.List;

public interface SellerService {
    public List<UserDTO> getAllSellers();
}
