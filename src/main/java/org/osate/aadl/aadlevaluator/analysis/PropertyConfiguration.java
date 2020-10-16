package org.osate.aadl.aadlevaluator.analysis;

public class PropertyConfiguration 
{
    public static final String CONFLICT_SUM = "SUM";
    public static final String CONFLICT_IGNORE = "IGNORE";
    
    private String propertyName;
    private String display;
    private String conflict;

    public PropertyConfiguration() 
    {
        // do nothing
    }

    public PropertyConfiguration( String propertyName , String display , String conflict )
    {
        this.propertyName = propertyName;
        this.display = display;
        this.conflict = conflict;
    }
    
    public PropertyConfiguration( String propertyName , String display )
    {
        this.propertyName = propertyName;
        this.display = display;
    }

    public PropertyConfiguration( String propertyName )
    {
        this.propertyName = propertyName;
        this.display = propertyName;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public PropertyConfiguration setPropertyName( String propertyName )
    {
        this.propertyName = propertyName;
        return this;
    }

    public String getDisplay()
    {
        return display;
    }

    public PropertyConfiguration setDisplay( String display )
    {
        this.display = display;
        return this;
    }

    public String getConflict()
    {
        return conflict;
    }

    public PropertyConfiguration setConflict( String conflict )
    {
        this.conflict = conflict;
        return this;
    }
    
}