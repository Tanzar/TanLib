/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import tanlib.exceptions.DocxException;
import tanlib.exceptions.EmptyFilePathException;
import tanlib.exceptions.NotExistException;
import tanlib.exceptions.WrongFileTypeException;

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
    
    public String getFolderPath(){
        String path = docx.getAbsolutePath();
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
        File dest = new File(destinationPath);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(this.docx);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
        return dest;
    }
    
    
    
}
