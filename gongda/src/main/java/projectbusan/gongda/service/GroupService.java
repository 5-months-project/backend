package projectbusan.gongda.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import projectbusan.gongda.dto.GroupDTO;
import projectbusan.gongda.dto.GroupEnterDTO;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.exception.NotFoundGroupException;
import projectbusan.gongda.exception.WrongGroupPasswordException;
import projectbusan.gongda.repository.GroupRepository;


import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;


@Transactional
@Service
public class GroupService {
    private final GroupRepository groupRepository;



    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /*
            그룹생성
        */

    public GroupDTO createGroup(Group group){
        group.setCode(codeCreate());
        while (validDuplicateMember(group)){
            group.setCode(codeCreate());
        }
        groupRepository.save(group);
        return new GroupDTO(group.getName(), group.getCode());


    }


    /*
        코드중복조사
    */
    private boolean validDuplicateMember(Group group) {
        if (groupRepository.findByCode(group.getCode()).isPresent()) return true;
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
        String code = groupEnterDto.getGroupCode();
        String password = groupEnterDto.getGroupCode();
        Optional<Group> opGroup =groupRepository.findByCode(code);
        if (opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group = groupRepository.findByCode(code).get();
        //패스워드 암호화해야함
        if (group.getPassword()!=password){
            throw new WrongGroupPasswordException("그룹참여 패스워드가 틀렸습니다.");
        }
        return group;
    }

    @Transactional
    public List<Group> findGroups(User user){
        return user.getGroupList();
    }

    @Transactional
    public List<User> findMembers(Group group){
        return group.getUserList();
    }

    @Transactional
    public GroupDTO enterGroup(User user, Group group){
        user.addGroup(group);
        return new GroupDTO(group.getName(), group.getCode());

    }

    @Transactional
    public GroupDTO exitGroup(User user, Group group){
        user.deleteGroup(group);
        return new GroupDTO(group.getName(), group.getCode());
    }









}
