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
    private DocxFile filledFile;
    
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
     * @param filledFilename name of filled file
     * @return file with filled tags
     * @throws IOException
     * @throws DocxException 
     */
    public DocxFile fillTags(String filledFilename) throws IOException, DocxException{
        filledFile = copyPattern(filledFilename);
        String contentsFilePath = filledFile.extractDocumentXML();
        String contents = readFile(contentsFilePath);
        StringContainer splitContents = changeTags(contents);
        prepareOutputFile(splitContents, contentsFilePath);
        return filledFile;
    }
    
    private DocxFile copyPattern(String copiedFileName) throws IOException, DocxException{
        String destinationFolder = patternFile.getLocalFolderPath();
        DocxFile copy = this.patternFile.copy(destinationFolder, copiedFileName);
        return copy;
    }
    
    private String readFile(String filePath){
        String data = "";
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
        }
        return data;
    }
    
    private StringContainer changeTags(String contents){
        StringContainer newContents = new StringContainer();
        boolean end = false;
        while(!end) {
            int tagStartIndex = findTagStartIndex(contents);
            int tagEndIndex = findTagEndIndex(contents);
            if(tagStartIndex != -1 && tagEndIndex != -1 && tagStartIndex < tagEndIndex){
                String charsBeforeTag = contents.substring(0, tagStartIndex);
                newContents.add(charsBeforeTag);
                String tag = contents.substring(tagStartIndex, tagEndIndex + 4);
                tag = checkTag(tag);
                newContents.add(tag);
                contents = contents.substring(tagEndIndex + 4);
            }
            else{
                end = true;
                newContents.add(contents);
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
        tag = "<" + tag.substring(4, tag.length() - 4) + ">";
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
    
    private void prepareOutputFile(StringContainer documentXMLContents, String documentXmlPath) throws IOException{
        String[] documentInputs = prepareFileInputs(documentXMLContents);
        writeFile(documentXmlPath, documentInputs);
        filledFile.replaceContents(documentXmlPath);
        File documentXml = new File(documentXmlPath);
        documentXml.delete();
    }
    
    private String[] prepareFileInputs(StringContainer container){
        String[] documentInputs = new String[2];
        documentInputs[0] = container.get(0);
        documentInputs[1] = "";
        for(int i = 1; i < container.size(); i++){
            documentInputs[1] += container.get(i);
        }
        return documentInputs;
    }
    
    private void writeFile(String filePath, String[] dataToWrite) throws IOException{
        FileWriter fileWriter = new FileWriter(filePath);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(int i = 0; i < dataToWrite.length; i++){
            printWriter.println(dataToWrite[i]);
        }
        printWriter.close();
    }
    
    public void removeFilledFile(){
        if(filledFile != null){
            File filledFile = this.filledFile.getFile();
            filledFile.delete();
            this.filledFile = null;
        }
    }
}
