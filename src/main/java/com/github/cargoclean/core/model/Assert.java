package com.github.cargoclean.core.model;

import java.util.Optional;

public class Assert {

    public static <T> T notNull(T something) {
        return Optional.ofNullable(something)
                .orElseThrow(() -> new InvalidDomainObjectError("Null found for a non-null model attribute"));
    }

    public static String notBlank(String something) {
        if (notNull(something).matches("\\s*")) {
            throw new InvalidDomainObjectError("Empty or blank string found for a non-blank model attribute");
        }
        return something;
    }

}
