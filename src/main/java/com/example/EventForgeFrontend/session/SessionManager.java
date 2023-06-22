package com.example.EventForgeFrontend.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SessionManager {

    private static String storeSessionToken;

    public static String storeSessionUserRole;

    public void setSessionToken(HttpServletRequest request, String sessionToken , String userRole) {
        String token = "Bearer "+sessionToken;
        storeSessionToken = token;
        storeSessionUserRole = userRole;

        HttpSession session = request.getSession(true);
        session.setAttribute("sessionToken", token);
        session.setAttribute("sessionUserRole" , userRole);
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

    public void isSessionExpired(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){
            resetSession(request);
        }
    }
}

