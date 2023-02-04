package projectbusan.gongda.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotNull @Size(min = 4, max = 127)
    private String username;

    @NotNull @Size(min = 4, max = 255)
    private String password;
}
