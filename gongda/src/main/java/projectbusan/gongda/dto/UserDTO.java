package projectbusan.gongda.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import projectbusan.gongda.entity.User;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotNull @Size(min = 4, max = 127)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull @Size(min= 4, max = 255)
    private String password;

    private Set<AuthorityDTO> authorityDTOSet;

    public static UserDTO from(User user) {
        if(user == null) return null;

        return UserDTO.builder()
                .username(user.getUsername())
                .authorityDTOSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDTO.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
