package projectbusan.gongda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.entity.UserGroup;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup,Long> {
    UserGroup save(User user,Group group);
    List<User> findAllByGroup(Group group);


    List<Group> findAllByUser(User user);

    void deleteById(Long id);



}
