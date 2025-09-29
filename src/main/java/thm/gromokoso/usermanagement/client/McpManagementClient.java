package thm.gromokoso.usermanagement.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import thm.gromokoso.usermanagement.security.TokenProvider;

import java.util.List;

@Component
public class McpManagementClient {

    private final TokenProvider tokenProvider;
    private final RestClient client;
    private final String baseUrl;

    Logger logger = LoggerFactory.getLogger(McpManagementClient.class);

    public McpManagementClient(TokenProvider tokenProvider,
                               @Value("${spring.subservices.mcp-management.url}") String baseUrl) {
        this.tokenProvider = tokenProvider;
        this.baseUrl = baseUrl;
        this.client = RestClient.builder().baseUrl(baseUrl).build();
    }

    public void notifyAboutChangedToolSets(String username, List<Integer> apiIds) {
        try {
            logger.info("====== Start notifying Mcp Management about changed tool set. ======");
            var response = client.post()
                    .uri("/users/{id}/toolsets/list-changed", username)
                    .header("Authorization", "Bearer " + tokenProvider.getToken())
                    .body(apiIds)
                    .retrieve();
            logger.info("====== Ending notifying Mcp Management about changed tool set. Response: {} ======",
                    response.toEntity(String.class).getStatusCode());
        } catch (OAuth2AuthenticationException ae) {
            logger.error("Ending notifying Mcp Management about changed tool set because of AUTHENTICATION ERROR: {}",
                    ae.getMessage());
        } catch (Exception e) {
            logger.error("Ending notifying Mcp Management about changed tool set because of OTHER ERROR: {}",
                    e.getMessage());
        }
    }
}
