package com.bomstart.tobyspring.user.dao;

import com.bomstart.tobyspring.user.domain.Level;
import com.bomstart.tobyspring.user.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

@SpringBootTest
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void mockUpFixture() {
        this.user1 = new User("test1", "testName1", "!@#$%", Level.BASIC, 1, 0);
        this.user2 = new User("test2", "testName2", "!@#$%", Level.SILVER, 55, 10);
        this.user3 = new User("test3", "testName3", "!@#$%", Level.GOLD, 100, 40);
    }

    @AfterEach
    void clearDB() {
        userDao.deleteAll();
    }

    @Test
    @DisplayName("유저 생성 테스트")
    void addUser() {
        userDao.add(user1);

        User user = userDao.get(user1.getId());

        checkSameUser(user, user1);
    }

    @Test
    @DisplayName("유저수 조회 테스트")
    void count() {
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        assertThat(userDao.getCount(), is(1));

        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));

        userDao.add(user3);
        assertThat(userDao.getCount(), is(3));
    }

    @Test
    @DisplayName("유저 생성과 조회 테스트")
    void addAndGet() {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        assertThat(userDao.getCount(), is(1));

        User user = userDao.get(user1.getId());

        checkSameUser(user, user1);
    }

    @Test
    @DisplayName("잘못된 유저 조회 테스트")
    void getUserFailure() {
        userDao.deleteAll();

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            assertThat(userDao.getCount(), is(0));

            userDao.get("unkown_id");
        });
    }

    @Test
    @DisplayName("모든 유저 조회")
    void getAll() {
        userDao.deleteAll();

        assertThat(userDao.getAll().size(), is(0));

        userDao.add(user1);
        List<User> users1 = userDao.getAll();
        assertThat(users1.size(), is(1));
        checkSameUser(user1, users1.get(0));

        userDao.add(user2);
        List<User> users2 = userDao.getAll();
        assertThat(users2.size(), is(2));
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));
    }

    @Test
    @DisplayName("유저 정보 업데이트")
    public void update() {
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user2);

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);

        userDao.update(user1);

        User user1update = userDao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2same = userDao.get(user2.getId());
        checkSameUser(user2, user2same);
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }

}
