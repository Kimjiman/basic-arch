package com.example.basicarch.config.scheduler;

import com.example.basicarch.module.code.facade.CodeFacade;
import com.example.basicarch.module.menu.facade.MenuFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CacheScheduler {
    private final CodeFacade codeFacade;
    private final MenuFacade menuFacade;

    @Scheduled(cron = "${cron.cache.refresh-code}")
    public void refreshCodeCache() {
        codeFacade.refresh();
    }

    @Scheduled(cron = "${cron.cache.refresh-menu}")
    public void refreshMenuCache() {
        menuFacade.refresh();
    }
}
