package org.osate.aadl.aadlevaluator.analysis;

import java.text.MessageFormat;
import java.util.Objects;

/*
    This scenario has 2 devices more that original. 
    Peso de 30gramas a mais. 
    Consumo de barramento maior em 30%
*/
public abstract class Analysis<T>
{
    protected final String characteristic;
    protected final String subcharacteristic;
    protected final String name;
    protected final T valueA;
    protected final T valueB;

    public Analysis( String charact , String sub , String name , T valueA , T valueB )
    {
        this.characteristic = charact;
        this.subcharacteristic = sub;
        this.name = name;
        this.valueA = valueA;
        this.valueB = valueB;
    }

    public String getCharacteristic()
    {
        return characteristic;
    }

    public String getSubcharacteristic()
    {
        return subcharacteristic;
    }

    public String getName()
    {
        return name;
    }
    
    public T getValueA()
    {
        return valueA;
    }

    public T getValueB()
    {
        return valueB;
    }
    
    public boolean areTheyEquals() 
    {
        return Objects.equals( valueB , valueA );
    }

    @Override
    public String toString() 
    {
        return MessageFormat.format( areTheyEquals() 
                ? "The {0} is equal." 
                : "The {0} is difference." 
            , name
        );
    }
    
}