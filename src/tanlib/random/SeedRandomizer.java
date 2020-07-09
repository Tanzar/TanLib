/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.random;

import java.util.Random;

/**
 *
 * @author Tanzar
 */
public class SeedRandomizer {
    private int state;
    private int seed;
    
    public SeedRandomizer(int seed){
        this.state = seed;
        this.seed = seed;
    }

    public SeedRandomizer() {
        Random rand = new Random();
        this.seed = rand.nextInt();
        this.state = this.seed;
    }
    
    public int random(int min, int max){
        if(min == max){
            return min;
        }
        int a = 1103515245;
        int b = 12345;
        int m = (int) Math.pow(2, 32);
        state = (a * state + b) % m;
        state = Math.abs(state);
        int number = (state % (max - min + 1));
        number = number + min;
        if(number < 0){
            number = Math.abs(number);
        }
        return number;
    }
    
    public static int generateSeed(){
        Random rand = new Random();
        return rand.nextInt();
    }
    
    public int getSeed(){
        return this.seed;
    }
    
    public void setSeed(int seed){
        this.seed = seed;
        this.state = seed;
    }
}
