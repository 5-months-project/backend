package projectbusan.gongda.dto;

import jakarta.persistence.*;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.User;

import java.time.LocalDateTime;

public class ScheduleDTO {
    private Long id;
    private String name;
    private String content;
    private LocalDateTime time_start;
    private LocalDateTime time_end;
    private Long creator_id;
    private Long modifier_id;
    private Long group_id;
    private String category;

}
