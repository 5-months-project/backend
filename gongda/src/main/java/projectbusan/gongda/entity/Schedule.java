package projectbusan.gongda.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_tbl")
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule extends BaseTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_idx")
    private Long id;

    @Column(name= "schedule_name",nullable = false)
    private String name;

    @Column(length= 11,name= "schedule_content",nullable = false)
    private String content;

    @Column(name= "schedule_time_from",nullable = false)
    private LocalDateTime time_start;

    @Column(name= "schedule_time_to",nullable = false)
    private LocalDateTime time_end;

    @Column(name = "schedule_creator",nullable = false)
    private Long creator_id;

    @Column(name = "schedule_modifier",nullable = false)
    private Long modifier_id;

    @Column(name = "schedule_group_id")
    private Long group_id;

    @Column(name = "schedule_category")
    private String category;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;










}
