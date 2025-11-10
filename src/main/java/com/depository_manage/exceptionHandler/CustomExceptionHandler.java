package com.depository_manage.exceptionHandler;

import com.depository_manage.exception.MyException;
import com.depository_manage.pojo.RestResponse;
import com.depository_manage.pojo.StatusInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常捕捉处理
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ResponseBody
    @ExceptionHandler(MyException.class)
    public RestResponse handleMyException(MyException e) {
        return new RestResponse(null, e.getCode(), new StatusInfo(e.getMsg(), e.getMsg()));
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public RestResponse handleException(Exception e) {
        log.error("系统异常", e);
        return new RestResponse(null, 500, new StatusInfo("系统异常", e.getMessage()));
    }
}



