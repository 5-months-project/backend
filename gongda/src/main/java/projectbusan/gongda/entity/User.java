package projectbusan.gongda.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_tbl")
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeStamp {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long id;

    @Column(name = "user_email", length = 127, unique = true, nullable = false)
    private String username;

    @Column(name = "user_nickname", length = 255)
    private String nickname;

    @Column(name = "user_password", length = 255, nullable = false)
    private String password;

    @Column(name = "user_activated", nullable = false)
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_group_tbl",
            joinColumns = @JoinColumn(name = "user_idx"),
            inverseJoinColumns = @JoinColumn(name = "group_idx")
    )

    @Builder.Default
    private List<Group> groupList = new ArrayList<>();
    public void addGroup(Group group){
        this.getGroupList().add(group);
        group.getUserList().add(this);
    }

    public void deleteGroup(Group group){
        this.getGroupList().remove(group);
        group.getUserList().remove(this);
    }

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_idx", referencedColumnName = "user_idx")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    public User(String subject, String s, Collection<? extends GrantedAuthority> authorities) {
    }
}
