package rm.tabou2.facade.spring.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import rm.tabou2.storage.item.ConnectedUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // Controle des patterns des URL
    AntPathMatcher pathMatcher;

    // Liste des URL à exclure
    Collection<String> excludeUrlPatterns;

    public JwtRequestFilter(final String[] excludeUrlPatterns) {
        this.excludeUrlPatterns = Arrays.asList(excludeUrlPatterns);
        pathMatcher = new AntPathMatcher();
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {

        // Request token header
        final String requestAuthentTokenHeader = request.getHeader(jwtTokenUtil.HEADER_TOKEN_JWT_AUTHENT_KEY);

        // Récupération des données du token
        final JwtTokenData authentJtd = jwtTokenUtil.validateToken(requestAuthentTokenHeader);

        // Si le token est expiré
        if (!authentJtd.isHasError() && authentJtd.isExpired()) {

            // Génération d'un code HTTP 489
            LOG.warn("Le token JWT d'authentification à expiré");
            response.setStatus(498);
            return;

        }

        // Validation du token
        else if (authentJtd != null && !authentJtd.isHasError() && authentJtd.getName() != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Récupération de l'utilisateur en bdd
            final ObjectMapper mapper = new ObjectMapper();
            final ConnectedUser cntAccount = mapper.convertValue(authentJtd.getAccount(), ConnectedUser.class);

            // Application des authorités
            final UsernamePasswordAuthenticationToken accountnamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    cntAccount, null, cntAccount.getAuthorities());
            accountnamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(accountnamePasswordAuthenticationToken);

            chain.doFilter(request, response);
        } else {
            // On considère que le token est invalide
            LOG.warn("Le token est invalide");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {

        // Contrôle si l'URL n'est pas dans le liste d'exclusion. Si c'est le cas, elle ne passera pas dans ce filtre
        return excludeUrlPatterns.stream().anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

}
