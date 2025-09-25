package thm.gromokoso.usermanagement.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;

@Component
public class TokenProvider {

    public String getToken() throws AuthenticationException {
        return getJWTToken().getTokenValue();
    }

    public String getUsernameFromToken() throws AuthenticationException {
        return (String) getJWTToken().getClaims().get("username");
    }

    private Jwt getJWTToken() throws AuthenticationException {
        var token = SecurityContextHolder.getContext().getAuthentication();
        if (!(token instanceof JwtAuthenticationToken jwtToken)) {
            throw new AuthenticationException("JWT token is not valid");
        }
        return jwtToken.getToken();
    }
}
