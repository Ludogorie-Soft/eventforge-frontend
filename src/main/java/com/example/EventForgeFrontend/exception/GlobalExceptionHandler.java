package com.example.EventForgeFrontend.exception;

import com.example.EventForgeFrontend.client.AuthenticationApiClient;
import com.example.EventForgeFrontend.session.SessionManager;
import feign.FeignException;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ErrorDecoder errorDecoder;

    @Autowired
    private AuthenticationApiClient authenticationApiClient;



    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ModelAndView handleException(EmailAlreadyExistsException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
            ModelAndView mav = new ModelAndView();
            String error = extractCustomErrorMessage(ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", error);
            mav.setViewName("redirect:" + request.getHeader("Referer"));
            return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleForbiddenException(AccessDeniedException ex , HttpServletRequest request , RedirectAttributes redirectAttributes){
        ModelAndView mav = new ModelAndView();
       String error = ex.getMessage();
       log.info(error);
        redirectAttributes.addFlashAttribute("errorMessage" , error);
        mav.setViewName("redirect:" + request.getHeader("Referer"));
        return mav;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ModelAndView handleTokenExpiredException(TokenExpiredException ex , HttpServletRequest request , RedirectAttributes redirectAttributes){
        ModelAndView mav = new ModelAndView();
        if(request.getSession().getAttribute("sessionToken")==null){
            redirectAttributes.addFlashAttribute("errorMessage" , "Трябва да сте вписани за да получите достъп");
            mav.setViewName("redirect:/index");
            return mav;
        }else {
            String error = ex.getMessage();
            String token =(String) request.getSession().getAttribute("sessionToken");
            sessionManager.invalidateSession(request);
            authenticationApiClient.logout(token);
            redirectAttributes.addFlashAttribute("errorMessage" , error);
            mav.setViewName("redirect:/login");
            return mav;
        }

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
