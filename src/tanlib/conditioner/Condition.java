/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.conditioner;

import java.lang.reflect.Method;

/**
 * Class allows creating dynamic condition instruction (if)
 * It uses three values:
 * - method name - method called on parameter object in isMet()
 * - operator - operation for contition
 * - value - value to which returned value from method name will be compared
 * 
 * It can be seen as:
 * <value returned from method name> operator <value>
 * 
 * @author Tanzar
 */
public class Condition {
    private final Object value;
    private final Operators operator;
    private final String methodName;
    
    /**
     * Allows setup of contition:
     * <value returned from method name> operator <value>
     * 
     * @param methodName - method called on parameter object in isMet()
     * @param operator - operation for condition
     * @param value - value to which returned value from method name will be compared
     */
    public Condition(String methodName, Operators operator, Object value){
        this.methodName = methodName;
        this.operator = operator;
        this.value = value;
    }
    
    /**
     * Method checks if object meets conditions set in constructor,
     * if anything is wrong it returns false
     * @param object - object to check
     * @return true if object meets set condition, false if doesn't or any error happened
     */
    public boolean isMet(Object object){
        Class c = object.getClass();
        Class[] parameterTypes = new Class[0];
        try {
            Method method = c.getMethod(methodName, parameterTypes);
            Object returnedValue = method.invoke(object, parameterTypes);
            return this.operator.compare(returnedValue, this.value);
            
        } catch (Exception ex) {
            return false;
        }
    }
}
