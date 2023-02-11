package projectbusan.gongda.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import projectbusan.gongda.dto.*;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.exception.NotFoundMemberException;
import projectbusan.gongda.repository.UserRepository;
import projectbusan.gongda.service.GroupService;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;
    private final UserRepository userRepository;


    @Autowired
    public GroupController(GroupService groupService, UserRepository userRepository) {
        this.groupService = groupService;
        this.userRepository = userRepository;
    }

    /*그룹생성, 나중에 amdin작업시 생성시에 admin을 멤버로 넣고 조회시에는 admin안뜨게 하면 될듯*/
    @PostMapping("/groups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> create(@Valid @RequestBody GroupCreateDTO groupCreateDto, @RequestParam String useremail){
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        Group group = groupService.createGroup(groupCreateDto,user);
        return ResponseEntity.ok(groupService.enterGroup(user,group));
    }

    /*유저의 그룹리스트 조회*/
    @GetMapping("/groups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> groups(@RequestParam String useremail){
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        List<GroupDTO> groups = groupService.readGroups(user);
        ResultDTO<List> resultDto = new ResultDTO<>(groups);
        return ResponseEntity.ok(resultDto);
    }
    /*그룹참여*/
    @PostMapping("/group")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> enter(@Valid @RequestBody GroupEnterDTO groupEnterDto, @RequestParam String useremail){
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        Group group = groupService.findGroup(groupEnterDto);
        return ResponseEntity.ok(groupService.enterGroup(user,group));//유저-그룹추가
    }

    /*그룹의 멤버조회*/
    @GetMapping("/groups/{groupcode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> groupMembers(@PathVariable String groupcode ,@RequestParam String useremail){
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        List<UserInfoDTO> members = groupService.findMembers(groupcode,user);
        ResultDTO<List> resultDto = new ResultDTO<>(members);
        return ResponseEntity.ok(resultDto);
    }

    /*그룹나가기*/
    @DeleteMapping("/group/{groupcode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> exit(@PathVariable String groupcode,@RequestParam String useremail){
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        return ResponseEntity.ok(groupService.exitGroup(user,groupcode));
    }


}

