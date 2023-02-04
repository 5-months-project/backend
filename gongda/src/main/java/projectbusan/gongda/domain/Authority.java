package projectbusan.gongda.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authority_tbl")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id @Column(name = "authority_name", length = 63)
    private String authorityName;
}
