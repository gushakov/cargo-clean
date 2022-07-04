package com.github.cargoclean.core.validator;

public interface Validator {

    <T> T validate(T toBeValidated);
}
