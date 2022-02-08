package com.example.core.validator;

import com.example.core.exception.ValidStatusException;

public interface Validator<T> {

  void validate(T value) throws ValidStatusException;

}
