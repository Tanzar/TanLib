/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.random;

import java.util.Random;

/**
 * Class for randomized operations
 * @author Tanzar
 */
public class Randomizer {
    
    /**
     * Generates random number between min and max
     * @param min minimal number that can be generated
     * @param max maximal number that can be generated
     * @return generated number
     */
    public static Integer random(Integer min, Integer max){
        if(min < max){
            Random generator = new Random();
            Integer result = min + generator.nextInt(max - min);
            return result;
        }
        else{
            return 0;
        }
    }
    
    /**
     * Method returns true based on pertentage chance
     * @param chance chance in %, chance must be more then 0 but less then 100
     * @return true or false based on chance
     */
    public static boolean randomByChance(Double chance){
        if(chance <= 100 && chance > 0){
            boolean result = false;
            Integer chanceNumber = doubleToInt(chance);
            Integer oppositeChance = doubleToInt(100.0 - chance);
            Integer re = random(0, (chanceNumber + oppositeChance));
            if(re <= chanceNumber){
                result = true;
            }
            return result;
        }
        else{
            return false;
        }
    }
    
    private static Integer doubleToInt(Double number){
        String numberString = number.toString();
        int index = numberString.indexOf(".");
        numberString = numberString.substring(index + 1);
        int length = numberString.length();
        double multiplier = Math.pow(10, length);
        return (int) (number * multiplier);
    }
}
