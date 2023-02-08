package projectbusan.gongda.service;

import org.springframework.stereotype.Service;
import projectbusan.gongda.repository.ScheduleRepository;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;


    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


}
