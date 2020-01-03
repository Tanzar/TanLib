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

/**
 *
 * @author Tanzar
 */
public class Tester {
    
    @Test
    public void test() throws IOException, DocxException{
        File pattern = new File("src\\resources\\TagReplacerTest.docx");
        DocxFile file = new DocxFile(pattern);
        DocxFiller replacer = new DocxFiller(file);
        Tag tag = new Tag("<..test1..>", "hello");
        replacer.addTag(tag);
        tag = new Tag("<test2>", "there");
        replacer.addTag(tag);
        tag = new Tag("<test3>", "General javobi");
        replacer.addTag(tag);
        tag = new Tag("<test4>", "puk puk");
        replacer.addTag(tag);
        tag = new Tag("<test5>", "Kto tam jest z lewej?");
        replacer.addTag(tag);
        tag = new Tag("<test6>", "Big");
        replacer.addTag(tag);
        tag = new Tag("<test7>", "minus big");
        replacer.addTag(tag);
        replacer.fillFile("test");
        replacer.clearAfterYourself();
    }
    
}
