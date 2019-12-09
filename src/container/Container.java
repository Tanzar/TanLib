/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package container;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author User
 */
public class Container<itemType> {
    
    private List<itemType> data;
    
    public Container(){
        this.data = new ArrayList<itemType>();
    }
    
    public Container(List<itemType> data){
        this.data = data;
    }
    
    public void add(itemType item){
        this.data.add(item);
    }
    
    public itemType get(Integer index){
        return data.get(index);
    }
    
    
    
}
