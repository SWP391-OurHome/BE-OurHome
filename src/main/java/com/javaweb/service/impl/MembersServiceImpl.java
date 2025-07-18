package com.javaweb.service.impl;

import com.javaweb.repository.entity.MembersEntity;
import com.javaweb.repository.entity.MembershipEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.MembersRepositoryImpl;
import com.javaweb.repository.impl.MembershipRepositoryImpl;
import com.javaweb.service.MembersService;
import com.javaweb.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MembersServiceImpl implements MembersService {
    @Autowired
    private MembersRepositoryImpl membersRepository;

    @Autowired
    private MembershipRepositoryImpl membershipRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public boolean createMembershipAfterPayment(Integer userId, Integer membershipId) {
        MembershipEntity membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new NoSuchElementException("Membership not found with id: " + membershipId));

        if (Boolean.FALSE.equals(membership.getActive())) {
            // Có thể log hoặc throw exception để gửi thông báo đến FE
            // Ví dụ: throw new IllegalStateException("Membership package is disabled.");
            return false; // Trả về false và caller xử lý thông báo
        }

        MembersEntity existingMember = membersRepository.findByUserUserIdAndMembershipMembershipId(userId, membershipId);
        if (existingMember != null) {
            if (!existingMember.getStatus()) {
                return renewMembership(userId, membershipId);
            } else {
                return false;
            }
        }

        // Tạo mới bản ghi nếu chưa tồn tại
        MembersEntity member = new MembersEntity();

        UserEntity user = new UserEntity();
        user.setUserId(userId);
        member.setUser(user);

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

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deactivateExpiredMemberships() {
        LocalDate currentDate = LocalDate.now();
        List<MembersEntity> activeMembers = membersRepository.findByStatus(true);
        for (MembersEntity member : activeMembers) {
            if (member.getEndDate().isBefore(currentDate)) {
                member.setStatus(false);
                membersRepository.save(member);

                // Gửi thông báo hết hạn qua NotificationService
                Integer userId = member.getUser().getUserId();
                Integer membershipId = member.getMembership().getMembershipId();
                notificationService.notifyMemberExpired(userId, membershipId);
            }
        }
    }

    @Override
    @Transactional
    public boolean renewMembership(Integer userId, Integer membershipId) {
        MembershipEntity membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new NoSuchElementException("Membership not found with id: " + membershipId));

        if (Boolean.FALSE.equals(membership.getActive())) {
            // Có thể log hoặc throw exception để gửi thông báo đến FE
            // Ví dụ: throw new IllegalStateException("Membership package is disabled.");
            return false; // Trả về false và caller xử lý thông báo
        }

        MembersEntity existingMember = membersRepository.findByUserUserIdAndMembershipMembershipId(userId, membershipId);
        if (existingMember == null) {
            return false; // Hoặc có thể tạo mới nếu cần, nhưng theo logic hiện tại là không tạo mới
        }

        LocalDate currentDate = LocalDate.now();
        if (existingMember.getEndDate().isBefore(currentDate)) {
            existingMember.setStatus(false);
            // Có thể gửi thông báo hết hạn ở đây
            // System.out.println("Membership for user " + userId + " has expired. Renewing now.");
        }

        LocalDate newStartDate = currentDate;
        existingMember.setStartDate(newStartDate);
        LocalDate newEndDate = newStartDate.plusMonths(1);
        existingMember.setEndDate(newEndDate);
        existingMember.setStatus(true);

        membersRepository.save(existingMember);
        return true;
    }

}