package tech.vault.server.application.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AuthenticationResponseDTO(
        @JsonProperty("access-token") String token
) {
}
