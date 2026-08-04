package com.hei.exo.security;

import com.hei.exo.model.User;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final JwtEncoder jwtEncoder;
  private final String issuer;
  private final long expirationInMinutes;

  public JwtService(
      JwtEncoder jwtEncoder,
      @Value("${app.security.jwt.issuer}") String issuer,
      @Value("${app.security.jwt.expiration-in-minutes}") long expirationInMinutes) {
    this.jwtEncoder = jwtEncoder;
    this.issuer = issuer;
    this.expirationInMinutes = expirationInMinutes;
  }

  public String generateToken(User user) {
    Instant now = Instant.now();

    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer(issuer)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expirationInMinutes * 60))
            .subject(user.getEmail())
            .claim("userId", user.getId().toString())
            .claim("role", user.getRole().name())
            .build();

    JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

    return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
  }
}
