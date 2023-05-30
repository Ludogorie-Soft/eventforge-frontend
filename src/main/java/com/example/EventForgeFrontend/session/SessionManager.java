package com.example.EventForgeFrontend.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    public void setSessionToken(HttpServletRequest request, String sessionToken) {
        HttpSession session = request.getSession();
        session.setAttribute("sessionToken", sessionToken);
    }

    public void invalidateSession(HttpServletRequest request ){
        HttpSession session = request.getSession(false);
        if(session!= null){
            session.invalidate();
        }
    }
}

