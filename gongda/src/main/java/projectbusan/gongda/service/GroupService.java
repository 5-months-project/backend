package projectbusan.gongda.service;


import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projectbusan.gongda.dto.GroupCreateDTO;
import projectbusan.gongda.dto.GroupDTO;
import projectbusan.gongda.dto.GroupEnterDTO;
import projectbusan.gongda.dto.UserInfoDTO;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.exception.AccessPermissionException;
import projectbusan.gongda.exception.NotFoundGroupException;
import projectbusan.gongda.exception.NotFoundMemberException;
import projectbusan.gongda.exception.WrongGroupPasswordException;
import projectbusan.gongda.repository.GroupRepository;


import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;



    public GroupService(GroupRepository groupRepository, PasswordEncoder passwordEncoder) {
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
            그룹생성
        */

    public Group createGroup(GroupCreateDTO groupCreateDTO){
        Group group =new Group();
        group.setName(groupCreateDTO.getGroupname());
        group.setPassword(passwordEncoder.encode(groupCreateDTO.getPassword()));
        group.setCode(codeCreate());
        while (validDuplicateMember(group)){
            group.setCode(codeCreate());
        }
        groupRepository.save(group);
        return group;


    }


    /*
        코드중복조사
    */
    private boolean validDuplicateMember(Group group) {
        if (groupRepository.findOneByCode(group.getCode()).isPresent() && groupRepository.findOneByCode(group.getCode())!=null) return true;
        else return false;

    }

    private String codeCreate(){
        StringBuffer code = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 11; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    code.append((char) ((rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    code.append((char) ( (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    code.append((rnd.nextInt(10)));
                    break;
            }
        }

        return code.toString();
    }

    public Group findGroup(GroupEnterDTO groupEnterDto){
        String code = groupEnterDto.getGroupcode();
        String password = groupEnterDto.getGroupcode();
        Optional<Group> opGroup =groupRepository.findOneByCode(code);
        if (opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group = opGroup.get();
        if (passwordEncoder.matches(password, group.getPassword())){
            throw new WrongGroupPasswordException("그룹참여 패스워드가 틀렸습니다.");
        }
        return group;
    }

    @Transactional
    public List<Group> findGroups(User user){
        return user.getGroupList();
    }

    @Transactional
    public List<UserInfoDTO> findMembers(String groupcode,User user){
        Optional<Group> opGroup =groupRepository.findOneByCode(groupcode);
        if (opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group = opGroup.get();
        if(!group.getUserList().contains(user)){
            throw new AccessPermissionException("접근권한이 없습니다. :유저가 해당그룹에 속해 있지않습니다.");
        }
        List<User> findMembers= group.getUserList();
        List<UserInfoDTO> members = findMembers.stream()
                .map(m-> new UserInfoDTO(m.getNickname(),m.getUsername()))
                .collect(Collectors.toList());
        return members;
    }

    @Transactional
    public GroupDTO enterGroup(User user, Group group){
        user.addGroup(group);
        return new GroupDTO(group.getName(), group.getCode());

    }

    @Transactional
    public GroupDTO exitGroup(User user, String groupcode){
        Optional<Group> opGroup = groupRepository.findOneByCode(groupcode);
        if (opGroup.isEmpty()){
            throw new NotFoundMemberException("일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group= opGroup.get();
        user.deleteGroup(group);
        return new GroupDTO(group.getName(), group.getCode());
    }

    public List<GroupDTO> readGroups(User user){
        List<Group> findGroups = findGroups(user); //유저엔티티로 그룹들찾아오기
        List<GroupDTO> groups = findGroups.stream()
                .map(m-> new GroupDTO(m.getName(),m.getCode()))
                .collect(Collectors.toList());
        return groups;
    }








}
