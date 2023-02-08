package projectbusan.gongda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbusan.gongda.entity.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {



}
