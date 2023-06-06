package com.example.EventForgeFrontend.exception.exceptionhandler;

import com.example.EventForgeFrontend.client.AuthenticationApiClient;
import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import com.example.EventForgeFrontend.exception.*;
import com.example.EventForgeFrontend.session.SessionManager;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ErrorDecoder errorDecoder;

    @Autowired
    private AuthenticationApiClient authenticationApiClient;

    @ExceptionHandler(InvalidUserCredentialException.class)
    public ModelAndView handleInvalidUserCredentialException(InvalidUserCredentialException e, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String error = e.getMessage();
        JWTAuthenticationRequest newLoginRequest = (JWTAuthenticationRequest) request.getAttribute("newLoginRequest");
        request.removeAttribute("newLoginRequest");
        mav.addObject("login" , newLoginRequest);
        mav.addObject("errorMessage" , error);
        mav.setViewName("login");

        return mav;
    }

    @ExceptionHandler(CustomValidationErrorException.class)
    public ModelAndView handleCustomValidationErrorException(CustomValidationErrorException e, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Map<String , String> errors = e.getErrors();
        for (Map.Entry<String, String> error : errors.entrySet()){
            String fieldName = error.getKey();
            String message = error.getValue();
            mav.addObject(fieldName , message);
            log.info("field name: "+fieldName);
            log.info("error message:"+message );
        }
        RegistrationRequest newRegistrationRequest = (RegistrationRequest) request.getAttribute("newRegistrationRequest");
        Set<String> orgPriorities = (Set<String>) request.getAttribute("organisationPriorities");
        request.removeAttribute("newRegistrationRequest");
        request.removeAttribute("organisationPriorities");
        mav.addObject("request" ,newRegistrationRequest);
        mav.addObject("priorityCategories" , orgPriorities);
        mav.setViewName("registerOrganisation");
        return mav;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ModelAndView handleException(EmailAlreadyExistsException ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String error = ex.getMessage();

        RegistrationRequest newRegistrationRequest = (RegistrationRequest) request.getAttribute("newRegistrationRequest");
        Set<String> orgPriorities = (Set<String>) request.getAttribute("organisationPriorities");
        request.removeAttribute("newRegistrationRequest");
        request.removeAttribute("organisationPriorities");
        mav.addObject("request" ,newRegistrationRequest);
        mav.addObject("emailTakenError" , error);
        mav.addObject("priorityCategories" , orgPriorities);
        mav.setViewName("registerOrganisation");
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleForbiddenException(AccessDeniedException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        String error = ex.getMessage();
        log.info(error);
        redirectAttributes.addFlashAttribute("errorMessage", error);
        mav.setViewName("redirect:" + request.getHeader("Referer"));
        return mav;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ModelAndView handleTokenExpiredException(TokenExpiredException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        if (request.getSession().getAttribute("sessionToken") == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Трябва да сте вписани за да получите достъп");
            mav.setViewName("redirect:/index");
            return mav;
        } else {
            String error = ex.getMessage();
            String token = (String) request.getSession().getAttribute("sessionToken");
            sessionManager.invalidateSession(request);
            authenticationApiClient.logout(token);
            redirectAttributes.addFlashAttribute("errorMessage", error);
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
