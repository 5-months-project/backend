package domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user_tbl")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long id;

    @Column(name = "user_email", length = 127, unique = true)
    private String email;

    @Column(name = "user_nickname", length = 255)
    private String nickname;

    @Column(name = "user_password", length = 255)
    private String password;

    @Column(name = "user_created_at") @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "user_updated_at") @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "user_activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}
