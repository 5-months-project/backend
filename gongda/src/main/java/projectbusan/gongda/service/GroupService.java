package projectbusan.gongda.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.repository.GroupRepository;



import java.util.Random;



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
    public Group createGroup(Group group){
        group.setCode(codeCreate());
        while (validDuplicateMember(group)){
            group.setCode(codeCreate());
        }
        return group;

    }


    /*
        코드중복조사
    */
    private boolean validDuplicateMember(Group group) {
        if (groupRepository.findByCode(group.getCode()).isPresent()){
            return true;
        }
        else return false;

    }

    private String codeCreate(){
        StringBuffer code = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 20; i++) {
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









}
