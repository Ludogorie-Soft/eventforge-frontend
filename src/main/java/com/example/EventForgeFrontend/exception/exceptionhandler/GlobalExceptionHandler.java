package com.example.EventForgeFrontend.exception.exceptionhandler;

import com.example.EventForgeFrontend.client.AuthenticationApiClient;
import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import com.example.EventForgeFrontend.exception.*;
import com.example.EventForgeFrontend.session.SessionManager;
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
    private AuthenticationApiClient authenticationApiClient;

    @ExceptionHandler(InvalidUserCredentialException.class)
    public ModelAndView handleInvalidUserCredentialException(InvalidUserCredentialException e, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String error = e.getMessage();
        JWTAuthenticationRequest newLoginRequest = (JWTAuthenticationRequest) request.getAttribute("newLoginRequest");
        request.removeAttribute("newLoginRequest");
        mav.addObject("login", newLoginRequest);
        mav.addObject("errorMessage", error);
        mav.setViewName("login");

        return mav;
    }

    @ExceptionHandler(CustomValidationErrorException.class)
    public ModelAndView handleCustomValidationErrorException(CustomValidationErrorException e, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("registerOrganisation");
        Map<String, String> errors = e.getErrors();
        for (Map.Entry<String, String> error : errors.entrySet()) {
            mav.addObject(error.getKey(), error.getValue());
            log.info("field name: " + error.getKey());
            log.info("error message: " + error.getValue());
        }
        Object newRegistrationRequest = getAttributeAsType(request  , "newRegistrationRequest" ,RegistrationRequest.class);
        if (newRegistrationRequest != null) {
            mav.addObject("request", newRegistrationRequest);
        }

        Object organisationPriorities =getAttributeAsType(request, "organisationPriorities" , Set.class);
        if (organisationPriorities != null) {
            mav.addObject("priorityCategories", organisationPriorities);
        }


        request.removeAttribute("newRegistrationRequest");
        request.removeAttribute("organisationPriorities");
        mav.setViewName("registerOrganisation");
        return mav;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ModelAndView handleException(EmailAlreadyExistsException ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String error = ex.getMessage();

        Object newRegistrationRequest = getAttributeAsType(request , "newRegistrationRequest" , RegistrationRequest.class);
        if (newRegistrationRequest != null) {
            mav.addObject("request", newRegistrationRequest);
        }
        Object organisationPriorities =getAttributeAsType(request, "organisationPriorities" , Set.class);
        if (organisationPriorities != null) {
            mav.addObject("priorityCategories", organisationPriorities);
        }

        request.removeAttribute("newRegistrationRequest");
        request.removeAttribute("organisationPriorities");
        mav.addObject("emailTakenError", error);
        mav.setViewName("registerOrganisation");
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleForbiddenException(AccessDeniedException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        log.info(ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
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
            String token = (String) request.getSession().getAttribute("sessionToken");
            sessionManager.invalidateSession(request);
            authenticationApiClient.logout(token);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            mav.setViewName("redirect:/login");
            return mav;
        }

    }

    @SuppressWarnings("unchecked")
    private <T> T getAttributeAsType(HttpServletRequest request, String attributeName, Class<T> targetType) {
        Object attribute = request.getAttribute(attributeName);
        if (targetType.isInstance(attribute)) {
            return (T) attribute;
        }
        return null;
    }

}
