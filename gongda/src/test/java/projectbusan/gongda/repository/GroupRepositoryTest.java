package projectbusan.gongda.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.exception.NotFoundGroupException;
import projectbusan.gongda.repository.GroupRepository;

import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@DataJpaTest
@Transactional
public class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;


    @Test
    public void saveTest() {
        Group group = Group.builder()
                .name("testName")
                .password("testPwd")
                .code("testCode")
                .build();
        Group savedGroup = groupRepository.save(group);
    }


    @Test
    public void findByCodeTest(){
        Group group = Group.builder()
                .name("testName")
                .password("testPwd")
                .code("testCode")
                .build();
        groupRepository.save(group);
        Optional<Group> opFoundGroup = groupRepository.findByCode("testCode");

    }
}
