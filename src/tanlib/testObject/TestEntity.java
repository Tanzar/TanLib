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
public class TestEntity {
    private int i;
    private boolean k;
    private String z;

    public TestEntity(int i) {
        this.i = i;
        this.k = true;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public boolean isK() {
        return k;
    }

    public void setK(boolean k) {
        this.k = k;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }
    
    
}
