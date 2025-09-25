package thm.gromokoso.usermanagement.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import thm.gromokoso.usermanagement.security.TokenProvider;

import javax.security.sasl.AuthenticationException;
import java.util.List;

@Component
public class McpManagementClient {

    private final TokenProvider tokenProvider;
    private final RestClient client;
    private final String baseUrl;

    public McpManagementClient(TokenProvider tokenProvider,
                               @Value("${spring.subservices.mcp-management.url}") String baseUrl) {
        this.tokenProvider = tokenProvider;
        this.baseUrl = baseUrl;
        this.client = RestClient.builder().baseUrl(baseUrl).build();
    }

    public void notifyAboutChangedToolSets(String username, List<Integer> apiIds) {
        try {
            var response = client.post()
                    .uri("/users/{id}/toolsets/list-changed", username)
                    .header("Authorization", "Bearer " + tokenProvider.getToken())
                    .body(apiIds)
                    .retrieve();
            System.out.println("Client Class: Notify About Changed Tool Sets: " + response.toEntity(String.class).getStatusCode());
        } catch (AuthenticationException ae) {
            System.out.println("Client Class: Notify About Changed Tool Sets AUTHENTIFICATION ERROR: " + ae);
        } catch (Exception e) {
            System.out.println("Client Class: Notify About Changed Tool Sets STANDARD ERROR: " + e);
        }
    }
}
