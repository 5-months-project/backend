package projectbusan.gongda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbusan.gongda.entity.Schedule;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    List<Schedule> findAllByCreatorAndDateAndGroupid(Long user_id, Long date, Long group_id);
    Optional<Schedule> findOneByScheduleCode(String schedule_code);


    List<Schedule> findAllByScheduleCode(String schedule_code);
    List<Schedule> findAllByDateAndGroupid(Long date, Long group_id);
}
