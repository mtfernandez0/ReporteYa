package com.cons.reporteya.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.io.IOException;
import java.util.Locale;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final MessageSource messages;

    public CustomAuthFailureHandler(MessageSource messages){
        this.messages = messages;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        Locale locale = RequestContextUtils.getLocale(request);

        String errorMessage = messages.getMessage("message.badCredentials", null, locale);

        if (exception.getMessage().equalsIgnoreCase("User is disabled"))
            errorMessage = messages.getMessage("auth.message.disabled", null, locale);
        else if (exception.getMessage().equalsIgnoreCase("User account has expired"))
            errorMessage = messages.getMessage("auth.message.expired", null, locale);

        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);

        request.getSession().setAttribute("email", request.getParameter("email"));
        response.sendRedirect("/login?error=fail");
    }
}
