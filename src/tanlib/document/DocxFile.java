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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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
        String path = docx.getAbsolutePath();
        String docxName = docx.getName();
        int endIndex = path.length() - docxName.length() - 1;
        path = path.substring(0, endIndex);
        return path;
    }
    
    public String getLocalFilePath(){
        return this.docx.getPath();
    }
    
    public String getSystemFilePath(){
        return this.docx.getAbsolutePath();
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
    public void replaceContents(String header, String newContents){
        try{
            String xmlPath = this.rewriteDocumentXml(header, newContents);
            this.replaceDocumentXml(xmlPath);
        }
        catch (IOException ex) {
            Logger.getLogger(DocxFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void replaceDocumentXml(String newXmlPath) throws IOException{
        Path xmlFilePath = Paths.get(newXmlPath);
        Path docxFilePath = Paths.get(docx.getPath());
        try( FileSystem fs = FileSystems.newFileSystem(docxFilePath, null) ){       //don't change
            Path xmlInsideDocxPath = fs.getPath("word/document.xml");
            Files.deleteIfExists(xmlInsideDocxPath);
            Files.copy(xmlFilePath, xmlInsideDocxPath);
        } catch (IOException e) {}                                                  //otherwise it breaks
        File file = new File(newXmlPath);
        file.delete();
    }
    
    private String rewriteDocumentXml(String header, String newContents) throws IOException{
        String xmlPath = this.extractDocumentXML();
        String[] dataToWrite = new String[2];
        dataToWrite[0] = header;
        dataToWrite[1] = newContents;
        writeToFile(xmlPath, dataToWrite);
        return xmlPath;
    }
    
    private void writeToFile(String filePath, String[] dataToWrite) throws IOException{
        FileWriter fileWriter = new FileWriter(filePath);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(int i = 0; i < dataToWrite.length; i++){
            printWriter.println(dataToWrite[i]);
        }
        printWriter.close();
    }
    
    /**
     * Method converts this DocxFile to PDF
     * @param pdfPath path to new file
     * @return output PDF file
     */
    public File convertToPDF(String pdfPath){
        try {
            InputStream doc = new FileInputStream(this.docx);
            XWPFDocument document = new XWPFDocument(doc);
            PdfOptions options = PdfOptions.create();
            File outputFile = new File(pdfPath);
            OutputStream out = new FileOutputStream(outputFile);
            PdfConverter.getInstance().convert(document, out, options);
            return outputFile;
        } catch (IOException ex) {
            File outputFile = new File(pdfPath);
            return outputFile;
        }
    }
    
    public void delete(){
        this.docx.delete();
    }
    
    public String getFileContents() throws IOException{
        String documentXmlPath = this.extractDocumentXML();
        String contents = readFile(documentXmlPath);
        File documentXml = new File(documentXmlPath);
        documentXml.delete();
        return contents;
    }
    
    private String readFile(String filePath){
        String[] data = new String[2];
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            data[0] = myReader.nextLine();
            data[1] = myReader.nextLine();
            myReader.close();
        } catch (FileNotFoundException e) {
        }
        return data[1];
    }
}
