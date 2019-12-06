/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coders;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class RemainCoder {
    
    public static String encode(Integer code, String text){
        char[] encodeArray = createArray(code);
        String encodedText = encodeByArray(text, encodeArray);
        return encodedText;
    }
    
    public static String decode(Integer code, String text){
        char[] encodeArray = createArray(code);
        String decodedText = decodeByArray(text, encodeArray);
        return decodedText;
    }
    
    private static String decodeByArray(String encodedText, char[] encodeArray){
        String result = "";
        char[] asciiArray = createAsciiArray();
        for(char decodedCharacter: encodedText.toCharArray()){
            boolean found = false;
            int index = 0;
            while(found == false && index < encodeArray.length){
                if(decodedCharacter == encodeArray[index]){
                    found = true;
                }
                else{
                    index++;
                }
            }
            if(found){
                result += asciiArray[index];
            }
            else{
                result += decodedCharacter;
            }
        }
        return result;
    }
    
    private static String encodeByArray(String text, char[] encodeArray){
        String result = "";
        char[] charArray = text.toCharArray();
        for(char encodedCharacter: charArray){
            int index = encodedCharacter - 32;
            if((index >= 0) && (index < encodeArray.length)){
                result += encodeArray[index];
            }
            else{
                result += encodedCharacter;
            }
        }
        return result;
    }
    
    private static char[] createArray(Integer kod){
        char[] asciiArray = createAsciiArray();
        char[] result = new char[asciiArray.length];
        List<Character> list = new ArrayList<Character>();
        for(int i = 0; i < result.length; i++){
            list.add(asciiArray[i]);
        }
        for(int i = 0; i < result.length; i++){
            int index = kod % (95 - i);
            result[i] = list.get(index);
            list.remove(index);
        }
        return result;
    }
    
    private static char[] createAsciiArray(){
        char[] asciiArray = new char[126 - 31];
        for(int i = 0; i < asciiArray.length; i++){
            asciiArray[i] = (char) (i+32);
        }
        return asciiArray;
    }
}
