package springbook.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class DaoTest {

    @Autowired
    private ApplicationContext context;

    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp(){
        this.dao = this.context.getBean("userDao",UserDao.class);

        this.user1 = new User("gyumee","박성철","springno1");
        this.user2 = new User("gyumee2","이길원","springno2");
        this.user3 = new User("gyumee3","박범진","springno3");
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        JUnitCore.main("springbook.user.DaoTest");

    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        dao.create();

        User user = new User("gyumee","박성철","springno1");

        dao.add(user);

        User user2 = dao.get(user.getId());

        System.out.println("user2.getName() : "+user2.getName());
        System.out.println("user.getName() : "+user.getName());

        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));
    }


    @Test
    public void count() throws SQLException, ClassNotFoundException {

        dao.deleteAll();
        assertThat(dao.getCount(),is(0));


        dao.add(user1);
        assertThat(dao.getCount(),is(1));

        dao.add(user2);
        assertThat(dao.getCount(),is(2));

        dao.add(user3);
        assertThat(dao.getCount(),is(3));

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
        dao.get("unkown_id");

    }
}
