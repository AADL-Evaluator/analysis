package org.osate.aadl.aadlevaluator.analysis;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

public class ArrayAnalysis extends Analysis<List<String>>
{
    
    public ArrayAnalysis(  final String charact , final String sub , final String name , final List<String> valueA, final List<String> valueB )
    {
        super( charact , sub , name , valueA , valueB );
    }
    
    public List<String> getRemovedElements()
    {
        List<String> results = new LinkedList<>();
        
        for( String value : valueA )
        {
            if( !valueB.contains( value ) )
            {
                results.add( value );
            }
        }
        
        return results;
    }
    
    public List<String> getNewElements()
    {
        List<String> results = new LinkedList<>();
        
        for( String value : valueB )
        {
            if( !valueA.contains( value ) )
            {
                results.add( value );
            }
        }
        
        return results;
    }
    
    @Override
    public String toString() 
    {
        List<String> added = getNewElements();
        List<String> removed = getRemovedElements();
        
        if( added.isEmpty() && removed.isEmpty() )
        {
            return MessageFormat.format( "The {0} are equals." , name );
        }
        else if( added.isEmpty() )
        {
            return MessageFormat.format(
                "The {0} has deleted this elements {1}"
                , removed
            );
        }
        else if( removed.isEmpty() )
        {
            return MessageFormat.format(
                "The {0} has added this elements {1}"
                , added
            );
        }
        else
        {
            return MessageFormat.format(
                "The {0} has added this elements {1} and deleted this elements {2}"
                , added
                , removed
            );
        }
    }
    
}
