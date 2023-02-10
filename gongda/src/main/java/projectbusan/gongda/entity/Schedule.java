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

    @Column(columnDefinition = "LONGTEXT", name= "schedule_content",nullable = false)
    private String content;

    @Column(length = 8,name= "schedule_date",nullable = false)
    private Long date;

    @Column(length = 12,name= "schedule_time_from",nullable = false)
    private Long timeStart;

    @Column(length = 12,name= "schedule_time_to",nullable = false)
    private Long timeEnd;

    @Column(name = "schedule_creator_email",nullable = false)
    private String creator;//이메일

    @Column(name = "schedule_modifier_email",nullable = false)
    private String modifier;

    @Column(name = "schedule_group_code")
    private String groupcode;

    @Column(name = "schedule_category")
    private String category;

    @Column(length= 11,name= "schedule_code",nullable = false)
    private String scheduleCode;













}
