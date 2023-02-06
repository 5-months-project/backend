package projectbusan.gongda.repository;
import lombok.Builder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbusan.gongda.entity.Group;

import java.util.Optional;

@Repository

public interface GroupRepository extends JpaRepository<Group,Long> {

    Group save(Group group);
    Optional<Group> findByCode(String code);



}
