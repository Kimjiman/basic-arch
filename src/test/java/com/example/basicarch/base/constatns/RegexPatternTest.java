package com.example.basicarch.base.constatns;

import com.example.basicarch.base.constants.RegexPattern;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * packageName    : com.example.basicarch.base.constatns
 * fileName       : RegexPatternTest
 * author         : KIM JIMAN
 * date           : 26. 3. 27. 금요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 27.     KIM JIMAN      First Commit
 */
@DisplayName("RegexPattern 테스트")
@Slf4j
public class RegexPatternTest {
    @Test
    @DisplayName("matching 성공 및 실패")
    void matchingSuc() {
        log.info("id pattern: {}", RegexPattern.ID.getPattern());
        log.info("id message: {}", RegexPattern.ID.getMessage());
        assertThat(RegexPattern.matches("toyriding", RegexPattern.ID)).isTrue();
        assertThat(RegexPattern.matches("ㅋ", RegexPattern.ID)).isFalse();
    }
}
