package projectbusan.gongda.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "group_tbl")
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_idx")
    private Long id;

    @Column(length= 127,name= "group_name",nullable = false)
    private String name;

    @Column(length= 11,name= "group_code",nullable = false)
    private String code;
    @Column(name = "group_password", nullable = false)
    private String password;


    @Column(name = "created_at", nullable = false) @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false) @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> userGroups = new ArrayList<>();




}
