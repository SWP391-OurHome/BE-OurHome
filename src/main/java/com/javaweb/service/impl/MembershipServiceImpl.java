package com.javaweb.service.impl;

import com.javaweb.model.MembershipDTO;
import com.javaweb.repository.entity.MembershipEntity;
import com.javaweb.repository.impl.MembershipRepositoryImpl;
import com.javaweb.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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

    @Override
    public MembershipDTO findById(Integer id) {
        return convertToDTO(membershipRepository.findById(id).orElse(null));
    }

    @Override
    public MembershipDTO save(MembershipDTO dto) {
        validate(dto);
        MembershipEntity entity = toEntity(dto);
        entity.setMembershipId(null); // Ensure new entity
        return convertToDTO(membershipRepository.save(entity));
    }

    @Override
    public MembershipDTO update(Integer id, MembershipDTO dto) {
        validate(dto);
        MembershipEntity entity = membershipRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Membership not found with id: " + id));
        entity.setType(dto.getType());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setNumListings(dto.getNumListings());
        entity.setNumListingsVip(dto.getNumListingsVip());
        return convertToDTO(membershipRepository.save(entity));
    }

    @Override
    public void disableById(Integer id) {
        MembershipEntity entity = membershipRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Membership not found with id: " + id));
        entity.setActive(false);
        membershipRepository.save(entity);
    }

    private MembershipDTO convertToDTO(MembershipEntity entity) {
        return new MembershipDTO(
                entity.getMembershipId(),
                entity.getType(),
                entity.getPrice(),
                entity.getDescription(),
                entity.getNumListings(),
                entity.getNumListingsVip(),
                entity.getActive()
        );
    }

    private void validate(MembershipDTO dto) {
        if (dto.getType() == null || dto.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Type is required and cannot be empty");
        }
        if (dto.getPrice() == null || dto.getPrice() < 0) {
            throw new IllegalArgumentException("Price is required and must be non-negative");
        }
        if (dto.getNumListings() != null && dto.getNumListings() < 0) {
            throw new IllegalArgumentException("Number of listings must be non-negative");
        }
        if (dto.getNumListingsVip() != null && dto.getNumListingsVip() < 0) {
            throw new IllegalArgumentException("Number of VIP listings must be non-negative");
        }
    }

    private MembershipEntity toEntity(MembershipDTO dto) {
        return new MembershipEntity(
                dto.getMembershipId(),
                dto.getType(),
                dto.getPrice(),
                dto.getDescription(),
                dto.getNumListings(),
                dto.getNumListingsVip()
        );
    }
}
