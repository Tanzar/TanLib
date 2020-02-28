/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package document;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import tanlib.document.DocxFile;
import tanlib.document.DocxFiller;
import tanlib.document.Tag;
import tanlib.exceptions.DocxException;

/**
 *
 * @author Tanzar
 */
public class DocxFillerTest {
    
    @Test
    public void testFill() throws DocxException, IOException{
        DocxFile testDocx = new DocxFile("src\\resources\\TagReplacerTest.docx");
        DocxFiller filler = new DocxFiller(testDocx);
        prepareTags(filler);
        DocxFile outputFile = filler.swapTags("testowy");
        File outputPDF = outputFile.convertToPDF("src\\resources\\testowy.pdf");
        filler.removeOutputFile();
        outputPDF.delete();
    }
    
    private void prepareTags(DocxFiller docxFiller){
        Tag newTag = new Tag("<..test1..>", "Hello");
        docxFiller.addTag(newTag);
        newTag = new Tag("<test2>", "There");
        docxFiller.addTag(newTag);
        newTag = new Tag("<test3>", "General Codobi");
        docxFiller.addTag(newTag);
        newTag = new Tag("<test4>", "Jestem lewy");
        docxFiller.addTag(newTag);
        newTag = new Tag("<test5>", "Góra");
        docxFiller.addTag(newTag);
        newTag = new Tag("<test6>", "dół jest na");
        docxFiller.addTag(newTag);
        newTag = new Tag("<test7>", "cichaj u góry");
        docxFiller.addTag(newTag);
    }
}
