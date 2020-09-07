package rm.tabou2.facade.spring.config.filter;

import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import rm.tabou2.storage.item.ApplicationCustomerData;
import rm.tabou2.service.UtilContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomerDataFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerDataFilter.class);

    @Autowired
    private UtilContext utilContext;

    @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final FilterChain filterChain) throws ServletException, IOException {

        // Récupération des informationd dans l'en-tête de la requete
        final String whoIam = httpServletRequest.getHeader("whoiam");
        final String referer = httpServletRequest.getHeader(HttpHeaders.REFERER);

        if (whoIam == null || whoIam.isEmpty()) {

            // Le header whoiam est invalide
            LOG.warn("Header whoiam invalide");
            //httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Header whoiam invalide");
        }

        // Sauvegarde des informations dans le context
        final ApplicationCustomerData appCustomerData = new ApplicationCustomerData();
        appCustomerData.setApplicationName(whoIam);
        appCustomerData.setApplicationReferrer(referer);
        utilContext.setApplicationCustomer(appCustomerData);

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

}
