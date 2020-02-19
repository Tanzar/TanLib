/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.testObject;

/**
 * Obiekt do sprawdzania
 * @author Tanzar
 */
public class TesterClass {
    
    public TestEntity test(TestEntity i){
        i.setI(i.getI() + 1);
        TestEntity k = new TestEntity(i.getI());
        return k;
    }
    
}
