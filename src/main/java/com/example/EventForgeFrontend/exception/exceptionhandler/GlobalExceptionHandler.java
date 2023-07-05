package com.example.EventForgeFrontend.exception.exceptionhandler;

import com.example.EventForgeFrontend.client.AuthenticationApiClient;
import com.example.EventForgeFrontend.dto.*;
import com.example.EventForgeFrontend.exception.*;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private AuthenticationApiClient authenticationApiClient;

    @ExceptionHandler(CustomValidationErrorException.class)
    public ModelAndView handleCustomValidationErrorException(CustomValidationErrorException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = assembleModelAndView(request);

        Map<String, String> errors = e.getErrors();
        for (Map.Entry<String, String> error : errors.entrySet()) {
            redirectAttributes.addFlashAttribute(error.getKey(), error.getValue());
            log.info("field name: " + error.getKey());
            log.info("error message: " + error.getValue());
        }
        Object newRegistrationRequest = getAttributeAsType(request, "newRegistrationRequest", RegistrationRequest.class);
        Object newUpdateRequest = getAttributeAsType(request, "updateRequest", UpdateAccountRequest.class);
        Object allPriorities = getAttributeAsType(request, "allPriorities", Set.class);
        Object logo = getAttributeAsType(request, "logo", MultipartFile.class);
        Object cover = getAttributeAsType(request, "cover", MultipartFile.class);

        redirectAttributes.addFlashAttribute("logoFile", logo);
        redirectAttributes.addFlashAttribute("backgroundCoverFile", cover);

        Object organisationPriorities = getAttributeAsType(request, "organisationPriorities", Set.class);
        redirectAttributes.addFlashAttribute("priorityCategories", organisationPriorities);
        redirectAttributes.addFlashAttribute("allPriorities", allPriorities);


        request.removeAttribute("updateRequest");
        request.removeAttribute("newRegistrationRequest");
        request.removeAttribute("organisationPriorities");
        request.removeAttribute("allPriorities");
        request.removeAttribute("logoFile");
        request.removeAttribute("backgroundCoverFile");
        if (newRegistrationRequest != null) {
            redirectAttributes.addFlashAttribute("request", newRegistrationRequest);
        }
        if (newUpdateRequest != null) {
            redirectAttributes.addFlashAttribute("updateRequest", newUpdateRequest);
        }
        return mav;
    }

    @ExceptionHandler(ImageException.class)
    public ModelAndView handleImageException(ImageException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        Object newRegistrationRequest = getAttributeAsType(request, "newRegistrationRequest", RegistrationRequest.class);
        Object organisationPriorities = getAttributeAsType(request, "organisationPriorities", Set.class);
        EventRequest newEventRequest = getAttributeAsType(request , "eventRequest" , EventRequest.class);


        redirectAttributes.addFlashAttribute("priorityCategories", organisationPriorities);


        request.removeAttribute("newRegistrationRequest");
        request.removeAttribute("priorityCategories");
        request.removeAttribute("eventRequest");
        redirectAttributes.addFlashAttribute("fileError", e.getMessage());
        redirectAttributes.addFlashAttribute("request", newRegistrationRequest);
        redirectAttributes.addFlashAttribute("eventRequest" , newEventRequest);
        mav.setViewName("redirect:" + request.getHeader("Referer"));
        return mav;
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ModelAndView handleMaxUploadSizeExceededException(FileSizeLimitExceededException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        redirectAttributes.addFlashAttribute("fileError", "Файлът не може да надвишава 5МБ!");
        mav.setViewName("redirect:" + request.getHeader("Referer"));
        return mav;

    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ModelAndView handleException(EmailAlreadyExistsException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = assembleModelAndView(request);
        String error = ex.getMessage();

        Object newRegistrationRequest = getAttributeAsType(request, "newRegistrationRequest", RegistrationRequest.class);


        redirectAttributes.addFlashAttribute("request", newRegistrationRequest);

        Object organisationPriorities = getAttributeAsType(request, "organisationPriorities", Set.class);

        redirectAttributes.addFlashAttribute("priorityCategories",organisationPriorities);

        request.removeAttribute("newRegistrationRequest");
        request.removeAttribute("organisationPriorities");

        redirectAttributes.addFlashAttribute("emailTakenError" , error);

        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleForbiddenException(AccessDeniedException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = assembleModelAndView(request);
        log.info(ex.getMessage());
        redirectAttributes.addFlashAttribute("accessDenied", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ModelAndView handleTokenExpiredException(TokenExpiredException ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("login", new JWTAuthenticationRequest());
        String sessionToken = (String) request.getSession().getAttribute("sessionToken");
        if (sessionToken != null) {
            mav.addObject("errorMessage", "Сесията Ви е изтекла , моля впишете се отново");
            sessionManager.invalidateSession(request);
            authenticationApiClient.logout(sessionToken);
            return mav;
        }
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler({EmailConfirmationNotSentException.class , InvalidEmailConfirmationLinkException.class})
    public ModelAndView emailConfirmationNotSentException(Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex.getMessage());
        mav.setViewName("login");
        return mav;
    }

    @ExceptionHandler({UserDisabledException.class, InvalidUserCredentialException.class, UserLockedException.class})
    public ModelAndView handleUserDisabledException(Exception ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = assembleModelAndView(request);
        JWTAuthenticationRequest newLoginRequest = (JWTAuthenticationRequest) request.getAttribute("newLoginRequest");
        redirectAttributes.addFlashAttribute("login", newLoginRequest);
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        request.removeAttribute("newLoginRequest");
        return mav;
    }


    @ExceptionHandler(DateTimeException.class)
    public ModelAndView handleDateTimeException(DateTimeException ex, HttpServletRequest request , RedirectAttributes redirectAttributes) {
        ModelAndView mav = assembleModelAndView(request);
        redirectAttributes.addFlashAttribute("dateTimeException" , ex.getMessage());
        return mav;
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ModelAndView passwordsNotMatchException(PasswordNotMatchException ex, HttpServletRequest request , RedirectAttributes redirectAttributes) {
        ModelAndView mav = assembleModelAndView(request);

        redirectAttributes.addFlashAttribute("passwordError" , ex.getMessage());

        return mav;
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttributeAsType(HttpServletRequest request, String attributeName, Class<T> targetType) {
        Object attribute = request.getAttribute(attributeName);
        if (targetType.isInstance(attribute)) {
            return (T) attribute;
        }
        return null;
    }

    private ModelAndView assembleModelAndView(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:" + request.getHeader("Referer"));
        return mav;
    }

}
