package com.javaweb.service;

public interface MembersService {
     boolean createMembershipAfterPayment(Integer userId, Integer membershipId);
     public void deactivateExpiredMemberships();
     public boolean renewMembership(Integer userId, Integer membershipId);
}
