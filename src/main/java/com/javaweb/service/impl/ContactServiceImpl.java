package com.javaweb.service.impl;

import com.javaweb.model.ContactDTO;
import com.javaweb.model.ContactResponseDTO;
import com.javaweb.repository.entity.ContactEntity;
import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.ContactRepositoryImpl;
import com.javaweb.repository.impl.ListingRepository;
import com.javaweb.repository.impl.PropertyRepository;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private  UserRepositoryImpl userRepository;

    @Autowired
    private  PropertyRepository propertyRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private ContactRepositoryImpl contactRepository;

    public ContactServiceImpl() {
        super();
    }

    @Override
    public boolean createContact(int userId, int propertyId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            PropertyEntity property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new IllegalArgumentException("Property not found"));

            ContactEntity contact = new ContactEntity();
            contact.setUser(user);
            contact.setProperty(property);
            contact.setContactDate(LocalDate.now());
            contact.setStatus(false);
            contact.setResponse(null);

            contactRepository.save(contact);
            return true; // Return true if save is successful
        } catch (Exception e) {
            return false; // Return false if any error occurs
        }
    }

    public List<ContactResponseDTO> getAllContacts() {
        List<ContactEntity> contacts = contactRepository.findAll();
        return contacts.stream()
                .map(c -> new ContactResponseDTO(
                        c.getContactId(),
                        c.getUser().getFirstName(),
                        c.getUser().getLastName(),
                        c.getUser().getEmail(),
                        c.getUser().getPhone(),
                        c.isStatus(),
                        c.getContactDate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsViewed(Integer id) {
        contactRepository.markAsViewed(id);
    }

    @Override
    public List<ContactDTO> getContactsForSellerDashboard(Integer sellerUserId) {
        if (sellerUserId == null || sellerUserId <= 0) {
            System.err.println("SellerID" + sellerUserId);
            throw new IllegalArgumentException("ID người bán không hợp lệ");
        }
        List<ContactDTO> contacts = contactRepository.findContactsByPropertyOwnerId(sellerUserId);
        if (contacts.isEmpty()) {
            System.out.println("Không tìm thấy liên hệ nào cho người bán có UserID: {}"+ sellerUserId);
        }
        return contacts;
    }

}
