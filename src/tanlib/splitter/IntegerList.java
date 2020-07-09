/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.splitter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tanzar
 */
public class IntegerList {
    private List<Integer> list;
    
    public IntegerList(){
        this.list = new ArrayList<Integer>();
    }
    
    public void add(Integer integer){
        this.list.add(integer);
    }
    
    public Integer get(Integer index){
        return this.list.get(index);
    }
    
    public Integer getAndRemove(Integer index){
        Integer item = this.list.get(index);
        this.list.remove(item);
        return item;
    }
    
    public Integer[] toArray(){
        Integer[] array = new Integer[this.list.size()];
        this.list.toArray(array);
        return array;
    }
    
    public boolean isEmpty(){
        if(this.list.size() == 0){
            return true;
        }
        else{
            return false;
        }
    }
    
    public int size(){
        return this.list.size();
    }
    
    public void clear(){
        this.list.clear();
    }
    
    @Override
    public String toString(){
        String output = "Items: ";
        for(int i = 0; i < this.list.size(); i++){
            if(i != this.list.size() - 1){
                output += list.get(i) + ", ";
            }
            else{
                output += list.get(i);
            }
        }
        return output;
    }
}
