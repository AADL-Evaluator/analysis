package org.osate.aadl.aadlevaluator.analysis;

import java.text.MessageFormat;
import org.osate.aadl.evaluator.unit.UnitUtils;

public class UnitAnalysis extends Analysis<String>
{
    
    public UnitAnalysis( final String charact , final String sub , final String name , final String valueA , final String valueB )
    {
        super( charact , sub , name , valueA , valueB );
    }
    
    public double getDiference()
    {
        return UnitUtils.getValue( valueB ) - UnitUtils.getValue( valueA );
    }
    
    public double getPercentage()
    {
        return UnitUtils.getValue( valueB ) / UnitUtils.getValue( valueB );
    }
    
    @Override
    public String toString() 
    {
        if( UnitUtils.getValue( valueA ) == UnitUtils.getValue( valueB ) )
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
        
        String unit = UnitUtils.getValueAndUnit( valueA )[0];
        
        return MessageFormat.format( diff < 0
            ? "The {0} has {1} {2} more than original (equivalent to {3}%)."
            : "The {0} has {1} {2} less than original (equivalent to {3}%)."
            , name , diff , unit , perc
        );
    }
    
}
