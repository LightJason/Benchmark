/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++) Benchmark                      #
 * # Copyright (c) 2017, LightJason (info@lightjason.org)                               #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.benchmark.scenario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.generator.IAgentGenerator;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.benchmark.actions.CBroadcastAction;
import org.lightjason.benchmark.actions.CSendAction;
import org.lightjason.benchmark.agent.CBenchmarkAgent;
import org.lightjason.benchmark.agent.IBaseBenchmarkAgent;
import org.lightjason.benchmark.agent.IBenchmarkAgent;
import org.lightjason.benchmark.grammar.CFormularParser;
import org.lightjason.benchmark.neighborhood.ENeighborhood;
import org.lightjason.benchmark.neighborhood.INeighborhood;
import org.lightjason.benchmark.runtime.ERuntime;
import org.lightjason.benchmark.runtime.IRuntime;
import org.pmw.tinylog.Logger;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * scenario
 */
public final class CScenario implements IScenario
{
    /**
     * statistic
     */
    private final IStatistic m_statistic = new CStatistic();
    /**
     * runtime
     */
    private final IRuntime m_runtime;
    /**
     * number fo runs
     */
    private final int m_runs;
    /**
     * number of iterations on each run
     */
    private final int m_iteration;
    /**
     * warum-up simulation steps
     */
    private final int m_warmup;
    /**
     * number padding
     */
    private final String m_numberpadding;
    /**
     * map with asl pathes and generatoring functions
     */
    private final Map<IAgentGenerator<IBenchmarkAgent>, Function<Number, Number>> m_agentdefinition;


    /**
     * instantiate scneario
     *
     * @param p_file file name
     */
    private CScenario( @Nonnull final String p_file )
    {
        final ITree l_configuration = load( p_file );

        m_runs = l_configuration.<Number>getOrDefault( 1, "global", "runs" ).intValue();
        m_iteration = l_configuration.<Number>getOrDefault( 1, "global", "iterations" ).intValue();
        m_warmup = l_configuration.<Number>getOrDefault( 0, "global", "warmup" ).intValue();
        m_numberpadding = "%0" + Math.max( String.valueOf( m_runs ).length(), String.valueOf( m_warmup ).length() ) + "d";


        m_runtime = ERuntime.from( l_configuration.getOrDefault( "", "runtime", "type" ) );
        final INeighborhood l_neighborhood = ENeighborhood.from( l_configuration.getOrDefault( "", "runtime", "neighborhood" ) ).build();


        // action instantiation
        final Set<IAction> l_action = this.action( l_neighborhood );


        // create variable builder
        final IVariableBuilder l_variablebuilder = new CBenchmarkAgent.CVariableBuilder(
            Collections.unmodifiableSet(
                l_configuration.<Map<String, Object>>getOrDefault( Collections.emptyMap(), "agent", "constant" )
                               .entrySet()
                               .parallelStream()
                               .map( i -> new CConstant<>( i.getKey(), i.getValue() ) )
                               .collect( Collectors.toSet() )
            )
        );


        // agent generators
        final String l_root = Paths.get( p_file ).getParent().toString();
        m_agentdefinition = Collections.unmodifiableMap(
            l_configuration.<Map<String, Object>>getOrDefault( Collections.emptyMap(), "agent", "source" )
                    .entrySet()
                    .parallelStream()
                    .collect(
                        Collectors.toMap(
                            i -> this.generator( Paths.get( l_root, i.getKey() ).toString(), l_action, l_variablebuilder, l_neighborhood ),
                            i -> i.getValue() instanceof String ? parse( i.getValue().toString() ) : objecttolistfunction( i.getValue() )
                        ) )
        );

    }

    /**
     * converts an object to a list with values
     *
     * @param p_object configuration object
     * @return function
     */
    @SuppressWarnings( "unchecked" )
    private static Function<Number, Number> objecttolistfunction( final Object p_object )
    {
        return i -> ( (List<Number>) p_object ).get( i.intValue() - 1 );
    }

    /**
     * load configuration file
     *
     * @param p_file filename
     * @return config tree
     */
    private static ITree load( @Nonnull final String p_file )
    {
        Logger.info( "read configuration file [{0}]", p_file );
        try
            (
                final InputStream l_stream = new FileInputStream( p_file )
            )
        {
            return new ITree.CTree( new Yaml().load( l_stream ) );

        }
        catch ( final Exception l_exception )
        {
            Logger.error( "error on file reading [{0}]", l_exception.getMessage() );
            throw new RuntimeException( l_exception );
        }
    }

    @Override
    public final void store( @Nonnull final String p_filename )
    {
        Logger.info( "store measurement result in [{0}]", p_filename );

        final Map<String, Object> l_configuration = new HashMap<>();
        l_configuration.put( "runs", m_runs );
        l_configuration.put( "iteration", m_iteration );
        l_configuration.put( "warmup", m_warmup );
        l_configuration.put( "runtime", m_runtime.toString() );

        final Map<String, Object> l_result = new HashMap<>();
        l_result.put( "time", m_statistic.get() );
        l_result.put( "configuration", l_configuration );

        try
        {
            new ObjectMapper().registerModules(
                new SimpleModule().addSerializer( IStatistic.CStatisticSerializer.CLASS, new IStatistic.CStatisticSerializer() )
            ).writeValue( new File( p_filename ), l_result );
        }
        catch ( final IOException l_exception )
        {
            Logger.error( "error on storing [{0}]", l_exception.getMessage() );
            throw new UncheckedIOException( l_exception );
        }
    }

    @Override
    public final IScenario call() throws Exception
    {
        if ( m_warmup > 0 )
            IntStream.rangeClosed( 1, m_warmup )
                     .forEach( j ->
                     {
                         Logger.info( "execute warum-up step [{0}]", j );
                         IntStream.range( 0, m_iteration ).forEach( i -> this.warmup( j ) );
                     } );

        IntStream.rangeClosed( 1, m_runs )
                 .forEach( j ->
                 {
                     Logger.info( "execute iteration step [{0}]", j );
                     IntStream.range( 0, m_iteration ).forEach( i -> this.iteration( j ) );
                 } );

        return this;
    }

    /**
     * instantiate actions
     *
     * @param p_agents agents
     * @return actions
     */
    private Set<IAction> action( final INeighborhood p_agents )
    {
        Logger.info( "initialize actions" );
        return m_statistic.starttimer( "actioninitialize" ).stop(
            Collections.unmodifiableSet(
                Stream.concat(
                    Stream.concat(
                        CCommon.actionsFromPackage(),
                        CCommon.actionsFromAgentClass( IBaseBenchmarkAgent.class )
                    ),
                    Stream.of(
                        new CBroadcastAction( p_agents ),
                        new CSendAction( p_agents )
                    )
                ).collect( Collectors.toSet() )
            )
        );
    }

    /**
     * instantiate agent generator
     *
     * @param p_asl asl file
     * @param p_action action definition
     * @param p_variablebuilder variable builder
     * @param p_neighborhood neighborhood
     * @return generator
     */
    private IAgentGenerator<IBenchmarkAgent> generator( @Nonnull final String p_asl, @Nonnull final Set<IAction> p_action,
                                                        @Nonnull final IVariableBuilder p_variablebuilder, @Nonnull final INeighborhood p_neighborhood )
    {
        Logger.info( "reading asl file [{0}]", p_asl );
        try
        (
            final InputStream l_stream = new FileInputStream( p_asl );
        )
        {
            return m_statistic.starttimer( "parsing" ).stop( new CBenchmarkAgent.CGenerator(
                l_stream,
                p_action,
                p_variablebuilder,
                p_asl.toLowerCase( Locale.ROOT ).replace( ".asl", "" ),
                p_neighborhood
            ) );
        }
        catch ( final Exception l_exception )
        {
            Logger.error( "error on reading asl file [{0}]", l_exception.getMessage() );
            throw new RuntimeException( l_exception );
        }
    }

    /**
     * run a single iteration
     *
     * @param p_run run number
     */
    private void iteration( @Nonnegative int p_run )
    {
        m_runtime.accept(
            m_statistic.starttimer(  MessageFormat.format( "{0}-agentinitialize", String.format( m_numberpadding, p_run ) ) ).stop(
                m_agentdefinition.entrySet()
                                 .parallelStream()
                                 .flatMap( i -> i.getKey().generatemultiple( i.getValue().apply( p_run ).intValue() ) )
                                 .collect( Collectors.toSet() )
            ),
            new ImmutablePair<>( MessageFormat.format( "{0}-execution", String.format( m_numberpadding, p_run ) ), m_statistic )
        );
    }

    /**
     * warm-up run
     *
     * @param p_run run number
     */
    private void warmup( @Nonnegative int p_run )
    {
        m_runtime.accept(
            m_agentdefinition.entrySet()
                             .parallelStream()
                             .flatMap( i -> i.getKey().generatemultiple( i.getValue().apply( p_run ).intValue() ) )
                             .collect( Collectors.toSet() ),
            new ImmutablePair<>( MessageFormat.format( "{0}-execution", String.format( m_numberpadding, p_run ) ), IStatistic.EMPTY )
        );
    }


    /**
     * parse string formular
     *
     * @param p_formular input string
     * @return calculation function
     */
    private static Function<Number, Number> parse( @Nonnull final String p_formular )
    {
        Logger.info( "parsing agent number formular [{0}]", p_formular );
        try
        {
            return new CFormularParser().apply( IOUtils.toInputStream( p_formular, "UTF-8" ) );
        }
        catch ( final IOException l_exception )
        {
            Logger.error( "parsing error on formular [{0}]", p_formular );
            throw new UncheckedIOException( l_exception );
        }
    }

    /**
     * returns a new scneario instance
     *
     * @param p_file configuration file
     * @return scenario instance
     */
    public static IScenario build( @Nonnull final String p_file )
    {
        return new CScenario( p_file );
    }

}
