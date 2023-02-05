package projectbusan.gongda.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.repository.UserGroupRepository;

import java.util.List;

@Transactional
@Service
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    public UserGroupService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    public List<Group> findGroups(Long userId){
        List<Group> groups = userGroupRepository.findAllByUserId(userId);
        return groups;

    }
}
