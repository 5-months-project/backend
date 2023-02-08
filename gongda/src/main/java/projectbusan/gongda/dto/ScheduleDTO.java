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
    private Long id;
    private String name;
    private String content;
    private Long date;
    private Long time_start;
    private Long time_end;
    private Long creator_id;
    private Long modifier_id;
    private Long group_id;
    private String code;
    private String category;

}
