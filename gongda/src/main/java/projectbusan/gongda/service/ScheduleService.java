package projectbusan.gongda.service;

import org.springframework.stereotype.Service;
import projectbusan.gongda.dto.ScheduleCreateDTO;
import projectbusan.gongda.dto.ScheduleDTO;
import projectbusan.gongda.dto.ScheduleModifyDTO;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.Schedule;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.exception.NotFoundGroupException;
import projectbusan.gongda.exception.NotFoundScheduleException;
import projectbusan.gongda.repository.GroupRepository;
import projectbusan.gongda.repository.ScheduleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;


    public ScheduleService(ScheduleRepository scheduleRepository, GroupRepository groupRepository) {
        this.scheduleRepository = scheduleRepository;
        this.groupRepository = groupRepository;
    }


    public List<ScheduleDTO> readByDate(User user, Long date) {
        List<Schedule> schedules = scheduleRepository.findAllByCreator_idAndDateAndGroup_id(user.getId(), date, 0L);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .content(schedule.getContent())
                    .name(schedule.getName())
                    .category(schedule.getCategory())
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

    public List<ScheduleDTO> group_readByDate(String group_code, Long date) {
        Optional<Group> opGroup = groupRepository.findOneByCode(group_code);
        if (opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹이 없습니다.");
        }
        Group group = opGroup.get();
        List<Schedule> schedules = scheduleRepository.findAllByDateAndGroup_id(date, group.getId());
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .content(schedule.getContent())
                    .name(schedule.getName())
                    .category(schedule.getCategory())
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
                    .content(schedule.getContent())
                    .name(schedule.getName())
                    .category(schedule.getCategory())
                    .creator_id(schedule.getCreator_id())
                    .modifier_id(schedule.getModifier_id())
                    .group_id(schedule.getGroup_id())
                    .time_end(schedule.getTime_end())
                    .time_start(schedule.getTime_start())
                    .build();
            if (!scheduleDTOS.contains(scheduleDTO)) {
                scheduleDTOS.add(scheduleDTO);
            }
        }
        return scheduleDTOS;
    }

    public List<ScheduleDTO> group_readAll(String groupCode) {
        Optional<Group> opGroup = groupRepository.findOneByCode(groupCode);
        if (opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group = opGroup.get();
        List<Schedule> schedules = group.getSchedules();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .content(schedule.getContent())
                    .name(schedule.getName())
                    .category(schedule.getCategory())
                    .creator_id(schedule.getCreator_id())
                    .modifier_id(schedule.getModifier_id())
                    .group_id(schedule.getGroup_id())
                    .time_end(schedule.getTime_end())
                    .time_start(schedule.getTime_start())
                    .build();
            if (!scheduleDTOS.contains(scheduleDTO)) {
                scheduleDTOS.add(scheduleDTO);
            }
        }
        return scheduleDTOS;
    }

    public ScheduleDTO readByCode(String code){
        Optional<Schedule> opSchedule = scheduleRepository.findOneByCode(code);
        if(opSchedule.isEmpty()){
            throw new NotFoundScheduleException("코드와 일치하는 스케쥴을 찾을 수 없습니다.");
        }
        Schedule schedule = opSchedule.get();
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .content(schedule.getContent())
                .name(schedule.getName())
                .category(schedule.getCategory())
                .creator_id(schedule.getCreator_id())
                .modifier_id(schedule.getModifier_id())
                .group_id(schedule.getGroup_id())
                .time_end(schedule.getTime_end())
                .time_start(schedule.getTime_start())
                .build();
        return scheduleDTO;
    }




    //시작시간 끝시간 확인해서 스케쥴객체 여러개 생성(2/1~2/4면 스케쥴 객체 4개 생성, 객체4개의 코드는 동일)
    public ScheduleDTO create(ScheduleCreateDTO scheduleCreateDTO, User user) {
        Long start = scheduleCreateDTO.getTime_start()/10000;
        Long end = scheduleCreateDTO.getTime_end()/10000;
        String code = codeCreate();
        while (validDuplicateCode(code)){
            code = codeCreate();
        }

        for(Long i=start;i<=end;i++) {
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
                    .schedule_code(code)
                    .build();
            user.addSchedule(schedule);
        }
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .content(scheduleCreateDTO.getContent())
                .name(scheduleCreateDTO.getName())
                .category(scheduleCreateDTO.getCategory())
                .creator_id(user.getId())
                .modifier_id(user.getId())
                .group_id(0L)
                .time_end(scheduleCreateDTO.getTime_end())
                .time_start(scheduleCreateDTO.getTime_start())
                .schedule_code(code)
                .build();
        return scheduleDTO;
    }

    public ScheduleDTO group_create(ScheduleCreateDTO scheduleCreateDTO,String groupCode,User user){
        Optional<Group> opGroup = groupRepository.findOneByCode(groupCode);
        if (opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹이 없습니다.");
        }
        Group group = opGroup.get();
        Long start = scheduleCreateDTO.getTime_start()/10000;
        Long end = scheduleCreateDTO.getTime_end()/10000;
        String code = codeCreate();
        while (validDuplicateCode(code)){
            code = codeCreate();
        }
        for(Long i=start;i<=end;i++) {
            Schedule schedule = Schedule.builder()
                    .creator_id(scheduleCreateDTO.getCreator_id())
                    .content(scheduleCreateDTO.getContent())
                    .group_id(group.getId())
                    .modifier_id(user.getId())
                    .name(scheduleCreateDTO.getName())
                    .time_end(scheduleCreateDTO.getTime_end())
                    .time_start(scheduleCreateDTO.getTime_start())
                    .category(scheduleCreateDTO.getCategory())
                    .date(i)
                    .schedule_code(code)
                    .build();
            group.addSchedule(schedule);
        }
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .content(scheduleCreateDTO.getContent())
                .name(scheduleCreateDTO.getName())
                .category(scheduleCreateDTO.getCategory())
                .creator_id(user.getId())
                .modifier_id(user.getId())
                .group_id(group.getId())
                .time_end(scheduleCreateDTO.getTime_end())
                .time_start(scheduleCreateDTO.getTime_start())
                .schedule_code(code)
                .build();
        return scheduleDTO;
    }


    public ScheduleDTO modify(ScheduleModifyDTO scheduleModifyDTO, User user){
        List<Schedule> schedules = scheduleRepository.findAllByCode(scheduleModifyDTO.getSchedule_code());
        Optional<Schedule> opSchedule = scheduleRepository.findOneByCode(scheduleModifyDTO.getSchedule_code());
        for(Schedule schedule:schedules){
            user.deleteSchedule(schedule);
        }
        if (opSchedule.isEmpty()){
            throw new NotFoundScheduleException("코드와 일치하는 스케줄을 찾을 수 없습니다.");
        }
        Schedule schedule = opSchedule.get();
        ScheduleCreateDTO scheduleCreateDTO = ScheduleCreateDTO.builder()
                .name(scheduleModifyDTO.getName())
                .content(scheduleModifyDTO.getContent())
                .category(scheduleModifyDTO.getCategory())
                .time_start(schedule.getTime_start())
                .time_end(schedule.getTime_end())
                .creator_id(schedule.getCreator_id())
                .build();
        return create(scheduleCreateDTO,user);
    }

    public ScheduleDTO group_modify(ScheduleModifyDTO scheduleModifyDTO, String group_code,User user){
        Optional<Group> opGroup = groupRepository.findOneByCode(group_code);
        if(opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group=opGroup.get();

        List<Schedule> schedules = scheduleRepository.findAllByCode(scheduleModifyDTO.getSchedule_code());
        Optional<Schedule> opSchedule = scheduleRepository.findOneByCode(scheduleModifyDTO.getSchedule_code());
        for(Schedule schedule:schedules){
            group.deleteSchedule(schedule);
        }
        if (opSchedule.isEmpty()){
            throw new NotFoundScheduleException("코드와 일치하는 스케줄을 찾을 수 없습니다.");
        }
        Schedule schedule = opSchedule.get();
        ScheduleCreateDTO scheduleCreateDTO = ScheduleCreateDTO.builder()
                .name(scheduleModifyDTO.getName())
                .content(scheduleModifyDTO.getContent())
                .category(scheduleModifyDTO.getCategory())
                .time_start(schedule.getTime_start())
                .time_end(schedule.getTime_end())
                .creator_id(schedule.getCreator_id())
                .build();
        return group_create(scheduleCreateDTO,group.getCode(),user);
    }


    public ScheduleDTO delete(String code,User user){
        List<Schedule> schedules = scheduleRepository.findAllByCode(code);
        Optional<Schedule> opSchedule = scheduleRepository.findOneByCode(code);
        for(Schedule schedule:schedules){
            user.deleteSchedule(schedule);
        }
        if (opSchedule.isEmpty()){
            throw new NotFoundScheduleException("코드와 일치하는 스케줄을 찾을 수 없습니다.");
        }
        Schedule schedule = opSchedule.get();
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .content(schedule.getContent())
                .name(schedule.getName())
                .category(schedule.getCategory())
                .creator_id(schedule.getCreator_id())
                .modifier_id(schedule.getModifier_id())
                .group_id(schedule.getGroup_id())
                .time_end(schedule.getTime_end())
                .time_start(schedule.getTime_start())
                .schedule_code(schedule.getSchedule_code())
                .build();

        return scheduleDTO;
    }

    public ScheduleDTO group_delete(String schedule_code){

        List<Schedule> schedules = scheduleRepository.findAllByCode(schedule_code);
        Optional<Schedule> opSchedule = scheduleRepository.findOneByCode(schedule_code);
        Schedule schedule = opSchedule.get();
        Optional<Group> opGroup = groupRepository.findOneById(schedule.getGroup_id());
        if(opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group=opGroup.get();
        for(Schedule temp_schedule:schedules){
            group.deleteSchedule(temp_schedule);
        }
        if (opSchedule.isEmpty()){
            throw new NotFoundScheduleException("코드와 일치하는 스케줄을 찾을 수 없습니다.");
        }
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .content(schedule.getContent())
                .name(schedule.getName())
                .category(schedule.getCategory())
                .creator_id(schedule.getCreator_id())
                .modifier_id(schedule.getModifier_id())
                .group_id(schedule.getGroup_id())
                .time_end(schedule.getTime_end())
                .time_start(schedule.getTime_start())
                .schedule_code(schedule.getSchedule_code())
                .build();

        return scheduleDTO;
    }


    private boolean validDuplicateCode(String code) {
        if (scheduleRepository.findOneByCode(code).isPresent() && scheduleRepository.findOneByCode(code)!=null) return true;
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
