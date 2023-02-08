package projectbusan.gongda.service;

import org.springframework.stereotype.Service;
import projectbusan.gongda.dto.ScheduleCreateDTO;
import projectbusan.gongda.dto.ScheduleDTO;
import projectbusan.gongda.dto.ScheduleModifyDTO;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.Schedule;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.exception.NotFoundGroupException;
import projectbusan.gongda.repository.ScheduleRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;


    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    public List<ScheduleDTO> readByDate(User user, Long date) {
        List<Schedule> schedules = scheduleRepository.findAllByCreator_idAndDateAndGroup_id(user.getId(), date, 0L);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .id(schedule.getId())
                    .content(schedule.getContent())
                    .name(schedule.getName())
                    .category(schedule.getCategory())
                    .date(schedule.getDate())
                    .creator_id(schedule.getCreator_id())
                    .modifier_id(schedule.getModifier_id())
                    .group_id(schedule.getGroup_id())
                    .time_end(schedule.getTime_end())
                    .time_start(schedule.getTime_start())
                    .build();
            scheduleDTOS.add(scheduleDTO);
        }
        return scheduleDTOS;
    }


    public List<ScheduleDTO> readAll(User user) {
        List<Schedule> schedules = user.getSchedules();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .id(schedule.getId())
                    .content(schedule.getContent())
                    .name(schedule.getName())
                    .category(schedule.getCategory())
                    .date(schedule.getDate())
                    .creator_id(schedule.getCreator_id())
                    .modifier_id(schedule.getModifier_id())
                    .group_id(schedule.getGroup_id())
                    .time_end(schedule.getTime_end())
                    .time_start(schedule.getTime_start())
                    .build();
            scheduleDTOS.add(scheduleDTO);
        }
        return scheduleDTOS;
    }


    //시작시간 끝시간 확인해서 스케쥴객체 여러개 생성(2/1~2/4면 스케쥴 객체 4개 생성, 객체4개의 neighbor코드는 동일)
    public List<ScheduleDTO> create(ScheduleCreateDTO scheduleCreateDTO, User user) {
        Long start = scheduleCreateDTO.getTime_start()/10000;
        Long end = scheduleCreateDTO.getTime_end()/10000;
        List<ScheduleDTO> scheduleDTOS= new ArrayList<>();
        String code = codeCreate();
        while (validDuplicateCode(code)){
            code = codeCreate();
        }

        for(Long i=start;i<=end;i++){
            Schedule schedule = Schedule.builder()
                    .creator_id(user.getId())
                    .content(scheduleCreateDTO.getContent())
                    .group_id(0L)
                    .modifier_id(user.getId())
                    .name(scheduleCreateDTO.getName())
                    .time_end(scheduleCreateDTO.getTime_end())
                    .time_start(scheduleCreateDTO.getTime_start())
                    .category(scheduleCreateDTO.getCategory())
                    .date(i)
                    .neighbor_code(code)
                    .build();
            scheduleRepository.save(schedule);
            user.addSchedule(schedule);
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .id(schedule.getId())
                    .content(schedule.getContent())
                    .name(schedule.getName())
                    .category(schedule.getCategory())
                    .date(schedule.getDate())
                    .creator_id(schedule.getCreator_id())
                    .modifier_id(schedule.getModifier_id())
                    .group_id(schedule.getGroup_id())
                    .time_end(schedule.getTime_end())
                    .time_start(schedule.getTime_start())
                    .neighbor_code(schedule.getNeighbor_code())
                    .build();
            scheduleDTOS.add(scheduleDTO);
        }


        return scheduleDTOS;
    }

    public ScheduleDTO remove()

    public List<ScheduleDTO> modify(ScheduleModifyDTO scheduleModifyDTO, User user){
        Optional<Schedule> opSchedule = scheduleRepository.findOneByNeighbor_code(scheduleModifyDTO.getNeighbor_code());
        if (opSchedule.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 스케줄을 찾을 수 없습니다.");
        }
        Schedule schedule = opSchedule.get();

        ScheduleCreateDTO scheduleCreateDTO = ScheduleCreateDTO.builder()
                .name(scheduleModifyDTO.getName())
                .content(scheduleModifyDTO.getContent())
                .category(scheduleModifyDTO.getCategory())
                .time_start(schedule.getTime_start())
                .time_end(schedule.getTime_end())
                .build();
        scheduleRepository.deleteAllByNeighbor_code(scheduleModifyDTO.getNeighbor_code());
        return create(scheduleCreateDTO,user);
    }

    public String delete(String neighbor_code){
        scheduleRepository.deleteAllByNeighbor_code(neighbor_code);
        String msg= "neighbor_code="+neighbor_code+", delete Success";
        return msg;
    }


    private boolean validDuplicateCode(String code) {
        if (scheduleRepository.findOneByNeighbor_code(code).isPresent() && scheduleRepository.findOneByNeighbor_code(code)!=null) return true;
        else return false;

    }

    private String codeCreate(){
        StringBuffer code = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 11; i++) {
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
