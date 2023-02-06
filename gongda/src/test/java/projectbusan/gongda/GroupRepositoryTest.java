package projectbusan.gongda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import projectbusan.gongda.entity.Group;
import projectbusan.gongda.repository.GroupRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@DataJpaTest
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
}
