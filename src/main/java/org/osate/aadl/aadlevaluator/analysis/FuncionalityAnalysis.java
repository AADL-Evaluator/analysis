package org.osate.aadl.aadlevaluator.analysis;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.osate.aadl.evaluator.project.Property;

public class FuncionalityAnalysis extends Analysis<Map<String,Property>>
{
    
    public FuncionalityAnalysis( final String charact , final String sub , final String name , final Map<String,Property> A , final Map<String,Property> B )
    {
        super( charact , sub , name , A , B );
    }
    
    public List<Analysis> getSubAnalysis()
    {
        List<Analysis> subs = new LinkedList<>();
        
        for( Property p1 : valueA.values() )
        {
            if( !valueB.containsKey( p1.getName() ) )
            {
                continue ;
            }
            
            Property p2 = valueA.get( p1.getName() );
            
            subs.add( new PropertyAnalysis( 
                characteristic , 
                subcharacteristic , 
                p1.getName() , 
                p1 , 
                p2 
            ) );
        }
        
        return subs;
    }

    @Override
    public String toString() 
    {
        return "funcionality name: " + name;
    }
    
}