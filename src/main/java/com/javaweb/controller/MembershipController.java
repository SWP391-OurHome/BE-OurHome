package com.javaweb.controller;

import com.javaweb.model.MembershipDTO;
import com.javaweb.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}