package org.osate.aadl.aadlevaluator.analysis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class AnalysisUtils 
{
    private static final String PROPERTY_FILE = "properties.txt";
    private static final String PROPERTY_SEPARATOR = ",";
    
    public static List<PropertyConfiguration> getDefaultProperties() throws Exception
    {
        return getProperties( 
            ClassLoader.getSystemResourceAsStream( PROPERTY_FILE )
        );
    }
    
    public static List<PropertyConfiguration> getProperties( InputStream file ) throws Exception
    {
        List<PropertyConfiguration> properties = new LinkedList<>();
        
        String line;
        BufferedReader reader = new BufferedReader( new InputStreamReader( file ) );
        
        while( (line = reader.readLine()) != null )
        {
            add( properties , line );
        }
        
        return properties;
    }
    
    public static void add( List<PropertyConfiguration> properties , String line ) throws Exception
    {
        if( line == null || line.isEmpty() )
        {
            return ;
        }

        String property = line.trim();
        String name = "";
        String conflict = PropertyConfiguration.CONFLICT_IGNORE;
        
        if( line.contains( PROPERTY_SEPARATOR ) )
        {
            String parts[] = line.split( PROPERTY_SEPARATOR );
            
            property = parts[0].trim();
            name = parts.length >= 2 ? parts[1].trim() : null;
            conflict = parts.length >= 3 ? parts[2].trim() : null;
        }

        if( property.trim().isEmpty() )
        {
            return ;
        }
        else if( name == null 
            || name.trim().isEmpty() )
        {
            name = property;
        }
        
        properties.add( new PropertyConfiguration( property , name , conflict ) );
    }
    
}
