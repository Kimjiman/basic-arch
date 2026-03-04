package com.example.basicarch.config.scheduler;

import com.example.basicarch.module.code.facade.CodeCacheFacade;
import com.example.basicarch.module.menu.facade.MenuCacheFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CacheScheduler {
    private final CodeCacheFacade codeCacheFacade;
    private final MenuCacheFacade menuCacheFacade;

    @Scheduled(cron = "${cron.cache.refresh-code}")
    public void refreshCodeCache() {
        codeCacheFacade.refresh();
    }

    @Scheduled(cron = "${cron.cache.refresh-menu}")
    public void refreshMenuCache() {
        menuCacheFacade.refresh();
    }
}
