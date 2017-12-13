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

import org.apache.commons.io.IOUtils;
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
import org.lightjason.benchmark.runtime.ERuntime;
import org.lightjason.benchmark.runtime.IRuntime;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
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
     * warum-up simulation steps
     */
    private final int m_warmup;
    /**
     * map with asl pathes and generatoring functions
     */
    private final Map<String, Function<Number, Number>> m_agentdefinition;
    /**
     * current run
     */
    private int m_currentrun;

    /**
     * instantiate scneario
     */
    private CScenario( @Nonnull final ITree p_configuration )
    {
        m_runtime = ERuntime.from( p_configuration.getOrDefault( "", "runtime", "type" ) );
        m_runs = p_configuration.<Number>getOrDefault( 0, "agent", "runs" ).intValue();
        m_warmup = p_configuration.<Number>getOrDefault( 0, "agent", "warmup" ).intValue();


        // action instantiation




        // create variable builder
        final IVariableBuilder l_variablebuilder = new CBenchmarkAgent.CVariableBuilder(
            Collections.unmodifiableSet(
                p_configuration.<Map<String, Object>>getOrDefault( Collections.emptyMap(), "agent", "constant" )
                               .entrySet()
                               .parallelStream()
                               .map( i -> new CConstant<>( i.getKey(), i.getValue() ) )
                               .collect( Collectors.toSet() )
            )
        );




        // agent generators
        m_agentdefinition = Collections.unmodifiableMap(
            p_configuration.<Map<String, Object>>getOrDefault( Collections.emptyMap(), "agent", "source" )
                    .entrySet()
                    .parallelStream()
                    .collect( Collectors.toMap( Map.Entry::getKey, i -> parse( i.getValue().toString() ) ) )
        );


    }

    /**
     * instantiate actions
     *
     * @param p_agents agents
     * @return actions
     */
    private Set<IAction> action( final List<IBenchmarkAgent> p_agents )
    {
        return m_statistic.star( "action" ).stop(
            Collections.unmodifiableSet(
                Stream.concat(
                    Stream.concat(
                        CCommon.actionsFromPackage(),
                        CCommon.actionsFromAgentClass( IBaseBenchmarkAgent.class )
                    ),
                    Stream.of(
                        new CBroadcastAction( null ),
                        new CSendAction( null )
                    )
                ).collect( Collectors.toSet() )
            )
        );
    }


    private IAgentGenerator<IBenchmarkAgent> generator( @Nonnull final String p_asl, @Nonnull final Set<IAction> p_action,
                                                        @Nonnull final IVariableBuilder p_variablebuilder, @Nonnull final List<IBenchmarkAgent> p_agents )
    {
        try
        (
            final InputStream l_stream = new FileInputStream( p_asl );
        )
        {
            return m_statistic.star( "parser" ).stop( new CBenchmarkAgent.CGenerator( l_stream, p_action, p_variablebuilder, p_agents ) );
        }
        catch ( final Exception l_exception )
        {
            throw new RuntimeException( l_exception );
        }
    }

    /**
     * parse string formular
     *
     * @param p_formular input string
     * @return calculation function
     */
    private static Function<Number, Number> parse( @Nonnull final String p_formular )
    {
        try
        {
            return new CFormularParser().apply( IOUtils.toInputStream( p_formular, "UTF-8" ) );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }


    @Override
    public boolean hasNext()
    {
        return m_currentrun < m_runs;
    }

    @Override
    public final IScenario next()
    {
        // running execution

        m_currentrun++;
        return this;
    }

    /**
     * returns a new scneario instance
     *
     * @param p_file configuration file
     * @return scenario instance
     */
    public static IScenario build( @Nonnull final String p_file )
    {
        try
        (
            final InputStream l_stream = new FileInputStream( p_file )
        )
        {

            return new CScenario( new ITree.CTree( new Yaml().load( l_stream ) ) );

        }
        catch ( final Exception l_exception )
        {
            throw new RuntimeException( l_exception );
        }
    }

}
