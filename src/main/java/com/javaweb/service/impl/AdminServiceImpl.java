package com.javaweb.service.impl;


import com.javaweb.model.AdminDashboardDTO;
import com.javaweb.model.TopSellingDTO;
import com.javaweb.model.UserDTO;
import com.javaweb.repository.entity.MembersEntity;
import com.javaweb.repository.entity.MembershipEntity;
import com.javaweb.repository.entity.RoleEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.*;
import com.javaweb.service.AdminService;
import org.modelmapper.internal.util.Members;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepositoryImpl userRepo;

    @Autowired
    private RoleRepositoryImpl roleRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private PaymentRepositoryImpl paymentRepo;

    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    private MembersRepositoryImpl  membersRepo;

    @Autowired
    private MembershipRepositoryImpl  membershipRepo;
    //Overview
    @Override
    public AdminDashboardDTO getTotalDashboard() {
        AdminDashboardDTO dto = new AdminDashboardDTO();
        dto.setProperties((int)propertyRepo.count());
        dto.setUsers((int)userRepo.count());
        dto.setRevunements(paymentRepo.sumAmounts()); // Use sum, not count
        dto.setReports((int)reportRepo.count());
        dto.setTransactions((int)paymentRepo.count());
        return dto;
    }

    @Override
    public List<TopSellingDTO> getTopSelling() {
        List<MembersEntity> members = membersRepo.findAll();
        List<TopSellingDTO> dtos = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (MembershipEntity m : membershipRepo.findAll()) {
            long activeCount = members.stream()
                    .filter(member ->
                            member.getMembership().getMembershipId().equals(m.getMembershipId()) &&
                                    Boolean.TRUE.equals(member.getStatus()) &&
                                    member.getEndDate() != null &&
                                    member.getEndDate().isAfter(currentDate)
                    )
                    .count();

            if (activeCount > 0) {
                TopSellingDTO dto = new TopSellingDTO();
                dto.setType(m.getType());
                dto.setUnitPrice((long) (m.getPrice() * activeCount)); // ép double -> long
                dto.setSold((int) activeCount);
                dtos.add(dto);
            }
        }

        dtos.sort(Comparator.comparingLong(TopSellingDTO::getUnitPrice).reversed());

        return dtos;
    }


    //User Manager
    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepo.findAll();
        return users.stream().map(user -> {
            UserDTO dto = new UserDTO();
            dto.setUserID(user.getUserId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setBirthday(user.getBirthday());
            dto.setRoleName(user.getRole().getRoleName());
            dto.setActive(user.getIsActive());
            dto.setCreateDate(user.getCreateDate());
            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    public boolean updateUserRole(Integer userId, Integer roleId) {
        Optional<UserEntity> userOpt = userRepo.findById(userId);
        Optional<RoleEntity> roleOpt = roleRepo.findById(roleId);
        if (userOpt.isPresent() && roleOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setRole(roleOpt.get());
            userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean banUser(Integer userId) {
        Optional<UserEntity> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setIsActive(false);
            userRepo.save(user);
            return true;
        }
        return false;
    }
}