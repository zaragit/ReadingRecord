package ch01.springbook.user;

import ch01.springbook.user.dao.DaoFactory;
import ch01.springbook.user.dao.UserDao;
import ch01.springbook.user.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class DaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        /*UserDao, ConnectionMaker 구현 클래스와의 런타임 오브젝트 의존관계를 설정하는 책임 담당 */
        //UserDao dao = new DaoFactory().userDao();

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao",UserDao.class);

        dao.create();

        System.out.println("테이블 생성 성공 !");

        User user = new User();
        user.setId("테스트 아이디");
        user.setName("테스터");
        user.setPassword("테스트 비번");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공 !");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + "조회 성공 ! ");

    }
}
