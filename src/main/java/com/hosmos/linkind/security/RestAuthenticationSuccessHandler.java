package com.hosmos.linkind.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
//        TODO save user history or manage roles

        logger.trace("START...");
        SavedRequest savedRequest = requestCache.getRequest(request, response);


        if (savedRequest == null) {
            logger.trace("SAVED_REQUEST_IS_NULL.");
            clearAuthenticationAttributes(request);
            return;
        }

        String targetUrlParam = getTargetUrlParameter();
        logger.trace("TARGET_URL_PARAMETER:" + targetUrlParam);

        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParam != null && StringUtils.hasText(request.getParameter(targetUrlParam)))) {
            requestCache.removeRequest(request, response);
            logger.trace("REMOVE_REQUEST.");
            clearAuthenticationAttributes(request);
            return;
        }

        logger.trace("CLEAR_AUTHENTICATION_ATTRIBUTES.");
        clearAuthenticationAttributes(request);
    }

}
