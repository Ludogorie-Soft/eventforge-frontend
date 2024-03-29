package com.example.EventForgeFrontend.exception.exceptionhandler;

import com.example.EventForgeFrontend.dto.EventRequest;
import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import com.example.EventForgeFrontend.dto.UpdateAccountRequest;
import com.example.EventForgeFrontend.exception.*;
import com.example.EventForgeFrontend.session.SessionManager;
import com.example.EventForgeFrontend.slack.SlackNotifier;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    private SessionManager sessionManager;

   @Autowired
   private SlackNotifier slackNotifier;

    @ExceptionHandler(Throwable.class)
   public void sendSlackNotificationWhenInternalServerErrorOccurs(Throwable throwable){

        if(!(throwable instanceof HttpRequestMethodNotSupportedException)){
            LocalDateTime timestamp = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            // Create a detailed message including timestamp, exception message, and cause
            String message = String.format(
                    "Exception occurred at %s%nFRONTEND APPLICATION%nMessage: %s%nCause: %s",
                    formatter.format(timestamp),
                    throwable.getMessage(),
                    throwable.getCause()
            );
            slackNotifier.sendNotification(message);
        }
   }

    @ExceptionHandler(CustomValidationErrorException.class )
    public ModelAndView handleCustomValidationErrorException(CustomValidationErrorException validationEx ,HttpServletRequest request, RedirectAttributes redirectAttributes ) {
        ModelAndView mav = assembleModelAndView(request);

            Map<String, String> errors = validationEx.getErrors();
            for (Map.Entry<String, String> error : errors.entrySet()) {
                redirectAttributes.addFlashAttribute(error.getKey(), error.getValue());
                log.info("field name: " + error.getKey());
                log.info("error message: " + error.getValue());
            }
        RegistrationRequest newRegistrationRequest = getAttributeAsType(request, "newRegistrationRequest", RegistrationRequest.class);
        UpdateAccountRequest newUpdateRequest = getAttributeAsType(request, "updateRequest", UpdateAccountRequest.class);
        Object allPriorities = getAttributeAsType(request, "allPriorities", Set.class);
        EventRequest newEventRequest = getAttributeAsType(request ,"event" ,EventRequest.class);
        Object organisationPriorities = getAttributeAsType(request, "organisationPriorities", Set.class);


        if(newRegistrationRequest!=null){
            redirectAttributes.addFlashAttribute("request", newRegistrationRequest);
            redirectAttributes.addFlashAttribute("result", "Неуспешна регистрация. Моля, проверете изискванията за всички полета.");
            redirectAttributes.addFlashAttribute("priorityCategories", organisationPriorities);
            redirectAttributes.addFlashAttribute("allPriorities", allPriorities);
            request.removeAttribute("newRegistrationRequest");
            request.removeAttribute("organisationPriorities");
            request.removeAttribute("allPriorities");
        }

        if (newUpdateRequest != null) {
            redirectAttributes.addFlashAttribute("updateRequest", newUpdateRequest);
            redirectAttributes.addFlashAttribute("result" ,"Неуспешен опит да редактирате профилът си");
            request.removeAttribute("updateRequest");

        }
        if(newEventRequest!=null){
            redirectAttributes.addFlashAttribute("event" ,newEventRequest);
            redirectAttributes.addFlashAttribute("result" ,"Неуспешен опит.Моля уверете се , че покривате изискванията на всички полета.");
            request.removeAttribute("event");

        }

        return mav;
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ModelAndView handleInvalidPasswordException(InvalidPasswordException ex , HttpServletRequest request , RedirectAttributes redirectAttributes){
        ModelAndView mav = assembleModelAndView(request);
        redirectAttributes.addFlashAttribute("oldPassword" , ex.getMessage());
        return mav;
    }

    @ExceptionHandler(ImageException.class)
    public ModelAndView handleImageException(ImageException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        RegistrationRequest newRegistrationRequest = getAttributeAsType(request, "newRegistrationRequest", RegistrationRequest.class);
        Object organisationPriorities = getAttributeAsType(request, "organisationPriorities", Set.class);
        EventRequest newEventRequest = getAttributeAsType(request , "event" , EventRequest.class);

        if(newRegistrationRequest!=null){
            redirectAttributes.addFlashAttribute("request", newRegistrationRequest);
            redirectAttributes.addFlashAttribute("priorityCategories", organisationPriorities);
            request.removeAttribute("newRegistrationRequest");
            request.removeAttribute("priorityCategories");
        }
        if(newEventRequest!=null){
            redirectAttributes.addFlashAttribute("event" , newEventRequest);
            request.removeAttribute("event");

        }
        redirectAttributes.addFlashAttribute("result" ,"Неуспешен опит.Моля уверете се , че покривате изискванията на всички полета.");
        redirectAttributes.addFlashAttribute("fileError", e.getMessage());
        mav.setViewName("redirect:" + request.getHeader("Referer"));
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
    public ModelAndView handleTokenExpiredException(TokenExpiredException ex, HttpServletRequest request , RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("redirect:/login");
        mav.addObject("login", new JWTAuthenticationRequest());
        String sessionToken = (String) request.getSession().getAttribute("sessionToken");
        if (sessionToken != null) {
            redirectAttributes.addFlashAttribute("result", "Сесията Ви е изтекла , моля впишете се отново");
            sessionManager.invalidateSession(request);
        } else {
            redirectAttributes.addFlashAttribute("result", ex.getMessage());;
        }
        return mav;
    }

    @ExceptionHandler({EmailConfirmationNotSentException.class , InvalidEmailConfirmationLinkException.class})
    public ModelAndView emailConfirmationNotSentException(Exception ex , HttpServletRequest request , RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        redirectAttributes.addFlashAttribute("result" , ex.getMessage());
        mav.setViewName("redirect:/login");
        return mav;
    }

    @ExceptionHandler({UserDisabledException.class, InvalidUserCredentialException.class, UserLockedException.class ,UsernameNotFoundException.class})
    public ModelAndView handleUserDisabledException(Exception ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        JWTAuthenticationRequest newLoginRequest = (JWTAuthenticationRequest) request.getAttribute("newLoginRequest");
        redirectAttributes.addFlashAttribute("login", newLoginRequest);
        redirectAttributes.addFlashAttribute("result", ex.getMessage());
        request.removeAttribute("newLoginRequest");
        mav.setViewName("redirect:/login");
        return mav;
    }


    @ExceptionHandler(EventRequestException.class)
    public ModelAndView handleEventRequestException(EventRequestException e ,RedirectAttributes redirectAttributes , HttpServletRequest request){
        ModelAndView mav = assembleModelAndView(request);
        redirectAttributes.addFlashAttribute("result" , e.getMessage());
        return mav;
    }

    @ExceptionHandler(OrganisationRequestException.class)
    public String handleOrganisationRequestException(OrganisationRequestException ex , Model model){
        model.addAttribute("result" , ex.getMessage());
        return "showOrganisationDetails";
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
