package com.github.cargoclean.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
    References:
    ----------

    1.  Default constructor with MapStruct: https://mapstruct.org/documentation/stable/reference/html/#non-shipped-annotations
 */

@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.CLASS)
public @interface Default {
}
