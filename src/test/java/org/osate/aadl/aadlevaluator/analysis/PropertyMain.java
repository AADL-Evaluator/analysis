package org.osate.aadl.aadlevaluator.analysis;

public class PropertyMain 
{
    
    public static void main( String[] args ) throws Exception
    {
        for( PropertyConfiguration conf : AnalysisUtils.getDefaultProperties() )
        {
            System.out.println( "property.: " + conf.getPropertyName() );
            System.out.println( "name.....: " + conf.getDisplay()  );
            System.out.println( "conflict.: " + conf.getConflict() );
            System.out.println( "---" );
        }
    }
    
}