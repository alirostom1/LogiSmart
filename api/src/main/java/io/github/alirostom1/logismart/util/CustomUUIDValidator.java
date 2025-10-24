package io.github.alirostom1.logismart.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class CustomUUIDValidator implements ConstraintValidator<CustomValidUUID, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        if(value == null || value.trim().isEmpty()){
            return true; //LEAVE TO @NOTBLANK TO HANDLE
        }
        try{
            UUID.fromString(value);
            return true;
        }catch(IllegalArgumentException e){
            return false;
        }
    }
}
