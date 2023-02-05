package projectbusan.gongda.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import projectbusan.gongda.dto.GroupCreateDto;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.service.GroupService;
import projectbusan.gongda.service.UserGroupService;


@RestController
public class GroupController {
    private final GroupService groupService;
    private final UserGroupService userGroupService;
    private Authentication authentication;

    @Autowired
    public GroupController(GroupService groupService,UserGroupService userGroupService) {
        this.groupService = groupService;
        this.userGroupService =userGroupService;
    }

    /*그룹생성*/
    @PostMapping("/users/groups")
    public ResponseEntity<Group> create(@RequestBody GroupCreateDto groupCreateDto){
        Group group =new Group();
        group.setName(groupCreateDto.getName());
        group.setPassword(groupCreateDto.getPassword());
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    /*유저의 그룹리스트 조회*/
//    @GetMapping("/users/groups")
//    public ResponseEntity<GroupListStr> groups(HttpServletRequest request){
////        authentication = SecurityContextHolder.getContext().getAuthentication();
////        String userEmail = authentication.getName();
////        Long userId = UserService.email_To_Id(username);// 유저이메일로 유저아이디찾아주는 메쏘드
////        List<Group> groups = userGroupService.findGroups(user.getId);
//        String groupsStr= "";
//        for (Group group : groups){
//            groupsStr = groupsStr + " " +group.getName();
//        }
//        GroupListStr groupListStr = new GroupListStr();
//        groupListStr.setGroupList(groupsStr);
//        return ResponseEntity.ok(groupListStr);
    }
    /*그룹참여api*/


    /*그룹정보(코드)조회api*/


    /*그룹의 멤버조회api*/

    /*그룹의 일정조회api -> 이건 스케쥴에서 해야지..?*/



//}
