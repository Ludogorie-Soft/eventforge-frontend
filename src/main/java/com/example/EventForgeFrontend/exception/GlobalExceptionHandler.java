package com.example.EventForgeFrontend.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ModelAndView handleException(FeignException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        String error = extractCustomErrorMessage(ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", error);
        mav.setViewName("redirect:" + request.getHeader("Referer"));
        return mav;
    }

    private String extractCustomErrorMessage(String fullErrorMessage) {
        String[] parts = fullErrorMessage.split("\\[");
        if (parts.length > 1) {
            String lastPart = parts[parts.length - 1];
            return lastPart.substring(0, lastPart.length() - 1);
        }
        return fullErrorMessage;
    }
}
