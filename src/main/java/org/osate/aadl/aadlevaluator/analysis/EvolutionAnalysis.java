package org.osate.aadl.aadlevaluator.analysis;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.osate.aadl.aadlevaluator.report.EvolutionReport;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.evolution.FuncionalityUtils;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.project.Property;

public class EvolutionAnalysis 
{
    private final List<PropertyConfiguration> properties;
    
    public EvolutionAnalysis()
    {
        properties = new LinkedList<>();
    }
    
    public EvolutionAnalysis setProperties( List<PropertyConfiguration> properties )
    {
        this.properties.addAll( properties );
        return this;
    }
    
    public List<Analysis> analysis( EvolutionReport originalReport , EvolutionReport evolutionReport ) throws Exception
    {
        List<Analysis> results = new LinkedList<>();
        
        results.add( new NumberAnalysis( 
            "performance" , 
            "" ,
            "factor" , 
            "Factor" , 
            originalReport.getFactor( "total" ).doubleValue() , 
            evolutionReport.getFactor( "total" ).doubleValue()
        ) );
        
        system( results , evolutionReport.getEvolution() );
        properties( results , evolutionReport.getEvolution() );
        
        return results;
    }
    
    private void system( List<Analysis> results , Evolution evolution ) throws Exception
    {
        if( evolution == null )
        {
            return ;
        }
        
        Component original = evolution.getSystem();
        Component evoluted = evolution.getSystemWidthChanges();
        
        results.add( new NumberAnalysis( 
            "maintainability" , 
            "modifiability" ,
            "subcomponents" , 
            "Subcomponents" , 
            original.getSubcomponentsAll().size() , 
            evoluted.getSubcomponentsAll().size()
        ) );
        
        results.add( new NumberAnalysis( 
            "maintainability" , 
            "modifiability" ,
            "connections" , 
            "Connections" , 
            original.getConnectionsAll().size() , 
            evoluted.getConnectionsAll().size()
        ) );
    }
    
    private void properties( List<Analysis> results , Evolution evolution ) throws Exception
    {
        if( evolution == null )
        {
            return ;
        }
        
        funcionalities( results , evolution );
        
        Component older = joinDeclarations( evolution.getDeclarations() );
        Component newer = joinCandidates( evolution.getCandidates() );
        
        if( properties.isEmpty() )
        {
            properties.addAll( AnalysisUtils.getDefaultProperties() );
        }
        
        for( PropertyConfiguration prop : properties )
        {
            List<Property> p1s = older.getProperty( prop.getPropertyName() );
            List<Property> p2s = newer.getProperty( prop.getPropertyName() );
            
            if( p1s.isEmpty() && p2s.isEmpty() )
            { 
                continue ;
            }
            
            Property p1 = p1s.isEmpty() 
                ? create( p2s.get( 0 ) )
                : p1s.get( 0 );
            
            Property p2 = p2s.isEmpty() 
                ? create( p1s.get( 0 ) )
                : p2s.get( 0 );
            
            results.add( 
                new PropertyAnalysis( 
                    "Property" , 
                    "" , 
                    prop.getDisplay() ,
                    prop.getDisplay() ,
                    p1 , 
                    p2 
                )
            );
        }
    }
    
    private Component joinDeclarations( List<Declaration> declarations )
    {
        if( declarations.isEmpty() )
        {
            return null;
        }
        else if( declarations.size() == 1 )
        {
            return declarations.get( 0 ).getComponent();
        }
        
        Component component = new Component();
        
        for( Declaration declaration : declarations )
        {
            join( component , declaration.getComponent() );
        }
        
        return component;
    }
    
    private Component joinCandidates( List<Candidate> candidates )
    {
        if( candidates.isEmpty() )
        {
            return null;
        }
        else if( candidates.size() == 1 )
        {
            return candidates.get( 0 ).getComponent();
        }
        
        Component component = new Component();
        
        for( Candidate candidate : candidates )
        {
            join( component , candidate.getComponent() );
        }
        
        return component;
    }
    
    private void join( Component c1 , Component c2 )
    {
        if( c1.getProperties().isEmpty() )
        {
            for( Property p : c1.getProperties() )
            {
                c2.add( p.clone() );
            }
        }
        else
        {
            for( Property p : c1.getProperties() )
            {
                List<Property> exists = c2.getProperty( p.getName() );

                if( !exists.isEmpty() )
                {
                    c2.add( p.clone() );
                }
                else
                {
                    // como resolver o conflito?
                }
            }
        }
    }
    
    private Property create( Property example )
    {
        switch( example.getValueType() )
        {
            case Property.TYPE_ARRAY: 
                return new Property( example.getName() , "()" );
            case Property.TYPE_MIN_MAX: 
                return new Property( example.getName() , "0 .. 0" );
            case Property.TYPE_OBJECT: 
                return new Property( example.getName() , "[]" );
            case Property.TYPE_VALUE: 
                return new Property( example.getName() , "0" );
            case Property.TYPE_STRING: 
                return new Property( example.getName() , "\"\"" );
            default:
                return new Property( example.getName() , "" );
        }
    }
    
    private void funcionalities( List<Analysis> results , Evolution evolution )
    {
        for( Candidate candidate : evolution.getCandidates() )
        {
            List<Property> list = candidate
                .getComponent()
                .getProperty( FuncionalityUtils.PROPERTY_FUNCIONALITY_1 , FuncionalityUtils.PROPERTY_FUNCIONALITY_2 );
            
            if( list.isEmpty() )
            {
                continue ;
            }
            
            Property property = list.get( 0 );
            
            // TODO: arrumar isso aqui
            for( String func : candidate.getFuncionalities().keySet() )
            {
                Map<String,Property> subsA = getFuncionalities( evolution , func );
                if( subsA == null )
                {
                    continue ;
                }
                
                Map<String,Property> subsB = getFuncionalities( property  , func );
                if( subsB == null )
                {
                    continue ;
                }
                
                results.add( new FuncionalityAnalysis( 
                    "Performance" , 
                    "" , 
                    func , 
                    func , 
                    subsA , 
                    subsB 
                ) );
            }
        }
    }
    
    private Map<String,Property> getFuncionalities( Property property , String name )
    {
        if( property.getValueType() != Property.TYPE_ARRAY )
        {
            return null;
        }
        
        for( String value : property.getValueArray() )
        {
            Property p = new Property( "" , value );
            Map<String,Property> sub = p.getValueObject();
            
            if( sub != null 
                && sub.containsKey( "name" ) 
                && name.equalsIgnoreCase( sub.get( "name" ).getValue() ) )
            {
                return sub;
            }
        }
        
        return null;
    }
    
    
    private Map<String,Property> getFuncionalities( Evolution evolution , String name )
    {
        for( Declaration declaration : evolution.getDeclarations() )
        {
            Component component = declaration.getComponent();
            Property property = component.getProperty( 
                    FuncionalityUtils.PROPERTY_FUNCIONALITY_1 , 
                    FuncionalityUtils.PROPERTY_FUNCIONALITY_2 
                ).get( 0 );
            
            Map<String,Property> results = getFuncionalities( property , name );
            
            if( results != null )
            {
                return results;
            }
        }
        
        return null;
    }
    
}