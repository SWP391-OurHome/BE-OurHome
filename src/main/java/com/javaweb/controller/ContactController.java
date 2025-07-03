package com.javaweb.controller;


import com.javaweb.model.ContactDTO;
import com.javaweb.model.ContactResponseDTO;
import com.javaweb.repository.entity.ContactEntity;
import com.javaweb.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mysql.cj.conf.PropertyKey.logger;


@RestController
@RequestMapping("api/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;


    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Integer> request) {
        int userId = request.get("userId");
        int propertyId = request.get("propertyId");
        boolean booking = contactService.createContact(userId, propertyId);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContactResponseDTO>> getAllBookings() {
        List<ContactResponseDTO> response = contactService.getAllContacts();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/view")
    public ResponseEntity<?> markBookingAsViewed(@PathVariable Integer id) {
        contactService.markAsViewed(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{sellerUserId}")
    public ResponseEntity<List<ContactDTO>> getSellerContacts(@PathVariable Integer sellerUserId) {
        List<ContactDTO> contacts = contactService.getContactsForSellerDashboard(sellerUserId);
        return ResponseEntity.ok(contacts);
    }
}

