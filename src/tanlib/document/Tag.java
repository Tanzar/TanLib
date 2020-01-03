/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.document;

/**
 *
 * @author Tanzar
 */
public class Tag {
    
    private String format;
    private String stringToWrite;
    
    public Tag(String format, String stringToWrite){
        this.format = format;
        this.stringToWrite = stringToWrite;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getStringToWrite() {
        return stringToWrite;
    }

    public void setStringToWrite(String stringToWrite) {
        this.stringToWrite = stringToWrite;
    }
}
