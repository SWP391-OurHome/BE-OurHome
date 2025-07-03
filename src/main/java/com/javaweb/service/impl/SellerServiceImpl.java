package com.javaweb.service.impl;

import com.javaweb.model.UserDTO;
import com.javaweb.repository.impl.ListingRepository;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;    // <-- import này

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private UserRepositoryImpl userRepo;

    @Autowired
    private ListingRepository listingRepo;

    @Override
    public List<UserDTO> getAllSellers() {
        List<UserEntity> sellers = userRepo.findByRoleRoleName("Seller");
        List<UserDTO> sellerDTOs = new ArrayList<>();

        for (UserEntity seller : sellers) {
            UserDTO dto = new UserDTO();
            dto.setUserID(seller.getUserId());
            dto.setFirstName(seller.getFirstName());
            dto.setLastName(seller.getLastName());
            dto.setEmail(seller.getEmail());
            dto.setPhone(seller.getPhone());
            dto.setBirthday(seller.getBirthday());
            dto.setImgPath(seller.getImgPath());
            sellerDTOs.add(dto);
        }

        return sellerDTOs;
    }


}