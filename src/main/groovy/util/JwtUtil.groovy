package util

import db.sql.tables.pojos.Userdata
import exception.InvalidTokenException
import groovy.util.logging.Slf4j
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm

@Slf4j
class JwtUtil {
  private String SECRET_KEY = "secret";

  String extractUsername(String token) {
    return extractAllClaims(token).getSubject()
  }

  Date extractExpiration(String token) {
    return extractAllClaims(token).getExpiration()
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody()
    } catch (Exception exception) {
      log.error("Exception occurred: {}", exception.getMessage())
      throw new InvalidTokenException(exception.getMessage())
    }
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date())
  }

  String generateToken(Userdata userDetails) {
    Map<String, Object> claims = new HashMap<String, Object>();
    return createToken(claims, userDetails.getUsername())
  }

  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 100 * 60 * 60 * 10))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }

  Boolean validateToken(String token, String userDetails) {
    final String username = extractUsername(token);
    return (username == userDetails && !isTokenExpired(token));
  }
}
