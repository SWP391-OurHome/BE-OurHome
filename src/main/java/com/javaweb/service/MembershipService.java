package com.javaweb.service;

import com.javaweb.model.MembershipDTO;

import java.util.List;

public interface MembershipService {
    List<MembershipDTO> getAllMemberships();
    MembershipDTO findById(Integer id);
    MembershipDTO save(MembershipDTO dto);
    MembershipDTO update(Integer id, MembershipDTO dto);
    void deleteById(Integer id);
}
