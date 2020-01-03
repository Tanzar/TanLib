/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.abstracts.container;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tanzar
 */
public abstract class ListContainer<itemType> {
    
    private List<itemType> data;
    
    public ListContainer(){
        this.data = new ArrayList<itemType>();
    }
    
    public ListContainer(List<itemType> data){
        this.data = data;
    }
    
    public void add(itemType item){
        this.data.add(item);
    }
    
    public itemType get(Integer index){
        return data.get(index);
    }
    
    public void remove(Integer index){
        itemType item = get(index);
        data.remove(item);
    }
    
    public void change(int index, itemType item){
        itemType[] tmpArray = toArray();
        data.clear();
        for(int i = 0; i < tmpArray.length; i++){
            if(i != index){
                data.add(tmpArray[i]);
            }
            else{
                data.add(item);
            }
        }
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
    
    public abstract itemType[] toArray(List<itemType> data);
}
