package com.javaweb.service.impl;

import com.javaweb.repository.entity.MembersEntity;
import com.javaweb.repository.entity.MembershipEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.MembersRepositoryImpl;
import com.javaweb.service.MembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class MembersServiceImpl implements MembersService {
    @Autowired
    private MembersRepositoryImpl membersRepository;

    @Override
    @Transactional
    public boolean createMembershipAfterPayment(Integer userId, Integer membershipId) {
        // Kiểm tra xem đã có bản ghi với userId và membershipId chưa
        MembersEntity existingMember = membersRepository.findByUserUserIdAndMembershipMembershipId(userId, membershipId);
        if (existingMember != null) {
            return false;
        }

        // Tạo mới bản ghi nếu chưa tồn tại
        MembersEntity member = new MembersEntity();

        UserEntity user = new UserEntity();
        user.setUserId(userId);
        member.setUser(user);

        MembershipEntity membership = new MembershipEntity();
        membership.setMembershipId(membershipId);
        member.setMembership(membership);

        LocalDate startDate = LocalDate.now(); // 2025-07-07
        member.setStartDate(startDate);

        LocalDate endDate = startDate.plusMonths(1); // 2025-08-07
        member.setEndDate(endDate);

        member.setStatus(true);

        // Lưu vào database
        membersRepository.save(member);
        return true;
    }
}
