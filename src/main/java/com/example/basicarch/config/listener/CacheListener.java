package com.example.basicarch.config.listener;

import com.example.basicarch.module.code.facade.CodeFacade;
import com.example.basicarch.module.menu.facade.MenuFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.example.basicarch.config.listener
 * fileName       : CacheListener
 * author         : KIM JIMAN
 * date           : 26. 3. 15. 일요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 15.     KIM JIMAN      First Commit
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class CacheListener {
    private final CodeFacade codeFacade;
    private final MenuFacade menuFacade;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        codeFacade.refresh();
        menuFacade.refresh();
    }
}
