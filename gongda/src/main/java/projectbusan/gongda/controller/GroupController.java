package projectbusan.gongda.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import projectbusan.gongda.dto.*;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.service.GroupService;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;

    }

    /*그룹생성*/
    @PostMapping("/groups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> create(@Valid @RequestBody GroupCreateDTO groupCreateDto, @AuthenticationPrincipal User user){
        Group group = groupService.createGroup(groupCreateDto);
        return ResponseEntity.ok(groupService.enterGroup(user,group));
    }

    /*유저의 그룹리스트 조회*/
    @GetMapping("/groups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")

    public ResponseEntity<ResultDTO> groups(@AuthenticationPrincipal User user){
        List<GroupDTO> groups = groupService.readGroups(user);
        ResultDTO<List> resultDto = new ResultDTO<>(groups);
        return ResponseEntity.ok(resultDto);
    }
    /*그룹참여*/
    @PostMapping("/group")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> enter(@Valid @RequestBody GroupEnterDTO groupEnterDto, @AuthenticationPrincipal User user){
        Group group = groupService.findGroup(groupEnterDto);
        return ResponseEntity.ok(groupService.enterGroup(user,group));//유저-그룹추가
    }

    /*그룹의 멤버조회*/
    @GetMapping("/groups/{groupcode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> groupMembers(@PathVariable String groupcode ,@AuthenticationPrincipal User user){
        List<UserInfoDTO> members = groupService.findMembers(groupcode,user);
        ResultDTO<List> resultDto = new ResultDTO<>(members);
        return ResponseEntity.ok(resultDto);
    }

    /*그룹나가기*/
    @DeleteMapping("/group/{groupcode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> exit(@PathVariable String groupcode,@AuthenticationPrincipal User user){

        return ResponseEntity.ok(groupService.exitGroup(user,groupcode));
    }


}

