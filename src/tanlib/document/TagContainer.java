/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.document;

import java.util.HashSet;
import tanlib.abstracts.container.HashSetContainer;

/**
 *
 * @author Tanzar
 */
public class TagContainer extends HashSetContainer<Tag>{
    
    public TagContainer(){
        super();
    }
    
    @Override
    public Tag[] toArray(HashSet<Tag> data) {
        Tag[] resultArray = new Tag[data.size()];
        data.toArray(resultArray);
        return resultArray;
    }
    
}
