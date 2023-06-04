package com.example.EventForgeFrontend.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    private static String storeSessionToken;

    public void setSessionToken(HttpServletRequest request, String sessionToken) {
        String token = "Bearer "+sessionToken;
        storeSessionToken = token;
        HttpSession session = request.getSession(true);
        session.setAttribute("sessionToken", token);
    }

    private void resetSession(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        session.setAttribute("sessionToken" , storeSessionToken);
    }

    public void invalidateSession(HttpServletRequest request ){
        HttpSession session = request.getSession(false);
        if(session!= null){
            session.invalidate();
            storeSessionToken = null;
        }
    }

    public void isSessionExpired(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){
            resetSession(request);
        }
    }
}

