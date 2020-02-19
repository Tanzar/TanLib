/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
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
    public void test() throws IOException, DocxException{
        TesterClass teste = new TesterClass();
        TestEntity test = new TestEntity(10);
        TestEntity tes = teste.test(test);
        System.out.println("przed " + test.getI() + " po " + tes.getI());
    }
    
}
