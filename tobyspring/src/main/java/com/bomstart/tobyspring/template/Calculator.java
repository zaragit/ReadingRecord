package com.bomstart.tobyspring.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public Integer calcSum(String filePath) throws IOException {
        return lineReadTemplate(filePath, new LineCallback<Integer>(){
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value + Integer.valueOf(line);
            }
        }, 0);
    }

    public Integer calcNultiply(String filePath) throws IOException {
        return lineReadTemplate(filePath, new LineCallback<Integer>(){
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value * Integer.valueOf(line);
            }
        }, 1);
    }

    public String concatenate(String filePath) throws IOException {
        return lineReadTemplate(filePath, new LineCallback<String>(){
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }
        }, "");
    }

    public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReader br = null;

        try {
          br = new BufferedReader(new FileReader(filePath));

          T res = initVal;
          String line = null;
          while ((line = br.readLine()) != null) {
              res = callback.doSomethingWithLine(line, res);
          }

          return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try { br.close(); }
                catch (IOException e) { System.out.println(e.getMessage()); }
            }
        }
    }
}
