package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao(){
        //UserDao 타입의 오브젝트를 어떻게 만들지 결정
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker());
        return userDao;
    }

    public ConnectionMaker connectionMaker() {
        System.out.println("ss");
        return new DConnectionMaker();
    }


}
