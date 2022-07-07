package com.github.cargoclean.core;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */

/**
 * Modeled after original "se.citerus.dddsample.domain.shared.Specification".
 */
public interface Specification<T> {

    boolean isSatisfiedBy(T t);

}
