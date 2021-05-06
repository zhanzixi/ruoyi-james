package com.cbest.ruoyijames.config;

import com.cbest.ruoyijames.common.BusinessException;
import com.cbest.ruoyijames.common.model.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author James
 * @date 2021/4/21 15:36
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler
    public AjaxResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public AjaxResult handleBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        if (e.getCode() != null) {
            return AjaxResult.error(e.getCode(), e.getMessage());
        }
        return AjaxResult.error(e.getMessage());
    }
}
