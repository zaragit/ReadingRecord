package com.bomstart.tobyspring;

import com.bomstart.tobyspring.pointcut.Bean;
import com.bomstart.tobyspring.pointcut.Target;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PointcutTest {
    @Test
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public int "
                + "com.bomstart.tobyspring.pointcut.Target.minus(int,int) "
                + "throws java.lang.RuntimeException)");

        // Target.minus()
        assertThat(pointcut.getClassFilter().matches(Target.class)
                && pointcut.getMethodMatcher()
                .matches(Target.class.getMethod("minus", int.class, int.class), null),
                is(true));

        // Target.plus()
        assertThat(pointcut.getClassFilter().matches(Target.class)
                && pointcut.getMethodMatcher()
                .matches(Target.class.getMethod("plus", int.class, int.class), null),
                is(false));

        // Bean.method()
        assertThat(pointcut.getClassFilter().matches(Bean.class)
                && pointcut.getMethodMatcher().matches(Target.class.getMethod("method"), null),
                is(false));
    }

    @Test
    public void pointcut() throws Exception {
        targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);
    }

    private void targetClassPointcutMatches(String expression, boolean... expected) throws Exception{
        pointcutMatches(expression, expected[0], Target.class, "hello");
        pointcutMatches(expression, expected[1], Target.class, "hello", String.class);
        pointcutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
        pointcutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
        pointcutMatches(expression, expected[4], Target.class, "method");
        pointcutMatches(expression, expected[5], Bean.class, "method");
    }

    private void pointcutMatches(String expression, Boolean expected, Class<?> clazz
            , String methodName, Class<?>... args) throws Exception {

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        assertThat(pointcut.getClassFilter().matches(clazz)
                && pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null),
                is(expected));
    }
}
