package projectbusan.gongda.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCreateDTO {
    private String creator_email;
    private String name;
    private String content;
    private Long time_start;
    private Long time_end;
    private String category;

}
