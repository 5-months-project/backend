package projectbusan.gongda.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCreateDTO {
    private Long creator_id;
    private String name;
    private String content;
    private Long time_start;
    private Long time_end;
    private String category;

}
