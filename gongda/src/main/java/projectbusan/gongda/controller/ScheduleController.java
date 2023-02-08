package projectbusan.gongda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projectbusan.gongda.dto.ResultDTO;
import projectbusan.gongda.dto.ScheduleCreateDTO;
import projectbusan.gongda.dto.ScheduleDTO;
import projectbusan.gongda.dto.ScheduleModifyDTO;
import projectbusan.gongda.service.ScheduleService;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }



    /*개인스케쥴-생성*/
    @PostMapping("/schedule")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> create(ScheduleCreateDTO scheduleCreateDTO){
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.create(scheduleCreateDTO,user));
        //todo 유저 입력받아야함
        return ResponseEntity.ok(resultDTO);
    }

    /*개인스케쥴-날짜별조회*/
    @GetMapping("/schedule/{date}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> read(@PathVariable Long date){
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.readByDate(user,date));
        //todo 유저 입력받아야함
        return ResponseEntity.ok(resultDTO);
    }

    /*개인스케쥴-수정 (name,content,category 수정만됨, 시간수정시에는 삭제후 재등록해야함)*/
    @PutMapping("/schedule")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> update(ScheduleModifyDTO scheduleModifyDTO){
        //todo 유저입력받아야함 권한확인
        ResultDTO<List> resultDTO = new ResultDTO<>(scheduleService.modify(scheduleModifyDTO,user));
        return ResponseEntity.ok(resultDTO);
    }

    /*개인스케쥴-삭제*/
    @DeleteMapping("/schedule/{neighbor_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> delete(@PathVariable String neighbor_code){
        //todo 유저입력받아야함, 권한확인
        ResultDTO<String> resultDTO = new ResultDTO<>(scheduleService.delete(neighbor_code));
        return ResponseEntity.ok(resultDTO);
    }




    /*그룹스케쥴-생성*/
    @PostMapping("/group-schedule/{group_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_create(ScheduleDTO scheduleDTO, @PathVariable String group_code){
        //todo
        return ResponseEntity.ok();
    }

    /*그룹스케쥴-날짜별 조회*/
    @GetMapping("/group-schedule/{date}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_read(@PathVariable Long date){
        //todo
        return ResponseEntity.ok();
    }

    /*그룹스케쥴-수정*/
    @PutMapping("/group-schedule/{schedule_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_update(@PathVariable Long schedule_id){
        //todo
        return ResponseEntity.ok();
    }
    /*그룹스케쥴-삭제*/
    @DeleteMapping("/group-schedule/{schedule_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_delete(@PathVariable Long schedule_id){
        //todo
        return ResponseEntity.ok();
    }


    /*개인의 모든 스케쥴 가져오기*/
    @GetMapping("/schedules")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> readAll(){
        //todo
        return ResponseEntity.ok(Result(scheduleService.readAll(user)));
    }

    /*그룹의 모든 스케쥴 가져오기*/
    @GetMapping("/group-schedules/{group_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> group_readAll(@PathVariable String group_code){
        //todo
        return ResponseEntity.ok();
    }

    /*연결된 일정들 가져오기 예를들면 2/1~2/5 여행일때  2/3의 스케줄아이디 조회하면 2/1~2/5 객체 다 가져오기, 한번에 수정또는 삭제할 때 사용, 하나로 묶어서 보여줄 때 사용*/

}
