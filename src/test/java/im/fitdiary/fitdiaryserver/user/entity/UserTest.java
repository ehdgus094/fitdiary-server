package im.fitdiary.fitdiaryserver.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("User Entity")
class UserTest {
    @Test
    @DisplayName("name 누락")
    void withoutName() {
        assertThatThrownBy(() ->
            User.builder()
                    .height(100)
                    .gender(Gender.FEMALE)
                    .weight(100)
                    .birthYmd("20001010")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("name 공백")
    void emptyName() {
        assertThatThrownBy(() ->
            User.builder()
                    .height(100)
                    .gender(Gender.FEMALE)
                    .weight(100)
                    .birthYmd("20001010")
                    .name("")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("birthYmd 누락")
    void withoutBirthYmd() {
        assertThatThrownBy(() ->
            User.builder()
                    .height(100)
                    .gender(Gender.FEMALE)
                    .weight(100)
                    .name("홍길동")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("birthYmd 공백")
    void emptyBirthYmd() {
        assertThatThrownBy(() ->
            User.builder()
                    .height(100)
                    .gender(Gender.FEMALE)
                    .weight(100)
                    .birthYmd("")
                    .name("홍길동")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("gender 누락")
    void withoutGender() {
        assertThatThrownBy(() ->
            User.builder()
                    .height(100)
                    .weight(100)
                    .name("홍길동")
                    .birthYmd("20001010")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("height 누락")
    void withoutHeight() {
        assertThatThrownBy(() ->
            User.builder()
                    .gender(Gender.FEMALE)
                    .weight(100)
                    .birthYmd("20101010")
                    .name("홍길동")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("weight 누락")
    void withoutWeight() {
        assertThatThrownBy(() ->
            User.builder()
                    .height(100)
                    .gender(Gender.FEMALE)
                    .birthYmd("20101010")
                    .name("홍길동")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }
}