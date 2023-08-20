package com.ptithcm.onlinetest.registration;

import com.ptithcm.onlinetest.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final String appUrl;
    private final Locale locale;
    private final User user;


    public OnRegistrationCompleteEvent(String appUrl, Locale locale, User user) {
        super(user);
        this.appUrl = appUrl;
        this.locale = null;
        this.user = user;
    }


    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public User getUser() {
        return user;
    }
}
