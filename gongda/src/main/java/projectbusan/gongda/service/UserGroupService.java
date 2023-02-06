package projectbusan.gongda.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import projectbusan.gongda.dto.GroupDTO;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.entity.UserGroup;
import projectbusan.gongda.repository.UserGroupRepository;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    public UserGroupService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    public List<Group> findGroups(User user){
        List<UserGroup> userGroupList = userGroupRepository.findAllByUser(user);
        List<Group> groupList = new ArrayList<>();
        for(UserGroup userGroup : userGroupList){
            groupList.add(userGroup.getGroup());
        }
        return groupList;
    }
    public List<User> findMembers(Group group){
         List<UserGroup> userGroupList = userGroupRepository.findAllByGroup(group);
         List<User> userList = new ArrayList<>();
         for(UserGroup userGroup : userGroupList){
             userList.add(userGroup.getUser());
         }
         return userList;
    }


    public GroupDTO enterGroup(User user, Group group){
        userGroupRepository.save(user,group);
        return new GroupDTO(group.getName(), group.getCode());


    }
    public GroupDTO exitGroup(User user, Group group){
        userGroupRepository.deleteByUserAndGroup(user,group);
        return new GroupDTO(group.getName(), group.getCode());
    }


}
