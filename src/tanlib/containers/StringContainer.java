/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.containers;

import java.util.List;
import tanlib.abstracts.container.ListContainer;

/**
 *
 * @author Tanzar
 */
public class StringContainer extends ListContainer<String>{

    @Override
    public String[] toArray(List<String> data) {
        String[] resultArray = new String[data.size()];
        for(int i = 0; i < resultArray.length; i++){
            resultArray[i] = data.get(i);
        }
        return resultArray;
    }
    
}
