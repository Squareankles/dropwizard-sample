package com.example.core.validator;

import com.example.core.exception.ValidStatusException;

public interface Validator<V> {

  void validate(V value) throws ValidStatusException;

}
