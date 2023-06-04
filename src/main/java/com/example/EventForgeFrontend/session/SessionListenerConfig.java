package com.example.EventForgeFrontend.session;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SessionListenerConfig implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setMaxInactiveInterval(3600); // 60 minutes
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // Session destroyed logic
    }

    @Bean
    public ServletListenerRegistrationBean<SessionListenerConfig> sessionListener() {
        ServletListenerRegistrationBean<SessionListenerConfig> listenerBean =
                new ServletListenerRegistrationBean<>();
        listenerBean.setListener(this);
        return listenerBean;
    }

}
