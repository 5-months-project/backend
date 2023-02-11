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
import projectbusan.gongda.dto.ResultDTO;
import projectbusan.gongda.dto.ScheduleCreateDTO;
import projectbusan.gongda.dto.ScheduleDTO;
import projectbusan.gongda.dto.ScheduleModifyDTO;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.exception.NotFoundMemberException;
import projectbusan.gongda.repository.UserRepository;
import projectbusan.gongda.service.ScheduleService;

import javax.validation.Valid;
import java.security.Key;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final UserRepository userRepository;
    private final String secret ="26630e34ffad38ac9c311b6521df32ce27b53c6c3a87403b79ed7bb09f836d9cf746b0d75ae42abd7845927f81b8343ee2fa292946cf67cfac95444a19c29127";

    @Autowired
    public ScheduleController(ScheduleService scheduleService, UserRepository userRepository) {
        this.scheduleService = scheduleService;
        this.userRepository = userRepository;
    }



    /*개인스케쥴-생성  생성시 날짜벌로 객체가 생성됨, 예를 들어 2/1~2/3인 스케쥴을 생성시 2/1,2/2,2/3 3개의 객체가 생성되고 이 셋은 동일한 code을 가짐 */
    @PostMapping("/schedule")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> create(@Valid @RequestBody ScheduleCreateDTO scheduleCreateDTO, HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        User user =userRepository.findOneWithAuthoritiesByUsername(useremail).get();
        return ResponseEntity.ok(scheduleService.create(scheduleCreateDTO,user,ScheduleService.codeCreate()));
    }

    /*개인스케쥴-날짜별조회*/
    @GetMapping("/schedule-date/{date}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> read(@PathVariable Long date,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.readByDate(user,date));
        return ResponseEntity.ok(resultDTO);
    }

    /*개인스케쥴-수정 (name,content,category 수정만됨, 시간수정시에는 삭제후 재등록해야함)*/
    @PutMapping("/schedule")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> update(@Valid @RequestBody ScheduleModifyDTO scheduleModifyDTO,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        return ResponseEntity.ok(scheduleService.modify(scheduleModifyDTO,user));
    }

    /*개인스케쥴-삭제*/
    @DeleteMapping("/schedule/{schedulecode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> delete(@PathVariable String schedulecode,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        return ResponseEntity.ok(scheduleService.delete(schedulecode,user));
    }


    /*개인의 모든 스케쥴 가져오기, DTO만들어서 하나처럼*/
    @GetMapping("/schedules")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> readAll(HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다. ");
        User user =opuser.get();
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.readAll(user));
        return ResponseEntity.ok(resultDTO);
    }



    /*그룹스케쥴-생성*/
    @PostMapping("/group-schedule/{group_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_create(@Valid @RequestBody ScheduleCreateDTO scheduleCreateDTO, @PathVariable String group_code,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        return ResponseEntity.ok(scheduleService.group_create(scheduleCreateDTO,group_code,user,ScheduleService.codeCreate()));
    }

    /*그룹스케쥴-날짜별 조회*/
    @GetMapping("/group-schedule-date/{group_code}/{date}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> group_read(@PathVariable String group_code,@PathVariable Long date,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.group_readByDate(group_code,date,user));
        return ResponseEntity.ok(resultDTO);
    }

    /*그룹스케쥴-수정*/
    @PutMapping("/group-schedule")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_update(@Valid @RequestBody ScheduleModifyDTO scheduleModifyDTO,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        return ResponseEntity.ok(scheduleService.group_modify(scheduleModifyDTO,user));
    }
    /*그룹스케쥴-삭제*/
    //todo 얘가 그룹에 속해있는지 체크
    @DeleteMapping("/group-schedule/{schedule_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_delete(@PathVariable String schedule_code, HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        return ResponseEntity.ok(scheduleService.group_delete(schedule_code,user));
    }


    /*그룹의 모든 스케쥴 가져오기 , DTO만들어서 하나처럼*/
    //todo 얘가 그룹에 속해있는지 체크
    @GetMapping("/group-schedules/{group_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> group_readAll(@PathVariable String group_code,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.group_readAll(group_code,user));
        return ResponseEntity.ok(resultDTO);
    }


    /*코드로 일정 가져오기(개인,그룹 상관X) , DTO 만들어서 하나의 일정처럼 보이게 내보내기*/
    @GetMapping("/schedule/{schedule_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_read(@PathVariable String schedule_code,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        return ResponseEntity.ok(scheduleService.readByCode(schedule_code,user));
    }

    /*개인,그룹 스케쥴-월별통합조회*/
    @GetMapping("/schedule-month/{month}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> readByMonth(@PathVariable Long month,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.readByMonth(user,month));
        return ResponseEntity.ok(resultDTO);
    }

    /*개인,그룹스케쥴-다가오는 일정통합조회*/
    @GetMapping("/schedule-comming/{todaydate}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> readCommings(@PathVariable Long todaydate,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.commingSchedule(user,todaydate));
        return ResponseEntity.ok(resultDTO);
    }

    /*개인,그룹스케쥴-날짜별 일정통합조회*/
    @GetMapping("/all-schedule-date/{date}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> allReadByDate(@PathVariable Long date,HttpServletRequest request){
        String useremail = getUseremailByToken(request);
        Optional<User> opuser =userRepository.findOneWithAuthoritiesByUsername(useremail);
        if (opuser.isEmpty()) throw new NotFoundMemberException("해당 유저를 찾을 수 없습니다.");
        User user =opuser.get();
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.allReadByDate(user,date));
        return ResponseEntity.ok(resultDTO);
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
