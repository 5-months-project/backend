package projectbusan.gongda.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import projectbusan.gongda.dto.GroupDTO;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.repository.UserGroupRepository;

import java.util.List;

@Transactional
@Service
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    public UserGroupService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    public List<Group> findGroups(User user){
        return userGroupRepository.findAllByUser(user);

    }
    public List<User> findMembers(Group group){
         return userGroupRepository.findAllByGroup(group);

    }


    public GroupDTO enterGroup(User user, Group group){
        userGroupRepository.save(user,group);
        return new GroupDTO(group.getName(), group.getCode());


    }


}
