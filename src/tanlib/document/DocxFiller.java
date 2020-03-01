/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.document;

import java.io.IOException;
import tanlib.exceptions.DocxException;

/**
 *
 * @author Tanzar
 */
public class DocxFiller {
    
    private final TagContainer tags;
    private final DocxFile patternFile;
    
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
     * WARNING! Library changes coding type to ISO 8859-2 from default set in file
     * @param outputFileName name of output file
     * @return file with changed tags, it's located at the same directory as pattern
     * @throws IOException
     * @throws DocxException 
     */
    public DocxFile fill(String outputFileName) throws IOException, DocxException{
        String documentContents = this.getPatternContents();
        String newContents = changeTags(documentContents);
        DocxFile outputFile = copyPatternFile(outputFileName);
        String header = "<?xml version=\"1.0\" encoding=\"ISO_8859-2\" standalone=\"yes\"?>";
        outputFile.replaceContents(header, newContents);
        return outputFile;
    }
    
    private String changeTags(String contents){
        String newContents = "";
        boolean end = false;
        while(!end) {
            int tagStartIndex = findTagStartIndex(contents);
            int tagEndIndex = findTagEndIndex(contents);
            if(tagStartIndex != -1 && tagEndIndex != -1){
                if(tagStartIndex < tagEndIndex){
                    String charsBeforeTag = contents.substring(0, tagStartIndex);
                    newContents += charsBeforeTag;
                    String tag = contents.substring(tagStartIndex, tagEndIndex);
                    tag = checkTag(tag);
                    newContents += tag;
                }
                contents = contents.substring(tagEndIndex);
            }
            else{
                end = true;
                newContents += contents;
            }
        }
        return newContents;
    }
    
    private int findTagStartIndex(String text){
        int index = text.indexOf("&lt;");
        return index;
    }
    
    private int findTagEndIndex(String text){
        int index = text.indexOf("&gt;") + 4;
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
    
    private DocxFile copyPatternFile(String newFileName) throws IOException, DocxException{
        String destinationFolder = patternFile.getLocalFolderPath();
        DocxFile copy = this.patternFile.copy(destinationFolder, newFileName);
        return copy;
    }
    
    /**
     * Method finds all tags in pattern file - finds all text fragments in '<tag>' format
     * Method clears all already set tags and replaces them with found tags
     * !WARNING!
     * Dont write '<' or '>' in pattern file if you plan to use this method, otherwise it WILL break your document
     * @return Container with all found tags
     * @throws IOException 
     */
    public TagContainer findAllTagsInPattern() throws IOException{
        this.tags.clear();
        String contents = this.getPatternContents();
        boolean end = false;
        while(!end) {
            int tagStartIndex = findTagStartIndex(contents);
            int tagEndIndex = findTagEndIndex(contents);
            if(tagStartIndex != -1 && tagEndIndex != -1 && tagStartIndex < tagEndIndex){
                String tag = contents.substring(tagStartIndex + 4, tagEndIndex);
                Tag newTag = new Tag("<" + tag + ">", "");
                this.tags.add(newTag);
                contents = contents.substring(tagEndIndex + 4);
            }
            else{
                end = true;
            }
        }
        return this.tags;
    }
    
    private String getPatternContents() throws IOException{
        String contents = this.patternFile.getFileContents();
        return contents;
    }
}
