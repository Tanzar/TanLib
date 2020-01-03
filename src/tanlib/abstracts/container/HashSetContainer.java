/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.abstracts.container;

import java.util.HashSet;

/**
 *
 * @author Tanzar
 */
public abstract class HashSetContainer<itemType> {
    
    private HashSet<itemType> data;
    
    public HashSetContainer(){
        this.data = new HashSet<itemType>();
    }
    
    public HashSetContainer(HashSet<itemType> data){
        this.data = data;
    }
    
    public void add(itemType item){
        this.data.add(item);
    }
    
    public itemType get(Integer index){
        itemType[] dataArray = toArray();
        return dataArray[index];
    }
    
    public void remove(Integer index){
        itemType item = get(index);
        data.remove(item);
    }
    
    public Integer size(){
        return data.size();
    }
    
    public boolean contains(itemType item){
        return data.contains(item);
    }
    
    public void clear(){
        this.data.clear();
    }
    
    public itemType[] toArray(){
        return toArray(data);
    }
    
    public abstract itemType[] toArray(HashSet<itemType> data);
}
