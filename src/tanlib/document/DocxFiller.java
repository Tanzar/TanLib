/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import tanlib.containers.StringContainer;
import tanlib.exceptions.DocxException;

/**
 *
 * @author Tanzar
 */
public class DocxFiller {
    
    private final TagContainer tags;
    private final DocxFile patternFile;
    private DocxFile outputFile;
    
    public DocxFiller(DocxFile patternFile){
        this.patternFile = patternFile;
        this.tags = new TagContainer();
    }
    
    public void addTag(Tag newTag){
        this.tags.add(newTag);
    }
    
    public void clearTags(){
        this.tags.clear();
    }
    
    /**
     * Method creates file based on pattern file and tags added to class instance with addTag() method. 
     * For tags to be found they must be marked by '<' and '>' characters ( example <ExampleTag>) in pattern file.
     * if tag is not on list it is ignored(not changed).
     * WARNING! Library changes codint type to ISO 8859-2 from default set in file
     * @param outputFileName name of output file
     * @return file with changed tags, it's located at the same directory as pattern
     * @throws IOException
     * @throws DocxException 
     */
    public DocxFile swapTags(String outputFileName) throws IOException, DocxException{
        outputFile = copyPatternFile(outputFileName);
        String contentsFilePath = outputFile.extractDocumentXML();
        String[] documentContents = readFile(contentsFilePath);
        documentContents[0] = "<?xml version=\"1.0\" encoding=\"ISO_8859-2\" standalone=\"yes\"?>";
        StringContainer splitedContents = changeTags(documentContents);
        prepareOutputFile(splitedContents, contentsFilePath);
        return outputFile;
    }
    
    private DocxFile copyPatternFile(String newFileName) throws IOException, DocxException{
        String destinationFolder = patternFile.getLocalFolderPath();
        DocxFile copy = this.patternFile.copy(destinationFolder, newFileName);
        return copy;
    }
    
    private String[] readFile(String filePath){
        String[] data = new String[2];
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            data[0] = myReader.nextLine();
            data[1] = myReader.nextLine();
            myReader.close();
        } catch (FileNotFoundException e) {
        }
        return data;
    }
    
    private StringContainer changeTags(String[] contents){
        StringContainer newContents = new StringContainer();
        newContents.add(contents[0]);
        boolean end = false;
        while(!end) {
            int tagStartIndex = findTagStartIndex(contents[1]);
            int tagEndIndex = findTagEndIndex(contents[1]);
            if(tagStartIndex != -1 && tagEndIndex != -1 && tagStartIndex < tagEndIndex){
                String charsBeforeTag = contents[1].substring(0, tagStartIndex);
                newContents.add(charsBeforeTag);
                String tag = contents[1].substring(tagStartIndex, tagEndIndex + 4);
                tag = checkTag(tag);
                newContents.add(tag);
                contents[1] = contents[1].substring(tagEndIndex + 4);
            }
            else{
                end = true;
                newContents.add(contents[1]);
            }
        }
        return newContents;
    }
    
    private int findTagStartIndex(String text){
        int index = text.indexOf("&lt;");
        return index;
    }
    
    private int findTagEndIndex(String text){
        int index = text.indexOf("&gt;");
        return index;
    }
    
    private String checkTag(String tag){
        int tagIndex = this.identifyTag(tag);
        if(tagIndex >= 0){
            Tag foundTag = tags.get(tagIndex);
            String textToWrite = foundTag.getStringToWrite();
            return textToWrite;
        }
        else{
            return tag;
        }
    }
    
    private int identifyTag(String tag){
        tag = "<" + tag.substring(4, tag.length() - 4) + ">";  //change tag format from &lt;tag&gt; to <tag>
        int tagIndex = -1;
        int j = 0;
        Tag[] tags = this.tags.toArray();
        boolean found = false;
        while(!found && j < tags.length){
            Tag currentTag = tags[j];
            if(tag.equals(currentTag.getFormat())){
                found = true;
                tagIndex = j;
            }
            j++;
        }
        return tagIndex;
    }
    
    private void prepareOutputFile(StringContainer splitedDocumentContents, String documentXmlPath) throws IOException{
        String[] dataToWrite = combineSplitedContents(splitedDocumentContents);
        writeToFile(documentXmlPath, dataToWrite);
        outputFile.replaceContents(documentXmlPath);
        File documentXml = new File(documentXmlPath);
        documentXml.delete();
    }
    
    /**
     * Output must be array with 2 elements, it is same as document.xml 
     */
    private String[] combineSplitedContents(StringContainer container){
        String[] documentInputs = new String[2];
        documentInputs[0] = container.get(0);
        documentInputs[1] = "";
        for(int i = 1; i < container.size(); i++){
            documentInputs[1] += container.get(i);
        }
        return documentInputs;
    }
    
    private void writeToFile(String filePath, String[] dataToWrite) throws IOException{
        FileWriter fileWriter = new FileWriter(filePath);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(int i = 0; i < dataToWrite.length; i++){
            printWriter.println(dataToWrite[i]);
        }
        printWriter.close();
    }
    
    public void removeOutputFile(){
        if(outputFile != null){
            File file = this.outputFile.getFile();
            file.delete();
            this.outputFile = null;
        }
    }
}
