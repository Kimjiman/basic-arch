package com.example.basicarch.module.user.repository;

import com.example.basicarch.base.repository.BaseRepositoryTest;
import com.example.basicarch.module.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * packageName    : com.example.basicarch.module.user.repository
 * fileName       : UserRepositoryTest
 * author         : KIM JIMAN
 * date           : 26. 3. 8. 일요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 8.     KIM JIMAN      First Commit
 */
@Slf4j
public class UserRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.save(User.builder()
                .loginId("admin2")
                .password("1234")
                .name("김지만")
                .useYn("Y")
                .build());

        sessionSetup();
    }

    @AfterEach
    void afterEach() {
        sessionClear();
    }

    @Test
    @DisplayName("유저 조회")
    void findByLoginId() {
        Optional<User> user = userRepository.findByLoginId("admin2");
        log.info("user: {}", user.orElseThrow().toJson());

        assertThat(user).isPresent();
        assertThat(user.get().getLoginId()).isEqualTo("admin2");
    }
}
