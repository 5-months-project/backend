package projectbusan.gongda.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import projectbusan.gongda.dto.*;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.exception.NotFoundGroupException;
import projectbusan.gongda.exception.NotFoundMemberException;
import projectbusan.gongda.repository.GroupRepository;
import projectbusan.gongda.repository.UserRepository;
import projectbusan.gongda.service.GroupService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;


    //DI 수정필요
    @Autowired
    public GroupController(GroupService groupService, GroupRepository groupRepository,  UserRepository userRepository) {
        this.groupService = groupService;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    /*그룹생성*/
    @PostMapping("/groups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> create(@Valid @RequestBody GroupCreateDTO groupCreateDto, Authentication authentication){
        Group group =new Group();
        group.setName(groupCreateDto.getGroupname());
        group.setPassword(groupCreateDto.getPassword());
        groupService.createGroup(group);

        String userEmail = authentication.getName();
        Optional<User> opUser = userRepository.findOneWithAuthoritiesByUsername(userEmail);
        if (opUser.isEmpty()){
            throw new NotFoundMemberException("일치하는 유저를 찾을 수 없습니다.");
        }
        User user = opUser.get();

        return ResponseEntity.ok(groupService.enterGroup(user,group));
    }

    /*유저의 그룹리스트 조회*/
    @GetMapping("/groups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> groups(Authentication authentication){
        String userEmail = authentication.getName();
        if (userEmail != null)throw new NotFoundMemberException("일치하는 유저가 없습니다.");
        Optional<User> opUser =userRepository.findOneWithAuthoritiesByUsername(userEmail);
        if (opUser.isEmpty()){
            throw new NotFoundMemberException("일치하는 유저가 없습니다.");
        }
        User user = userRepository.findOneWithAuthoritiesByUsername(userEmail).get();
        List<Group> findGroups = groupService.findGroups(user); //유저엔티티로 그룹들찾아오기
        List<GroupDTO> groups = findGroups.stream()
                .map(m-> new GroupDTO(m.getName(),m.getCode()))
                .collect(Collectors.toList());
        ResultDTO<List> resultDto = new ResultDTO<>(groups);
        return ResponseEntity.ok(resultDto);
    }
    /*그룹참여*/
    @PostMapping("/group")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> enter(@Valid @RequestBody GroupEnterDTO groupEnterDto, Authentication authentication){
        String userEmail = authentication.getName();
        Optional<User> opUser = userRepository.findOneWithAuthoritiesByUsername(userEmail);
        if (opUser.isEmpty()){
            throw new NotFoundMemberException("일치하는 유저를 찾을 수 없습니다.");
        }
        User user = opUser.get();
        Group group = groupService.findGroup(groupEnterDto);
        return ResponseEntity.ok(groupService.enterGroup(user,group));//유저-그룹추가
    }

    /*그룹의 멤버조회*/
    //그룹조회권한 확인추가해야함
    @GetMapping("/groups/{groupcode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> groupMembers(@PathVariable String groupcode){
        Optional<Group> opGroup =groupRepository.findByCode(groupcode);
        if (opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group = opGroup.get();
        List<User> findMembers = groupService.findMembers(group); //그룹엔티티로 유저리스트찾기
        List<UserInfoDTO> members = findMembers.stream()
                .map(m-> new UserInfoDTO(m.getNickname(),m.getUsername()))
                .collect(Collectors.toList());
        ResultDTO<List> resultDto = new ResultDTO<>(members);
        return ResponseEntity.ok(resultDto);
    }

    /*그룹나가기*/
    @DeleteMapping("/group/{groupcode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> exit(@PathVariable String groupcode,Authentication authentication){
        String userEmail = authentication.getName();
        Optional<User> opUser = userRepository.findOneWithAuthoritiesByUsername(userEmail);
        if (opUser.isEmpty()){
            throw new NotFoundMemberException("일치하는 유저를 찾을 수 없습니다.");
        }
        User user = opUser.get();
        Optional<Group> opGroup = groupRepository.findByCode(groupcode);
        if (opGroup.isEmpty()){
            throw new NotFoundMemberException("일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group= opGroup.get();

        return ResponseEntity.ok(groupService.exitGroup(user,group)); //유저-그룹 삭제
    }

}

