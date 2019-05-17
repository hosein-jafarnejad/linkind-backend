package ir.projects.learner.security;

import ir.projects.learner.dao.DAOUser;
import ir.projects.learner.model.himodels.user.HiLoginHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private AuthorityManager authorityManager;

    @Autowired
    public void setAuthorityManager(AuthorityManager authorityManager) {
        this.authorityManager = authorityManager;
    }

    private DAOUser daoUser;

    @Autowired
    public void setUserService(DAOUser daoUser) {
        this.daoUser = daoUser;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        logger.info("Save login history.[USER_ID:" + authenticatedUser.getId() + "]");
        daoUser.saveLoginHistory(new HiLoginHistory(authenticatedUser.getId()));

        logger.info("LOADING_ROLE_AND_PRIVILEGES.");
        authorityManager.initial(authenticatedUser.getPrivileges());

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

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

    public RequestCache getRequestCache() {
        return requestCache;
    }
}
