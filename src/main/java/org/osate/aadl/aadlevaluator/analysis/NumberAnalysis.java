package org.osate.aadl.aadlevaluator.analysis;

import java.text.MessageFormat;

public class NumberAnalysis extends Analysis<Double>
{

    public NumberAnalysis( final String charact , final String sub , final String name , final String title , final double A , final double B ) 
    {
        super( charact , sub , name , title , A , B );
    }
    
    public double getDiference()
    {
        return valueB - valueA;
    }
    
    public double getPercentage()
    {
        return valueA == 0 
            ? 0
            : valueB / valueA;
    }

    @Override
    public String toString() 
    {
        if( (double) valueA == (double) valueB )
        {
            return MessageFormat.format(
                "The {0} are equals."
                , name
            );
        }
        
        double diff = getDiference();
        double perc = getPercentage();
        
        perc = perc < 1 
            ? 1 - perc      // 1 - 0,89 = 11%
            : perc - 1;     // 1,09 - 1 =  9%
        
        perc *= 100;
        
        return MessageFormat.format( diff < 0
            ? "The {0} has {1} more than original (equivalent to {2}%)."
            : "The {0} has {1} less than original (equivalent to {2}%)."
            , name , (diff < 0 ? diff * -1 : diff) , perc
        );
    }
    
}