package thm.gromokoso.usermanagement.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Component
public class TokenProvider {

    public String getToken() throws OAuth2AuthenticationException {
        return getJWTToken().getTokenValue();
    }

    public String getUsernameFromToken() throws OAuth2AuthenticationException  {
        return getJWTToken().getClaim("preferred_username");
    }

    private Jwt getJWTToken() throws OAuth2AuthenticationException  {
        var token = SecurityContextHolder.getContext().getAuthentication();
        if (!(token instanceof JwtAuthenticationToken jwtToken)) {
            throw new OAuth2AuthenticationException ("JWT token is not valid");
        }
        return jwtToken.getToken();
    }
}
