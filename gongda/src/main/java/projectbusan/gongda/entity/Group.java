package projectbusan.gongda.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group_tbl")
public class Group extends BaseTimeStamp{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_idx")
    private Long id;

    @Column(length= 127,name= "group_name",nullable = false)
    private String name;

    @Column(length= 11,name= "group_code",nullable = false)
    private String code;
    @Column(name = "group_password", nullable = false)
    private String password;

    @Builder.Default
    @ManyToMany(mappedBy = "groupList" ,cascade = CascadeType.ALL)
    private List<User> userList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)

    private List<Schedule> schedules = new ArrayList<>();

    public void addSchedule(Schedule schedule){
        if (!this.getSchedules().contains(schedule)) this.getSchedules().add(schedule);
    }

    public void deleteSchedule(Schedule schedule){
        if (this.getSchedules().contains(schedule)) this.getSchedules().remove(schedule);
    }


}
