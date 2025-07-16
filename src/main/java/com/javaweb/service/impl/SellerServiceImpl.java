package com.javaweb.service.impl;

import com.javaweb.model.ContactOverTimeDTO;
import com.javaweb.model.TopPropertyDTO;
import com.javaweb.model.UserDTO;
import com.javaweb.repository.entity.ContactEntity;
import com.javaweb.repository.entity.MembersEntity;
import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.impl.*;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;    // <-- import này

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private UserRepositoryImpl userRepo;

    @Autowired
    private ListingRepository listingRepo;

    @Autowired
    private MembersRepositoryImpl membersRepository;

    @Autowired
    private ContactRepositoryImpl contactRepository;

    @Autowired
    private PropertyRepository propertyRepository;

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

    @Override
    public int getActiveListingsCount(Integer userId) {
        return listingRepo.countActiveByUserId(userId);
    }

    @Override
    public String getMembershipType(Integer userId) {
        MembersEntity activeMembership = membersRepository.findActiveByUserId(userId);
        if (activeMembership != null) {
            return activeMembership.getMembership().getType();
        }
        return null;
    }

    @Override
    public int getContactsCount(Integer userId) {
        return contactRepository.countByLandlordUserId(userId);
    }

    @Override
    public List<ContactOverTimeDTO> getContactsOverTime(Integer userId) {
        // Fetch all contacts for the user
        List<ContactEntity> contacts = contactRepository.findByPropertyUserId(userId); // Assume new repo method

        // Group by month
        Map<YearMonth, Long> contactsByMonth = contacts.stream()
                .collect(Collectors.groupingBy(c -> YearMonth.from(c.getContactDate()), Collectors.counting()));

        // Create DTO list, fill last 12 months for example
        List<ContactOverTimeDTO> data = new ArrayList<>();
        YearMonth current = YearMonth.now();
        for (int i = 11; i >= 0; i--) {
            YearMonth month = current.minusMonths(i);
            long count = contactsByMonth.getOrDefault(month, 0L);
            data.add(new ContactOverTimeDTO(month.getMonth().toString().substring(0, 3), (int) count));
        }

        // Add active listings if needed, similar grouping

        return data;
    }

    @Override
    public List<TopPropertyDTO> getTopProperties(Integer userId) {
        // Fetch properties for user
        List<PropertyEntity> properties = propertyRepository.findByUserId(userId);

        // For each property, count contacts
        List<TopPropertyDTO> top = properties.stream()
                .map(p -> {
                    int contacts = contactRepository.countByPropertyId(p.getId()); // Assume repo method
                    return new TopPropertyDTO(p.getId(), p.getAddressLine1(), p.getPrice(), contacts, p.getImgURL());
                })
                .sorted((a, b) -> b.getContacts() - a.getContacts()) // Sort by contacts desc
                .limit(5) // Top 5
                .collect(Collectors.toList());

        return top;
    }
}