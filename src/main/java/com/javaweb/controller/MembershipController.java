package com.javaweb.controller;

import com.javaweb.model.MembershipDTO;
import com.javaweb.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/memberships")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public List<MembershipDTO> getAllMemberships() {
        return membershipService.getAllMemberships();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipDTO> getMembershipById(@PathVariable Integer id) {
        MembershipDTO dto = membershipService.findById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<MembershipDTO> createMembership(@RequestBody MembershipDTO dto) {
        try {
            MembershipDTO saved = membershipService.save(dto);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipDTO> updateMembership(@PathVariable Integer id, @RequestBody MembershipDTO dto) {
        try {
            MembershipDTO updated = membershipService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Void> disableMembership(@PathVariable Integer id) {
        try {
            membershipService.disableById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}