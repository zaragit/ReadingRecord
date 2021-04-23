package com.bomstart.tobyspring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml", classes = {})
public class JUnitTests {
    static JUnitTests testObjects;

    @Test public void test1() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Object obj3 = new Object();

        assertThat(obj1, is(obj2));
        assertThat(obj1, sameInstance(obj3));

        assertThat(this, is(not(sameInstance(testObjects))));
        testObjects = this;
    }

    @Test public void test2() {
        assertThat(this, not(sameInstance(testObjects)));
        testObjects = this;
    }

    @Test public void test3() {
        assertThat(this, is(not(testObjects)));
        testObjects = this;
    }
}
