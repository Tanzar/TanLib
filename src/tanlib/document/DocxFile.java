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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import tanlib.exceptions.DocxException;

/**
 *
 * @author Tanzar
 */
public class DocxFile {
    
    private File docx;
    
    public DocxFile(File docx) throws DocxException{
        checkFile(docx);
        this.docx = docx;
    }
    
    public DocxFile(String filePath) throws DocxException{
        File file = new File(filePath);
        checkFile(file);
        this.docx = file;
    }
    
    private void checkFile(File docx) throws DocxException {
        String path = docx.getAbsolutePath();
        checkIfFilePathIsEmpty(path);
        checkFileExtension(path);
        checkIfExists(docx);
    }
    
    private void checkIfFilePathIsEmpty(String path) throws DocxException{
        if(path.trim().isEmpty()){
            throw new DocxException("Path cannot be empty.");
        }
    }
    
    private void checkFileExtension(String path) throws DocxException{
        int index = path.lastIndexOf(".");
        String extension = path.substring(index + 1);
        if(!extension.equals("docx")){
            throw new DocxException("Only files *.docx are allowed");
        }
    }
    
    private void checkIfExists(File file) throws DocxException{
        if(!file.exists()){
            throw new DocxException("File don't exist");
        }
    }
    
    public File getFile(){
        return this.docx;
    }
    
    /**
     * Method finds folder which contains docx file.
     * @return Local path, where project is root
     */
    public String getLocalFolderPath(){
        String path = docx.getPath();
        String docxName = docx.getName();
        int endIndex = path.length() - docxName.length() - 1;
        path = path.substring(0, endIndex);
        return path;
    }
    
    /**
     * Method finds folder which contains docx file.
     * @return System path, where system root is start of path
     */
    public String getSystemFolderPath(){
        String path = docx.getPath();
        String docxName = docx.getName();
        int endIndex = path.length() - docxName.length() - 1;
        path = path.substring(0, endIndex);
        return path;
    }
    
    public DocxFile copy(String destinationFolder, String filename) throws IOException, DocxException{
        String destinationPath = destinationFolder + File.separator + filename;
        if(!filename.contains(".docx")){
            destinationPath += ".docx";
        }
        File dest = copy(destinationPath);
        DocxFile newFile = new DocxFile(dest);
        return newFile;
    }
    
    private File copy(String destinationPath) throws IOException{
        File destinationFile = new File(destinationPath);
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(this.docx);
            output = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } finally {
            input.close();
            output.close();
        }
        return destinationFile;
    }
    
    /**
     * Method extracts docx contents in document.xml file to selected folder. Document.xml contains text from docx file.
     * @param outputFolderPath path to folder where file will be extracted.
     * @return path to extracted file
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public String extractDocumentXML(String outputFolderPath) throws FileNotFoundException, IOException{
        ZipEntry zEntry = null;
        String unzippedFilePath = "";
        FileInputStream fis = new FileInputStream(this.docx.getPath());
        ZipInputStream zipIs = new ZipInputStream(new BufferedInputStream(fis));
        boolean found = false;
        while (found == false && (zEntry = zipIs.getNextEntry()) != null) {
            String filepath = unzip(zEntry, zipIs, outputFolderPath);
            if(!filepath.isEmpty()){
                unzippedFilePath = filepath;
                found = true;
            }
        }
        zipIs.close();
        fis.close();
        return unzippedFilePath;
    }
    
    /**
     * Method extracts docx contents in document.xml file to same location as docx. Document.xml contains text from docx file.
     * @return path to extracted file
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public String extractDocumentXML() throws FileNotFoundException, IOException{
        ZipEntry zEntry = null;
        String unzippedFilePath = "";
        FileInputStream fis = new FileInputStream(this.docx.getPath());
        ZipInputStream zipIs = new ZipInputStream(new BufferedInputStream(fis));
        boolean found = false;
        while (found == false && (zEntry = zipIs.getNextEntry()) != null) {
            String filepath = unzip(zEntry, zipIs, this.getLocalFolderPath());
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
    
    /**
     * Method changes contents of docx file.
     * @param documentXmlPath Path to document.xml file which should be extracted from docx file
     */
    public void replaceContents(String documentXmlPath){
        Path myFilePath = Paths.get(documentXmlPath);
        Path zipFilePath = Paths.get(docx.getPath());
        try( FileSystem fs = FileSystems.newFileSystem(zipFilePath, null) ){
            Path fileInsideZipPath = fs.getPath("word/document.xml");
            Files.deleteIfExists(fileInsideZipPath);
            Files.copy(myFilePath, fileInsideZipPath);
        } catch (IOException e) {
        }
    }
}
