package com.example.EventForgeFrontend.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SessionManager {

    private   String storeSessionUserRole;

    public void setSessionToken(HttpServletRequest request, String sessionToken, String userRole) {
        String token = "Bearer " + sessionToken;
        storeSessionUserRole = userRole;

        HttpSession session = request.getSession(true);
        session.setAttribute("sessionToken", token);
        session.setAttribute("sessionUserRole", userRole);
    }

    public void invalidateSession(HttpServletRequest request ){
        HttpSession session = request.getSession(false);
        if(session!= null){
            session.invalidate();
        }
    }

}
