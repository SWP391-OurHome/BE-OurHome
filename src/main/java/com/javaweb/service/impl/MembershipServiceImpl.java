package com.javaweb.service.impl;

import com.javaweb.model.MembershipDTO;
import com.javaweb.repository.entity.MembershipEntity;
import com.javaweb.repository.impl.MembershipRepositoryImpl;
import com.javaweb.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipServiceImpl implements MembershipService {
    @Autowired
    private MembershipRepositoryImpl membershipRepository;

    @Override
    public List<MembershipDTO> getAllMemberships() {
        List<MembershipEntity> memberships = membershipRepository.findAll();
        return memberships.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MembershipDTO convertToDTO(MembershipEntity entity) {
        return new MembershipDTO(
                entity.getMembershipId(),
                entity.getType(),
                entity.getPrice(),
                entity.getDescription()
        );
    }

}
