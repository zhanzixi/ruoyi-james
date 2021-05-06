package com.cbest.ruoyijames.common.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author James
 * @date 2021/4/21 14:47
 */
@AllArgsConstructor
@Component
public class ValidatorUtils {

    private final Validator validator;

    public <T> void validate(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, groups);
        if (!violationSet.isEmpty()) {
            String errorMessage = violationSet.stream()
                    .map(cv -> cv.getPropertyPath() + cv.getMessage())
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public <T> void validate(Iterable<T> list, Class<?>... groups) {
        for (T t : list) {
            this.validate(t, groups);
        }
    }
}
