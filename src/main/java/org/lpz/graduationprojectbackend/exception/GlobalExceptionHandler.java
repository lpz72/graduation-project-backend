package org.lpz.graduationprojectbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.lpz.usercenter.common.BaseResponse;
import org.lpz.usercenter.common.ErrorCode;
import org.lpz.usercenter.common.ResultUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author lpz
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //应用到所有@RequestMapping注解的方法，在其抛出Exception异常时执行
    //定义全局异常处理，value属性可以过滤拦截指定异常，此处拦截所有的Exception
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException:" + e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler({RuntimeException.class})
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
