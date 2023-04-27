package im.fitdiary.fitdiaryserver.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Entity")
class UserTest {
    @Test
    @DisplayName("name 누락")
    void withoutName() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.builder()
                    .height(100)
                    .gender(Gender.FEMALE)
                    .weight(100)
                    .birthYmd("20001010")
                    .build();
        });
    }

    @Test
    @DisplayName("name 공백")
    void emptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.builder()
                    .height(100)
                    .gender(Gender.FEMALE)
                    .weight(100)
                    .birthYmd("20001010")
                    .name("")
                    .build();
        });
    }

    @Test
    @DisplayName("birthYmd 누락")
    void withoutBirthYmd() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.builder()
                    .height(100)
                    .gender(Gender.FEMALE)
                    .weight(100)
                    .name("홍길동")
                    .build();
        });
    }

    @Test
    @DisplayName("birthYmd 공백")
    void emptyBirthYmd() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.builder()
                    .height(100)
                    .gender(Gender.FEMALE)
                    .weight(100)
                    .birthYmd("")
                    .name("홍길동")
                    .build();
        });
    }

    @Test
    @DisplayName("gender 누락")
    void withoutGender() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.builder()
                    .height(100)
                    .weight(100)
                    .name("홍길동")
                    .birthYmd("20001010")
                    .build();
        });
    }

    @Test
    @DisplayName("height 누락")
    void withoutHeight() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.builder()
                    .gender(Gender.FEMALE)
                    .weight(100)
                    .birthYmd("20101010")
                    .name("홍길동")
                    .build();
        });
    }

    @Test
    @DisplayName("weight 누락")
    void withoutWeight() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.builder()
                    .height(100)
                    .gender(Gender.FEMALE)
                    .birthYmd("20101010")
                    .name("홍길동")
                    .build();
        });
    }
}