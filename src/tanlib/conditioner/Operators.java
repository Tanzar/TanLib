/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.conditioner;

import java.math.BigDecimal;

/**
 *
 * @author Tanzar
 */
public enum Operators {
    less(-1),
    lessOrSame(-1, 0),
    same(0),
    different(-1, 1),
    moreOrSame(0, 1),
    more(1);
    
    private final int[] acceptableCompareResults;
    
    Operators(int ... compareResults){
        int length = compareResults.length;
        this.acceptableCompareResults = new int[length];
        for(int i = 0; i < length; i++){
            this.acceptableCompareResults[i] = compareResults[i];
        }
    }
    
    public boolean compare(Object first, Object second){
        if(first instanceof Number && second instanceof Number){
            return this.compareAsNumbers(first, second);
        }
        else{
            return this.compareAsObjects(first, second);
        }
    }
    
    private boolean compareAsNumbers(Object first, Object second){
        BigDecimal a = new BigDecimal(first.toString());
        BigDecimal b = new BigDecimal(second.toString());
        int compareResult = a.compareTo(b);
        for(int value: this.acceptableCompareResults){
            if(compareResult == value){
                return true;
            }
        }
        return false;
    }
    
    private boolean compareAsObjects(Object first, Object second){
        switch(this){
            case same:
                return first.equals(second);
            case different:
                return !first.equals(second);
            default:
                return false;
        }
    }
}
