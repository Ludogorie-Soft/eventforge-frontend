package com.example.EventForgeFrontend.exception.container;

import com.example.EventForgeFrontend.exception.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static java.util.Map.entry;

public class ExceptionMapperContainer {

    public static final Map<Class<? extends RuntimeException>, Integer> exceptionResponse = Map.ofEntries(
            entry(AccessDeniedException.class, HttpServletResponse.SC_FORBIDDEN),
            entry(DateTimeException.class, HttpServletResponse.SC_SEE_OTHER),
            entry(EmailConfirmationNotSentException.class, HttpServletResponse.SC_EXPECTATION_FAILED),
            entry(EventRequestException.class, HttpServletResponse.SC_NO_CONTENT),
            entry(ImageException.class, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE),
            entry(InvalidEmailConfirmationLinkException.class, HttpServletResponse.SC_BAD_REQUEST),
            entry(InvalidUserCredentialException.class, HttpServletResponse.SC_NOT_FOUND),
            entry(OrganisationRequestException.class, HttpServletResponse.SC_GONE),
            entry(TokenExpiredException.class, HttpServletResponse.SC_NOT_ACCEPTABLE),
            entry(UserDisabledException.class, HttpServletResponse.SC_SERVICE_UNAVAILABLE),
            entry(UserLockedException.class, HttpStatus.LOCKED.value()),
            entry(UsernameNotFoundException.class , 321)
    );
}
