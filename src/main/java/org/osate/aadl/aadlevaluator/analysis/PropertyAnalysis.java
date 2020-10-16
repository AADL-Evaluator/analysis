package org.osate.aadl.aadlevaluator.analysis;

import java.util.HashMap;
import java.util.Map;
import org.osate.aadl.evaluator.project.Property;

public class PropertyAnalysis extends Analysis<Property>
{
    
    public PropertyAnalysis( final String charact , final String sub , final String name , final Property valueA , final Property valueB )
    {
        super( charact , sub , name , valueA , valueB );
    }
    
    public int getType()
    {
        return valueA != null 
            ? valueA.getValueType()
            : valueB != null
                ? valueB.getValueType()
                : 0;
    }
    
    public Map<String,UnitAnalysis> getMinAndMaxAnalysis()
    {
        final Map<String,String> valuesA = valueA.getValueMinAndMax();
        final Map<String,String> valuesB = valueB.getValueMinAndMax();
        
        final Map<String,UnitAnalysis> results = new HashMap<>();
        
        for( Map.Entry<String,String> entry : valuesA.entrySet() )
        {
            String vA = entry.getValue();
            String vB = valuesB.get( entry.getKey() );
            
            results.put( entry.getKey() , new UnitAnalysis( 
                characteristic , 
                subcharacteristic ,
                entry.getKey() + " value of " + name , 
                vA , 
                vB 
            ) );
        }
        
        return results;
    }
    
    public ArrayAnalysis getArrayAnalysis()
    {
        return new ArrayAnalysis( 
            characteristic , 
            subcharacteristic ,
            name ,
            valueA.getValueArray() , 
            valueB.getValueArray() 
        );
    }
    
    public Analysis getStringAnalysis()
    {
        return new StringAnalysis( 
            characteristic ,
            subcharacteristic ,
            name ,
            valueA.getValue() , 
            valueB.getValue() 
        );
    }
    
    public Analysis getValueAnalysis()
    {
        try
        {
            return new NumberAnalysis( 
                characteristic , 
                subcharacteristic ,
                name ,
                Double.parseDouble( valueA.getValue() ) , 
                Double.parseDouble( valueB.getValue() )
            );
        }
        catch( Exception err )
        {
            return new UnitAnalysis( 
                characteristic , 
                subcharacteristic ,
                name ,
                valueA.getValue() , 
                valueB.getValue() 
            );
        }
    }
    
}