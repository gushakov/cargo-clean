package com.github.cargoclean.core.model.location;

import com.github.cargoclean.core.model.InvalidDomainObjectError;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocationTest {

    @ParameterizedTest
    @ValueSource(strings = {"JNTKO", "AUMEL"})
    void should_build_valid_unlocode(String code) {
        final UnLocode unLocode = UnLocode.builder()
                .code(code)
                .build();
        assertThat(unLocode.getCode()).isEqualTo(code);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "123", "foo bar"})
    void should_not_accept_invalid_unlocodes(String code) {
        assertThrows(InvalidDomainObjectError.class,
                () -> UnLocode.builder().code(code).build());
    }
}
