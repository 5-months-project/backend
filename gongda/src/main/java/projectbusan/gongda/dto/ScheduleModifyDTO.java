package projectbusan.gongda.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleModifyDTO {
    private String schedule_code;
    private String name;
    private String content;
    private String category;
    private Long time_start;
    private Long time_end;
}
