package projectbusan.gongda.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {
    private String name;
    private String content;
    private Long time_start;
    private Long time_end;
    private String creator_nickname;
    private String modifier_nickname;
    private String group_code;
    private String schedule_code;
    private String category;

}
