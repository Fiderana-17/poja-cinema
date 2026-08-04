package com.hei.exo.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {

  @Bean
  public JwtEncoder jwtEncoder(@Value("${app.security.jwt.secret}") String jwtSecret) {
    return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey(jwtSecret)));
  }

  @Bean
  public JwtDecoder jwtDecoder(@Value("${app.security.jwt.secret}") String jwtSecret) {
    return NimbusJwtDecoder.withSecretKey(getSecretKey(jwtSecret))
        .macAlgorithm(MacAlgorithm.HS256)
        .build();
  }

  private SecretKey getSecretKey(String jwtSecret) {
    return new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
  }
}
