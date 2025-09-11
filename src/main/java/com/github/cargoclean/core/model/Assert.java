package com.github.cargoclean.core.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Assert {

    private Assert() {
    }

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

    public static int positive(int value) {
        if (value <= 0) {
            throw new InvalidDomainObjectError("Value must be positive");
        }
        return value;
    }

    /**
     * Returns a defensive immutable copy of the provided list.
     * Returns an empty list if the parameter is null.
     *
     * @param list the list to copy, can be null
     * @param <T>  the type of elements in the list
     * @return immutable copy of the list, never null
     */
    public static <T> List<T> defensiveCopy(List<T> list) {
        return list != null ? List.copyOf(list) : List.of();
    }

    /**
     * Returns a defensive immutable copy of the provided set.
     * Returns an empty set if the parameter is null.
     *
     * @param set the set to copy, can be null
     * @param <T> the type of elements in the set
     * @return immutable copy of the set, never null
     */
    public static <T> Set<T> defensiveCopy(Set<T> set) {
        return set != null ? Set.copyOf(set) : Set.of();
    }

}