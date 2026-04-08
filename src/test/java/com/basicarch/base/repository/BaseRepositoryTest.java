package com.basicarch.base.repository;

import com.basicarch.base.security.AuthUserDetails;
import com.basicarch.module.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

/**
 * packageName    : com.basicarch.base.repository
 * fileName       : BaseRepositoryTest
 * author         : KIM JIMAN
 * date           : 26. 3. 8. 일요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 8.     KIM JIMAN      First Commit
 */

/*
* @DataJpaTest: Jpa관련 Bean 생성(queryDsl 포함 안되어있음)
* @AutoConfigureTestDatabase: 기본 H2 DB 사용 X -> 설정된 DB 사용(postgre)
* @Testcontainers: @Container달린 필드 테스트 시작과 종료에 자동시작/자동종료
* @Import: JPAQueryFactory 빈을 수동으로 등록한 TestConfig를 컨텍스트에 추가
* */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@Import(BaseRepositoryTest.TestConfig.class)
public abstract class BaseRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    // 컨테이너가 띄워진 후 동적으로 DB 연결 정보 주입
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @TestConfiguration
    static class TestConfig {
        // queryDsl 수동등록
        @Bean
        JPAQueryFactory jpaQueryFactory(EntityManager em) {
            return new JPAQueryFactory(em);
        }
    }

    protected void sessionSetup() {
        User testUser = User.builder()
                .loginId("testUser")
                .password("pass1234")
                .name("테스트유저")
                .useYn("Y")
                .roleList(List.of("USR"))
                .build();
        testUser.setSystemUser();

        AuthUserDetails authUserDetails = new AuthUserDetails(testUser);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(authUserDetails, null, authUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected void sessionClear() {
        SecurityContextHolder.clearContext();
    }
}
