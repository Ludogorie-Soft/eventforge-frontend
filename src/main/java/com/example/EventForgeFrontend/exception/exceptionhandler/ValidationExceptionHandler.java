package com.example.EventForgeFrontend.exception.exceptionhandler;

import com.example.EventForgeFrontend.exception.CustomValidationErrorException;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@ControllerAdvice
@Slf4j
public class ValidationExceptionHandler {

    @Autowired
    private ErrorDecoder decoder;

    @ExceptionHandler(CustomValidationErrorException.class)
    public ModelAndView handleValidationErrorException(CustomValidationErrorException ex , HttpServletRequest request , RedirectAttributes redirectAttributes){
        ModelAndView mav = new ModelAndView();
        List<String> errors = ex.getErrors();
        redirectAttributes.addFlashAttribute("validationError" , ex.getMessage());
        mav.setViewName("redirect:" + request.getHeader("Referer"));
        return mav;

    }
}
