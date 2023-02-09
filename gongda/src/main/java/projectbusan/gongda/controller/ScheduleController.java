package projectbusan.gongda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import projectbusan.gongda.dto.ResultDTO;
import projectbusan.gongda.dto.ScheduleCreateDTO;
import projectbusan.gongda.dto.ScheduleDTO;
import projectbusan.gongda.dto.ScheduleModifyDTO;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.service.ScheduleService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }



    /*개인스케쥴-생성  생성시 날짜벌로 객체가 생성됨, 예를 들어 2/1~2/3인 스케쥴을 생성시 2/1,2/2,2/3 3개의 객체가 생성되고 이 셋은 동일한 code을 가짐 */
    @PostMapping("/schedule")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> create(@Valid @RequestBody ScheduleCreateDTO scheduleCreateDTO, @AuthenticationPrincipal User user){
        //todo 유저 입력받아야함
        return ResponseEntity.ok(scheduleService.create(scheduleCreateDTO,user));
    }

    /*개인스케쥴-날짜별조회*/
    @GetMapping("/schedule-date/{date}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> read(@PathVariable Long date,@AuthenticationPrincipal User user){
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.readByDate(user,date));
        //todo 유저 입력받아야함
        return ResponseEntity.ok(resultDTO);
    }

    /*개인스케쥴-수정 (name,content,category 수정만됨, 시간수정시에는 삭제후 재등록해야함)*/
    @PutMapping("/schedule")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> update(@Valid @RequestBody ScheduleModifyDTO scheduleModifyDTO,@AuthenticationPrincipal User user){
        //todo 유저입력받아야함 권한확인
        return ResponseEntity.ok(scheduleService.modify(scheduleModifyDTO,user));
    }

    /*개인스케쥴-삭제*/
    @DeleteMapping("/schedule/{schedulecode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> delete(@PathVariable String schedulecode,@AuthenticationPrincipal User user){
        //todo 유저입력받아야함, 권한확인
        return ResponseEntity.ok(scheduleService.delete(schedulecode,user));
    }


    /*개인의 모든 스케쥴 가져오기, DTO만들어서 하나처럼*/
    @GetMapping("/schedules")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> readAll(@AuthenticationPrincipal User user){
        //todo
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.readAll(user));
        return ResponseEntity.ok(resultDTO);
    }



    /*그룹스케쥴-생성*/
    @PostMapping("/group-schedule/{group_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_create(@Valid @RequestBody ScheduleCreateDTO scheduleCreateDTO, @PathVariable String group_code,@AuthenticationPrincipal User user){
        //todo
        return ResponseEntity.ok(scheduleService.group_create(scheduleCreateDTO,group_code,user));
    }

    /*그룹스케쥴-날짜별 조회*/
    @GetMapping("/group-schedule-date/{group_code}/{date}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> group_read(@PathVariable String group_code,@PathVariable Long date){
        //todo
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.group_readByDate(group_code,date));
        return ResponseEntity.ok(resultDTO);
    }

    /*그룹스케쥴-수정*/
    @PutMapping("/group-schedule/{group_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_update(@PathVariable String group_code, @Valid @RequestBody ScheduleModifyDTO scheduleModifyDTO,@AuthenticationPrincipal User user){
        //todo
        return ResponseEntity.ok(scheduleService.group_modify(scheduleModifyDTO,group_code,user));
    }
    /*그룹스케쥴-삭제*/
    @DeleteMapping("/group-schedule/{schedule_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_delete(@PathVariable String schedule_code){
        //todo
        return ResponseEntity.ok(scheduleService.group_delete(schedule_code));
    }



    /*그룹의 모든 스케쥴 가져오기 , DTO만들어서 하나처럼*/
    @GetMapping("/group-schedules/{group_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> group_readAll(@PathVariable String group_code){
        //todo
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.group_readAll(group_code));
        return ResponseEntity.ok(resultDTO);
    }



    /*코드로 일정 가져오기(개인,그룹 상관X) , DTO 만들어서 하나의 일정처럼 보이게 내보내기*/
    @GetMapping("/schedule/{schedule_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_read(@PathVariable String schedule_code){
        //todo 권한체크: 그룹id가 0이면 크레이터랑 유저가 일치하는지, 그룹id로 그룹찾고 그 그룹이 user의 리스트에 있는지 체크
        return ResponseEntity.ok(scheduleService.readByCode(schedule_code));
    }


}
