package rm.tabou2.facade.spring.config.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rm.tabou2.storage.dao.RefreshTokenDao;
import rm.tabou2.storage.entity.RefreshToken;
import rm.tabou2.facade.exception.RefreshTokenExpiredException;
import rm.tabou2.service.items.Tokens;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * Classe utilitaire des gestion de token JWT
 */
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Autowired
    RefreshTokenDao refreshTokenDao;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.validity}")
    private int tokenValidity;

    @Value("${jwt.refresh.validity}")
    private int refreshTokenValidity;

    public final String HEADER_TOKEN_JWT_AUTHENT_KEY = "Authorization";

    public final String HEADER_TOKEN_JWT_PREFIX = "Bearer ";

    private final String CONNECTED_USER = "connectedUser";

    /**
     * Generation d'un token pour un utilisateur
     *
     * @param accountUid id de l'iutilisateur
     * @return
     */
    private String generateJwtToken(final String accountUid, final Object connectedUser) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(CONNECTED_USER, connectedUser);
        return doGenerateToken(claims, accountUid, tokenValidity);
    }

    /**
     * Generation d'un token de refresh pour un utilisateur
     * un tokeb de refresh permet de redemander un token
     *
     * @param accountUid
     * @return
     */
    private String generateRefreshToken(final String accountUid, final Object connectedUser) {

        // Génération d'un token
        final Map<String, Object> claims = new HashMap<>();
        claims.put(CONNECTED_USER, connectedUser);
        final String token = doGenerateToken(claims, accountUid, refreshTokenValidity);

        // Lors de l'ajout d'un nouveau token de refresh,on purge la table des token pour supprimer les token expirés
        this.purgeRefreshToken();

        // Ajout du nouveau token en bdd
        final RefreshToken rToken = new RefreshToken();
        rToken.setToken(token);
        refreshTokenDao.save(rToken);

        return token;
    }

    /**
     * Retourne le nom d'utilisateur associé au token
     *
     * @param token
     * @return
     */
    public String getUuidFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * récupération une propriété d'un token
     *
     * @param token
     * @return
     */
    public <T> T getTokenProperty(final String token, final String propertyName) {
        return (T) getAllClaimsFromToken(token).get(propertyName);
    }

    /**
     * Retourne la date d'expiration du token
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(final String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Genration du token JWT et du token de refresh
     *
     * @param accountLogin
     * @return
     */
    public Tokens generateTokens(final String accountLogin, final Object connectedUser) {

        // Génération du token d'authentification et de refesh
        final Tokens tk = new Tokens();
        tk.setJwtToken(HEADER_TOKEN_JWT_PREFIX + this.generateJwtToken(accountLogin, connectedUser));
        tk.setRefreshToken(HEADER_TOKEN_JWT_PREFIX + this.generateRefreshToken(accountLogin, connectedUser));
        return tk;
    }


    /**
     * Retourne les informations contenu dans un token
     *
     * @param token          token jwt
     * @param claimsResolver Nom de la propriété
     * @param <T>
     * @return
     */
    private <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * récupération de toutes les propriétes d'un token
     *
     * @param token
     * @return
     */
    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Détermine si le token est expiré
     *
     * @param token
     * @return
     */
    private Boolean isTokenExpired(final String token) {
        try {
            // la récupération de la date retourne une exection si le token est déjà expiré
            getExpirationDateFromToken(token);
            return false;
        } catch (final ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Controle de l'existance du token de refresh dans la référentiel de l'application
     *
     * @param token
     * @return
     */
    private boolean checkRefreshToken(final String token) {

        final RefreshToken rToken = refreshTokenDao.findFirstByTokenEquals(token);
        if (rToken != null) {

            // Suppression du token dans la bdd car usage unique
            refreshTokenDao.delete(rToken);

            return true;
        } else {
            return false;
        }
    }


    /**
     * permet de purger les token de refresh dont la date est expiré
     */
    private void purgeRefreshToken() {

        final List<RefreshToken> tokenList = refreshTokenDao.findAll();
        for (final RefreshToken tokenData : tokenList) {
            if (this.isTokenExpired(tokenData.getToken())) {
                refreshTokenDao.delete(tokenData);
            }
        }
    }

    /**
     * Generation d'un token
     *
     * @param claims   Issuer, Expiration, Subject, and the ID
     * @param subject
     * @param validity validity in seconde
     * @return
     */
    private String doGenerateToken(final Map<String, Object> claims, final String subject, final int validity) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Generation de nouveau token à partir du refresh token
     *
     * @param refreshToken
     * @return
     */
    public Tokens generateNewJwtTokens(final String refreshToken) throws RefreshTokenExpiredException {

        // Récupération des données du token de refresh
        final JwtTokenData refreshJtd = this.validateToken(refreshToken);

        // Vérification si le token existe dans le référentiel de l'application (en bdd)
        final boolean refreshToKenExist = this.checkRefreshToken(refreshJtd.getToken());

        // Si le token de refresh existe et qu'il n'est pas expiré
        if (refreshToKenExist && !refreshJtd.isHasError() && !refreshJtd.isExpired()) {

            // Generation de nouveaux token (jwt et refresh)
            return this.generateTokens(refreshJtd.getName(), refreshJtd.getAccount());

        } else {
            // Exception car le tocken de refresh n'est pas valide
            throw new RefreshTokenExpiredException();
        }
    }

    /**
     * Récupération des données du token tout en le validant
     *
     * @param requestToken token
     * @return
     */
    public JwtTokenData validateToken(final String requestToken) {

        final JwtTokenData token = new JwtTokenData();

        // Contrôle du préfixe dans la requete
        if (requestToken == null || !requestToken.startsWith(this.HEADER_TOKEN_JWT_PREFIX)) {
            LOG.error("Le token ne commence pas avec la chaine Bearer");
            token.setHasError(true);
        } else {

            final String tokenJwt = requestToken.substring(this.HEADER_TOKEN_JWT_PREFIX.length());
            token.setToken(tokenJwt);

            try {
                token.setName(this.getUuidFromToken(tokenJwt));
                token.setAccount(this.getTokenProperty(tokenJwt, CONNECTED_USER));
            } catch (final IllegalArgumentException ex) {
                LOG.error("impossible de récupérer le token JWT");
                token.setHasError(true);

            } catch (final ExpiredJwtException ex) {
                LOG.error("Token JWT expiré");
                token.setExpired(true);

            } catch (final MalformedJwtException ex) {
                LOG.error("Erreur de formatage du token JWT");
                token.setHasError(true);
            }
        }

        return token;

    }
}