package com.javaweb.config;


import com.javaweb.service.MembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class StartupTaskRunner {


    @Autowired
    private MembersService membersService;


    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup() {
        membersService.deactivateExpiredMemberships();
    }
}



