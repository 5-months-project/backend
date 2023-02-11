package projectbusan.gongda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbusan.gongda.entity.Schedule;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    List<Schedule> findAllByCreatorAndDateAndGroupcode(String username, Long date, String groupcode);

    List<Schedule> findAllByScheduleCode(String schedule_code);
    List<Schedule> findAllByDateAndGroupcode(Long date, String groupcode);

    List<Schedule> findAllByDateBetweenAndGroupcode(Long start,Long end ,String groupcode);
    List<Schedule> findAllByDateBetweenAndCreatorAndGroupcode(Long start,Long end , String CreatorEmail, String groupcode);
    List<Schedule> findAllByDateAfterAndGroupcode(Long date,String groupcode);
    List<Schedule> findAllByDateAfterAndCreatorAndGroupcode(Long date, String CreatorEmail, String groupcode);

}

