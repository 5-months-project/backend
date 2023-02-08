package projectbusan.gongda.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleModifyDTO {
    private String neighbor_code;
    private String name;
    private String content;
    private String category;
}
