/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import org.junit.Test;
import tanlib.conditioner.Condition;
import tanlib.conditioner.Operators;
import tanlib.document.DocxFile;
import tanlib.document.DocxFiller;
import tanlib.document.Tag;
import tanlib.exceptions.DocxException;
import tanlib.exceptions.EmptyFilePathException;
import tanlib.exceptions.WrongFileTypeException;
import tanlib.testObject.TestEntity;
import tanlib.testObject.TesterClass;

/**
 * Klasa do sprawdzania różnych spraw, nie jest ważna
 * @author Tanzar
 */
public class Tester {
    
    @Test
    public void test() throws IOException, DocxException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        TestEntity test = new TestEntity(10);
        Class c = test.getClass();
        Class[] parameterTypes = new Class[0];
        Method method = c.getMethod("getI", parameterTypes);
        Object obj = method.invoke(test, parameterTypes);
        Class<?> returnType = method.getReturnType();
        for(Method meth: c.getDeclaredMethods()){
            System.out.println(meth.getName());
        }
        
        Integer k = 10;
        Condition condition = new Condition("getI", Operators.same ,k);
        int b = 0;
        boolean met = condition.isMet(b);
        System.out.println(met);
        met = condition.isMet(test);
        System.out.println(met);
        condition = new Condition("isK", Operators.same ,k);
        met = condition.isMet(test);
        System.out.println(met);
        condition = new Condition("isK", Operators.same ,true);
        met = condition.isMet(test);
        System.out.println(met);
        
    }
    
}
