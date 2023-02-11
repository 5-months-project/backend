package projectbusan.gongda.controller;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import projectbusan.gongda.dto.*;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.exception.NotFoundMemberException;
import projectbusan.gongda.repository.UserRepository;
import projectbusan.gongda.service.GroupService;
import javax.validation.Valid;
import java.security.Key;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;
    private final UserRepository userRepository;
    private final String secret ="26630e34ffad38ac9c311b6521df32ce27b53c6c3a87403b79ed7bb09f836d9cf746b0d75ae42abd7845927f81b8343ee2fa292946cf67cfac95444a19c29127";


    @Autowired
    public GroupController(GroupService groupService, UserRepository userRepository) {
        this.groupService = groupService;
        this.userRepository = userRepository;
    }

    /*그룹생성, 나중에 amdin작업시 생성시에 admin을 멤버로 넣고 조회시에는 admin안뜨게 하면 될듯*/
    @PostMapping("/groups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> create(@Valid @RequestBody GroupCreateDTO groupCreateDto, HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        Group group = groupService.createGroup(groupCreateDto,user);
        return ResponseEntity.ok(groupService.enterGroup(user,group));
    }

    /*유저의 그룹리스트 조회*/
    @GetMapping("/groups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> groups(HttpServletRequest request){
        String useremail = getUseremailByToken(request);
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
    public ResponseEntity<GroupDTO> enter(@Valid @RequestBody GroupEnterDTO groupEnterDto, HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        Group group = groupService.findGroup(groupEnterDto);
        return ResponseEntity.ok(groupService.enterGroup(user,group));//유저-그룹추가
    }

    /*그룹의 멤버조회*/
    @GetMapping("/groups/{groupcode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> groupMembers(@PathVariable String groupcode ,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
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
    public ResponseEntity<GroupDTO> exit(@PathVariable String groupcode,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        return ResponseEntity.ok(groupService.exitGroup(user,groupcode));
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private String getUseremailByToken(HttpServletRequest request){
        byte[] keyBytes= Decoders.BASE64.decode(secret);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(resolveToken(request)).getBody();
        String useremail= claims.getSubject();
        return useremail;
    }

}

