package projectbusan.gongda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbusan.gongda.entity.Schedule;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    List<Schedule> findAllByCreatorAndDateAndGroupcode(String username, Long date, String groupcode);
    Optional<Schedule> findOneByScheduleCode(String schedule_code);


    List<Schedule> findAllByScheduleCode(String schedule_code);
    List<Schedule> findAllByDateAndGroupcode(Long date, String groupcode);
}
