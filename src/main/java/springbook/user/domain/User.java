package springbook.user.domain;

public class User {
    String id;
    String name;
    String password;

    public User(){
        //자바 빈 규약에서 생성자를 명시적으로 추가 했을 경우 디폴트 생성자도 함께 정의
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
