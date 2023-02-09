package projectbusan.gongda.dto;

import jakarta.persistence.*;
import lombok.*;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.User;

import java.time.LocalDateTime;

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
    private Long creator_id;
    private Long modifier_id;
    private Long group_id;
    private String schedule_code;
    private String category;

}
