package projectbusan.gongda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projectbusan.gongda.dto.ResultDTO;
import projectbusan.gongda.dto.ScheduleCreateDTO;
import projectbusan.gongda.dto.ScheduleDTO;
import projectbusan.gongda.service.ScheduleService;

import javax.xml.transform.Result;


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
        //todo
        return ResponseEntity.ok();
    }

    /*개인스케쥴-날짜별조회*/
    @GetMapping("/schedule/{date}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> read(PathVariable date){
        //todo
        return ResponseEntity.ok();
    }

    /*개인스케쥴-수정*/
    @PutMapping("/schedule/{schedule_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> update(PathVariable schedule_id){
        //todo
        return ResponseEntity.ok();
    }
    /*개인스케쥴-삭제*/
    @DeleteMapping("/schedule/{schedule_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> delete(PathVariable schedule_id){
        //todo
        return ResponseEntity.ok();
    }


    /*그룹스케쥴-생성*/
    @PostMapping("/group-schedule/{group_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_create(ScheduleDTO scheduleDTO, PathVariable group_code){
        //todo
        return ResponseEntity.ok();
    }

    /*그룹스케쥴-날짜별 조회*/
    @GetMapping("/group-schedule/{date}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_read(PathVariable date){
        //todo
        return ResponseEntity.ok();
    }

    /*그룹스케쥴-수정*/
    @PutMapping("/group-schedule/{schedule_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_update(PathVariable schedule_id){
        //todo
        return ResponseEntity.ok();
    }
    /*그룹스케쥴-삭제*/
    @DeleteMapping("/group-schedule/{schedule_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ScheduleDTO> group_delete(PathVariable schedule_id){
        //todo
        return ResponseEntity.ok();
    }


    /*개인의 모든 스케쥴 가져오기*/
    @GetMapping("/schedules")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> group_delete(){
        //todo
        return ResponseEntity.ok();
    }

    /*그룹의 모든 스케쥴 가져오기*/
    @GetMapping("/group-schedules/{group_code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResultDTO> group_delete(PathVariable group_code){
        //todo
        return ResponseEntity.ok();
    }
}
