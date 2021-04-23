package com.bomstart.tobyspring.template;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
