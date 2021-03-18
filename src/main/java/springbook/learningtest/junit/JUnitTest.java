package springbook.learningtest.junit;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class JUnitTest {
    /*
    * JUnit으로 생성하는 JUnit 기능 테스트
    * 테스트 메소드 수행 때마다 오브젝트를 생성하므로 순서 상관없이 중복여부 확인 가능
    * */

    static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();

    @Test
    public void test1(){
        assertThat(testObjects,not(hasItem(this)));
        testObjects.add(this);
    }

    @Test
    public void test2(){
        assertThat(testObjects,not(hasItem(this)));
        testObjects.add(this);
    }


    @Test
    public void test3(){
        assertThat(testObjects,not(hasItem(this)));
        testObjects.add(this);
        System.out.println(testObjects.size());
    }


}
