package connecticus.in.quiz.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements IJwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);
    private final Key SECRET_KEY = getSigning();

    // generating token
    public String generateToken(UserDetails userDetails) {
        logger.info("Generating token for user: {}", userDetails.getUsername());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // extracting userName
    public String extractUserName(String token) {
        logger.info("Extracting username from token");
        return extractClaims(token, Claims::getSubject);
    }

    // extraction claims
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        logger.info("Extracting claims from token");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        logger.info("Parsing and extracting all claims from token");
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // signing key
    private Key getSigning() {
        logger.info("Generating signing key");
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // checking expiration
    public Date getExpirationDateFromToken(String token) {
        logger.info("Getting expiration date from token");
        return extractClaims(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        logger.info("Checking if token is expired");
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // validation token
    public Boolean validateToken(String token, UserDetails userDetails) {
        logger.info("Validating token for user: {}", userDetails.getUsername());
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
