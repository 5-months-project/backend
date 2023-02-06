package projectbusan.gongda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_tbl")
@Data
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

    @OneToMany(mappedBy = "user")
    private List<UserGroup> userGroups = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_idx", referencedColumnName = "user_idx")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    public User(String subject, String s, Collection<? extends GrantedAuthority> authorities) {
    }
}
