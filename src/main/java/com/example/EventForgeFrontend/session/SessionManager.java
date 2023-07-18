package com.example.EventForgeFrontend.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Getter
public class SessionManager {
    private static final String SESSION_ID_ATTRIBUTE = "uniqueSessionId";
    private static String storeSessionToken;

    public static String storeSessionUserRole;

    public void setSessionToken(HttpServletRequest request, String sessionToken, String userRole) {
        String token = "Bearer " + sessionToken;
        storeSessionToken = token;
        storeSessionUserRole = userRole;

        HttpSession session = request.getSession(true);
        String uniqueSessionId = generateUniqueSessionId(request);
        session.setAttribute(SESSION_ID_ATTRIBUTE, uniqueSessionId);
        session.setAttribute("sessionToken", token);
        session.setAttribute("sessionUserRole", userRole);
    }

    private String generateUniqueSessionId(HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();
        String uniqueInfo = userAgent + ipAddress;

        // Generate UUID based on the unique information
        UUID uuid = UUID.nameUUIDFromBytes(uniqueInfo.getBytes());

        // Convert UUID to string and return it
        return uuid.toString();
    }


    private boolean isSessionIdValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String uniqueSessionId = generateUniqueSessionId(request);
            String storedSessionId = (String) session.getAttribute(SESSION_ID_ATTRIBUTE);
            return uniqueSessionId.equals(storedSessionId);
        }
        return false;
    }

    private void resetSession(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        session.setAttribute("sessionToken" , storeSessionToken);
        session.setAttribute("sessionUserRole" , storeSessionUserRole);
    }

    public void invalidateSession(HttpServletRequest request ){
        HttpSession session = request.getSession(false);
        if(session!= null){
            session.invalidate();
            storeSessionToken = null;
            storeSessionUserRole = null;
        }
    }

    public void isSessionExpired(HttpServletRequest request) {
        if (!isSessionIdValid(request)) {
            resetSession(request);
        }
    }
}

