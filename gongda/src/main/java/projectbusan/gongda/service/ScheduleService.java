package projectbusan.gongda.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import projectbusan.gongda.dto.ScheduleCreateDTO;
import projectbusan.gongda.dto.ScheduleDTO;
import projectbusan.gongda.dto.ScheduleModifyDTO;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.entity.Schedule;
import projectbusan.gongda.entity.User;
import projectbusan.gongda.exception.AccessPermissionException;
import projectbusan.gongda.exception.NotFoundGroupException;
import projectbusan.gongda.exception.NotFoundMemberException;
import projectbusan.gongda.exception.NotFoundScheduleException;
import projectbusan.gongda.repository.GroupRepository;
import projectbusan.gongda.repository.ScheduleRepository;
import projectbusan.gongda.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;



    public ScheduleService(ScheduleRepository scheduleRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }


    public List<ScheduleDTO> readByDate(User user, Long date) {
        List<Schedule> schedules = scheduleRepository.findAllByCreatorAndDateAndGroupcode(user.getUsername(), date, "0");
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            Optional<User> opCreator = userRepository.findOneWithAuthoritiesByUsername(schedule.getCreator());
            if(opCreator.isEmpty()) throw new NotFoundMemberException("이메일과 일치하는 유저를 찾지 못했습니다.");
            User creator= opCreator.get();
            if (!creator.getId().equals(user.getId())){
                throw new AccessPermissionException("접근권한이 없습니다. :해당유저의 일정이 아닙니다.");
            }
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .content(schedule.getContent())
                    .name(schedule.getName())
                    .category(schedule.getCategory())
                    .creator_nickname(creator.getNickname())
                    .modifier_nickname(creator.getNickname())
                    .group_code("0")
                    .time_end(schedule.getTimeEnd())
                    .time_start(schedule.getTimeStart())
                    .schedule_code(schedule.getScheduleCode())
                    .build();
            scheduleDTOS.add(scheduleDTO);
        }
        return scheduleDTOS;
    }

    public List<ScheduleDTO> group_readByDate(String group_code, Long date,User user) {
        Optional<Group> opGroup = groupRepository.findOneByCode(group_code);
        if (opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹이 없습니다.");
        }
        Group group = opGroup.get();
        if(!group.getUserList().contains(user)){
            throw new AccessPermissionException("접근권한이 없습니다. :유저가 해당그룹에 속해 있지않습니다.");
        }
        List<Schedule> schedules = scheduleRepository.findAllByDateAndGroupcode(date, group.getCode());
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            Optional<User> opCreator = userRepository.findOneWithAuthoritiesByUsername(schedule.getCreator());
            if(opCreator.isEmpty()) throw new NotFoundMemberException("이메일과 일치하는 유저를 찾지 못했습니다.");
            User creator= opCreator.get();
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .content(schedule.getContent())
                    .name(schedule.getName())
                    .category(schedule.getCategory())
                    .creator_nickname(creator.getNickname())
                    .modifier_nickname(creator.getNickname())
                    .group_code(group_code)
                    .time_end(schedule.getTimeEnd())
                    .time_start(schedule.getTimeStart())
                    .schedule_code(schedule.getScheduleCode())
                    .build();
            scheduleDTOS.add(scheduleDTO);
        }
        return scheduleDTOS;
    }



    public List<ScheduleDTO> readAll(User user) {
        List<Schedule> schedules = user.getSchedules();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            Optional<User> opCreator = userRepository.findOneWithAuthoritiesByUsername(schedule.getCreator());
            if(opCreator.isEmpty()) throw new NotFoundMemberException("이메일과 일치하는 유저를 찾지 못했습니다.");
            User creator= opCreator.get();
            if (!creator.getId().equals(user.getId())){
                throw new AccessPermissionException("접근권한이 없습니다. :해당유저의 일정이 아닙니다.");
            }
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .content(schedule.getContent())
                    .name(schedule.getName())
                    .category(schedule.getCategory())
                    .creator_nickname(creator.getNickname())
                    .modifier_nickname(creator.getNickname())
                    .group_code("0")
                    .time_end(schedule.getTimeEnd())
                    .time_start(schedule.getTimeStart())
                    .schedule_code(schedule.getScheduleCode())
                    .build();
            if (!scheduleDTOS.contains(scheduleDTO)) {
                scheduleDTOS.add(scheduleDTO);
            }
        }
        return scheduleDTOS;
    }

    public List<ScheduleDTO> group_readAll(String groupCode,User user) {
        Optional<Group> opGroup = groupRepository.findOneByCode(groupCode);
        if (opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group = opGroup.get();
        if(!group.getUserList().contains(user)){
            throw new AccessPermissionException("접근권한이 없습니다. :유저가 해당그룹에 속해 있지않습니다.");
        }
        List<Schedule> schedules = group.getSchedules();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .content(schedule.getContent())
                    .name(schedule.getName())
                    .category(schedule.getCategory())
                    .creator_nickname(schedule.getCreator())
                    .modifier_nickname(schedule.getModifier())
                    .group_code(groupCode)
                    .time_end(schedule.getTimeEnd())
                    .time_start(schedule.getTimeStart())
                    .schedule_code(schedule.getScheduleCode())
                    .build();
            if (!scheduleDTOS.contains(scheduleDTO)) {
                scheduleDTOS.add(scheduleDTO);
            }
        }
        return scheduleDTOS;
    }

    public ScheduleDTO readByCode(String code,User user){
        //todo 권한체크: 그룹id가 0이면 크레이터랑 유저가 일치하는지, 그룹id로 그룹찾고 그 그룹이 user의 리스트에 있는지 체크
        Optional<Schedule> opSchedule = scheduleRepository.findOneByScheduleCode(code);
        if(opSchedule.isEmpty()){
            throw new NotFoundScheduleException("코드와 일치하는 스케쥴을 찾을 수 없습니다.");
        }
        Schedule schedule = opSchedule.get();
        Optional<User> opCreator = userRepository.findOneWithAuthoritiesByUsername(schedule.getCreator());
        if(opCreator.isEmpty()) throw new NotFoundMemberException("이메일과 일치하는 유저를 찾지 못했습니다.");
        User creator= opCreator.get();
        //개인일정일 경우
        if (schedule.getGroupcode().equals("0")){
            if (!creator.getId().equals(user.getId())){
                throw new AccessPermissionException("접근권한이 없습니다. :해당유저의 일정이 아닙니다.");
            }
        }
        //그룹일정일 경우
        else {
            String groupCode= schedule.getGroupcode();
            Optional<Group> opGroup = groupRepository.findOneByCode(groupCode);
            if (opGroup.isEmpty()){
                throw new NotFoundGroupException("코드와 일치하는 그룹을 찾을 수 없습니다.");
            }
            Group group = opGroup.get();
            if(!group.getUserList().contains(user)){
                throw new AccessPermissionException("접근권한이 없습니다. :유저가 해당그룹에 속해 있지않습니다.");
            }
        }
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .content(schedule.getContent())
                .name(schedule.getName())
                .category(schedule.getCategory())
                .creator_nickname(creator.getNickname())
                .modifier_nickname(creator.getNickname())
                .group_code(schedule.getGroupcode())
                .time_end(schedule.getTimeEnd())
                .time_start(schedule.getTimeStart())
                .schedule_code(schedule.getScheduleCode())
                .build();
        return scheduleDTO;
    }




    //시작시간 끝시간 확인해서 스케쥴객체 여러개 생성(2/1~2/4면 스케쥴 객체 4개 생성, 객체4개의 코드는 동일)
    public ScheduleDTO create(ScheduleCreateDTO scheduleCreateDTO, User user) {
        if(!scheduleCreateDTO.getCreator_email().equals(user.getUsername())){
            throw new AccessPermissionException("접근권한이 없습니다. :Creator_id가  로그인유저와 일치하지 않습니다.");
        }
        Long start = scheduleCreateDTO.getTime_start()/10000;
        Long end = scheduleCreateDTO.getTime_end()/10000;
        String code = codeCreate();
        while (validDuplicateCode(code)){
            code = codeCreate();
        }

        for(Long i=start;i<=end;i++) {
            Schedule schedule = Schedule.builder()
                    .creator(scheduleCreateDTO.getCreator_email())
                    .content(scheduleCreateDTO.getContent())
                    .groupcode("0")
                    .modifier(user.getUsername())
                    .name(scheduleCreateDTO.getName())
                    .timeEnd(scheduleCreateDTO.getTime_end())
                    .timeStart(scheduleCreateDTO.getTime_start())
                    .category(scheduleCreateDTO.getCategory())
                    .date(i)
                    .scheduleCode(code)
                    .build();
            user.addSchedule(schedule);
        }
        Optional<User> opCreator = userRepository.findOneWithAuthoritiesByUsername(scheduleCreateDTO.getCreator_email());
        if(opCreator.isEmpty()) throw new NotFoundMemberException("이메일과 일치하는 유저를 찾지 못했습니다.");
        User creator= opCreator.get();
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .content(scheduleCreateDTO.getContent())
                .name(scheduleCreateDTO.getName())
                .category(scheduleCreateDTO.getCategory())
                .creator_nickname(creator.getNickname())
                .modifier_nickname(user.getNickname())
                .group_code("0")
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
        if(!scheduleCreateDTO.getCreator_email().equals(user.getUsername())){
            throw new AccessPermissionException("접근권한이 없습니다. :Creator_id가  로그인유저와 일치하지 않습니다.");
        }
        if(!group.getUserList().contains(user)){
            throw new AccessPermissionException("접근권한이 없습니다. :유저가 해당그룹에 속해 있지않습니다.");
        }
        Long start = scheduleCreateDTO.getTime_start()/10000;
        Long end = scheduleCreateDTO.getTime_end()/10000;
        String code = codeCreate();
        while (validDuplicateCode(code)){
            code = codeCreate();
        }
        for(Long i=start;i<=end;i++) {
            Schedule schedule = Schedule.builder()
                    .creator(scheduleCreateDTO.getCreator_email())
                    .content(scheduleCreateDTO.getContent())
                    .groupcode(group.getCode())
                    .modifier(user.getUsername())
                    .name(scheduleCreateDTO.getName())
                    .timeEnd(scheduleCreateDTO.getTime_end())
                    .timeStart(scheduleCreateDTO.getTime_start())
                    .category(scheduleCreateDTO.getCategory())
                    .date(i)
                    .scheduleCode(code)
                    .build();
            group.addSchedule(schedule);
        }
        Optional<User> opCreator = userRepository.findOneWithAuthoritiesByUsername(scheduleCreateDTO.getCreator_email());
        if(opCreator.isEmpty()) throw new NotFoundMemberException("이메일과 일치하는 유저를 찾지 못했습니다.");
        User creator= opCreator.get();

        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .content(scheduleCreateDTO.getContent())
                .name(scheduleCreateDTO.getName())
                .category(scheduleCreateDTO.getCategory())
                .creator_nickname(creator.getNickname())
                .modifier_nickname(user.getNickname())
                .group_code(group.getCode())
                .time_end(scheduleCreateDTO.getTime_end())
                .time_start(scheduleCreateDTO.getTime_start())
                .schedule_code(code)
                .build();
        return scheduleDTO;
    }


    public ScheduleDTO modify(ScheduleModifyDTO scheduleModifyDTO, User user){
        List<Schedule> schedules = scheduleRepository.findAllByScheduleCode(scheduleModifyDTO.getSchedule_code());
        Optional<Schedule> opSchedule = scheduleRepository.findOneByScheduleCode(scheduleModifyDTO.getSchedule_code());
        if (opSchedule.isEmpty()){
            throw new NotFoundScheduleException("코드와 일치하는 스케줄을 찾을 수 없습니다.");
        }
        Schedule schedule = opSchedule.get();
        if (!schedule.getGroupcode().equals("0")){
            throw new AccessPermissionException("개인일정이 아닙니다.");
        }
        if (!schedule.getCreator().equals(user.getUsername())){
            throw new AccessPermissionException("해당유저의 일정이 아닙니다.");
        }
        for(Schedule tempSchedule:schedules){
            user.deleteSchedule(tempSchedule);
        }
        ScheduleCreateDTO scheduleCreateDTO = ScheduleCreateDTO.builder()
                .name(scheduleModifyDTO.getName())
                .content(scheduleModifyDTO.getContent())
                .category(scheduleModifyDTO.getCategory())
                .time_start(schedule.getTimeStart())
                .time_end(schedule.getTimeEnd())
                .creator_email(schedule.getCreator())
                .build();
        return create(scheduleCreateDTO,user);
    }

    public ScheduleDTO group_modify(ScheduleModifyDTO scheduleModifyDTO,User user){
        List<Schedule> schedules = scheduleRepository.findAllByScheduleCode(scheduleModifyDTO.getSchedule_code());
        Optional<Schedule> opSchedule = scheduleRepository.findOneByScheduleCode(scheduleModifyDTO.getSchedule_code());
        if (opSchedule.isEmpty()){
            throw new NotFoundScheduleException("코드와 일치하는 스케줄을 찾을 수 없습니다.");
        }
        Schedule schedule = opSchedule.get();
        Optional<Group> opGroup = groupRepository.findOneByCode(schedule.getGroupcode());
        if (opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group = opGroup.get();
        if(!group.getUserList().contains(user)){
            throw new AccessPermissionException("유저가 해당그룹에 속해 있지 않습니다.");
        }
        for(Schedule tempSchedule:schedules){
            group.deleteSchedule(tempSchedule);
        }

        ScheduleCreateDTO scheduleCreateDTO = ScheduleCreateDTO.builder()
                .name(scheduleModifyDTO.getName())
                .content(scheduleModifyDTO.getContent())
                .category(scheduleModifyDTO.getCategory())
                .time_start(schedule.getTimeStart())
                .time_end(schedule.getTimeEnd())
                .creator_email(schedule.getCreator())
                .build();
        return group_create(scheduleCreateDTO,group.getCode(),user);
    }


    public ScheduleDTO delete(String scheduleCode,User user){
        List<Schedule> schedules = scheduleRepository.findAllByScheduleCode(scheduleCode);
        Optional<Schedule> opSchedule = scheduleRepository.findOneByScheduleCode(scheduleCode);
        if (opSchedule.isEmpty()){
            throw new NotFoundScheduleException("코드와 일치하는 스케줄을 찾을 수 없습니다.");
        }
        Schedule schedule = opSchedule.get();
        if (!schedule.getGroupcode().equals("0")){
            throw new AccessPermissionException("개인일정이 아닙니다.");
        }
        if (!schedule.getCreator().equals(user.getUsername())){
            throw new AccessPermissionException("해당유저의 일정이 아닙니다.");
        }
        for(Schedule tempSchedule:schedules){
            user.deleteSchedule(tempSchedule);
        }
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .content(schedule.getContent())
                .name(schedule.getName())
                .category(schedule.getCategory())
                .creator_nickname(schedule.getCreator())
                .modifier_nickname(schedule.getModifier())
                .group_code(schedule.getGroup().getCode())
                .time_end(schedule.getTimeEnd())
                .time_start(schedule.getTimeStart())
                .schedule_code(schedule.getScheduleCode())
                .build();

        return scheduleDTO;
    }

    public ScheduleDTO group_delete(String schedule_code,User user){

        List<Schedule> schedules = scheduleRepository.findAllByScheduleCode(schedule_code);
        Optional<Schedule> opSchedule = scheduleRepository.findOneByScheduleCode(schedule_code);
        Schedule schedule = opSchedule.get();
        Optional<Group> opGroup = groupRepository.findOneByCode(schedule.getGroupcode());
        if(opGroup.isEmpty()){
            throw new NotFoundGroupException("코드와 일치하는 그룹을 찾을 수 없습니다.");
        }
        Group group=opGroup.get();
        if(!group.getUserList().contains(user)){
            throw new AccessPermissionException("유저가 해당그룹에 속해 있지 않습니다.");
        }
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
                .creator_nickname(schedule.getCreator())
                .modifier_nickname(schedule.getModifier())
                .group_code(schedule.getGroup().getCode())
                .time_end(schedule.getTimeEnd())
                .time_start(schedule.getTimeStart())
                .schedule_code(schedule.getScheduleCode())
                .build();

        return scheduleDTO;
    }


    private boolean validDuplicateCode(String code) {
        if (scheduleRepository.findOneByScheduleCode(code).isPresent() && scheduleRepository.findOneByScheduleCode(code)!=null) return true;
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
