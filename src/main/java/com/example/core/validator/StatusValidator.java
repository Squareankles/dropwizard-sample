package com.example.core.validator;

import com.example.core.exception.ValidStatusException;
import com.example.core.model.StatusValidationHolder;

public class StatusValidator implements Validator<StatusValidationHolder> {

  @Override
  public void validate(StatusValidationHolder value) {

    if (value.getCurrent() != value.getExpected()) {
      throw new ValidStatusException();
    }
  }

}
