package com.javaweb.service;

import com.javaweb.model.ContactDTO;
import com.javaweb.model.ContactResponseDTO;

import java.util.List;

public interface ContactService {
    public boolean  createContact(int userId, int propertyId);
    List<ContactResponseDTO> getAllContacts();
    public void markAsViewed(Integer id);
    List<ContactDTO> getContactsForSellerDashboard(Integer sellerUserId);

}
