package com.github.cargoclean.infrastructure.adapter.map;

/*
    References:
    ----------

    1.  Ignoring methods with MapStruct: https://bit.ly/38T5TfY
 */

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface IgnoreForMapping {
    // use this annotation for methods which should be ignored by MapStruct
}
