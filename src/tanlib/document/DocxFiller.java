/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import tanlib.containers.StringContainer;
import tanlib.exceptions.DocxException;

/**
 *
 * @author Tanzar
 */
public class DocxFiller {
    
    private TagContainer tags;
    private DocxFile patternFile;
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
    
    public DocxFile fillFile(String filledFilename) throws IOException, DocxException{
        String destinationFolder = patternFile.getFolderPath();
        filledFile = this.patternFile.copy(destinationFolder, filledFilename);
        fillTags(filledFile);
        return filledFile;
    }
    
    private void fillTags(DocxFile fileToFill) throws IOException{
        String path = fileToFill.getFolderPath();
        File fileToUnzip = fileToFill.getFile();
        path = unzipFile(fileToUnzip.getAbsolutePath(), path);
        String contents = readFile(path);
        StringContainer splitContents = splitContents(contents);
        splitContents = identifyTags(splitContents);
        String[] documentInputs = prepareFileInputs(splitContents);
        writeFile(path, documentInputs);
        replaceContents(filledFile, path);
        File documentXml = new File(path);
        documentXml.delete();
    }
    
    private StringContainer splitContents(String contents){
        StringContainer container = new StringContainer();
        String newContentsFragment = "";
        int index = 0;
        boolean end = false;
        while(!end) {
            index = contents.indexOf("&lt;");
            if(index != -1){
                newContentsFragment = contents.substring(0, index);
                container.add(newContentsFragment);
                contents = contents.substring(index);
                newContentsFragment = findTagEnd(contents);
                container.add(newContentsFragment);
                index = newContentsFragment.length() + 6;
                contents = contents.substring(index);
            }
            else{
                end = true;
                container.add(contents);
            }
        }
        return container;
    }
    
    private String findTagEnd(String contents){
        String result = "";
        int index = 0;
        contents = contents.substring(4);
        contents = "<" + contents;
        index = contents.indexOf("&gt;");
        if(index != -1){
            result = contents.substring(0, index + 4);
            result = result.substring(0, index) + ">";
        }
        else{
            result = contents;
        }
        return result;
    }
    
    private StringContainer identifyTags(StringContainer splitedContents){
        for(int i = 0; i < splitedContents.size(); i++){
            String currentContents = splitedContents.get(i);
            int j = 0;
            Tag[] tags = this.tags.toArray();
            boolean found = false;
            while(!found && j < tags.length){
                Tag currentTag = tags[j];
                if(currentContents.equals(currentTag.getFormat())){
                    splitedContents.change(i, currentTag.getStringToWrite());
                }
                j++;
            }
        }
        return splitedContents;
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
    
    private void replaceContents(DocxFile fileToChange, String documentXmlPath){
        Path myFilePath = Paths.get(documentXmlPath);
        File filledFile = fileToChange.getFile();
        Path zipFilePath = Paths.get(filledFile.getAbsolutePath());
        try( FileSystem fs = FileSystems.newFileSystem(zipFilePath, null) ){
            Path fileInsideZipPath = fs.getPath("word/document.xml");
            Files.deleteIfExists(fileInsideZipPath);
            Files.copy(myFilePath, fileInsideZipPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private String unzipFile(String filePath, String oPath) throws FileNotFoundException, IOException{
        FileInputStream fis = null;
        ZipInputStream zipIs = null;
        ZipEntry zEntry = null;
        String unzippedFilePath = "";
        fis = new FileInputStream(filePath);
        zipIs = new ZipInputStream(new BufferedInputStream(fis));
        boolean found = false;
        while (found == false && (zEntry = zipIs.getNextEntry()) != null) {
            String filepath = unzip(zEntry, zipIs, oPath);
            if(!filepath.isEmpty()){
                unzippedFilePath = filepath;
                found = true;
            }
        }
        zipIs.close();
        fis.close();
        return unzippedFilePath;
    }
    
    private String unzip(ZipEntry zEntry, ZipInputStream inputStream, String destinationFolder) throws FileNotFoundException, IOException{
        String newFilePath = "";
        String filename = zEntry.getName();
        byte[] buffer = new byte[1024];
        if(filename.contains("word/document.xml")){
            newFilePath = destinationFolder + File.separator + "document.xml";
            File newFile = new File(newFilePath);
            new File(newFile.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);             
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close(); 
        }
        return newFilePath;
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
    
    public void clearAfterYourself(){
        if(filledFile != null){
            File filledFile = this.filledFile.getFile();
            filledFile.delete();
            this.filledFile = null;
        }
    }
}
