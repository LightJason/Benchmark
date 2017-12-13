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
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.IVariable;
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
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * scenario
 */
public final class CScenario implements IScenario
{
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
     * agent constant values
     */
    private final Set<IVariable<?>> m_agentconstants;
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

        m_agentconstants = Collections.unmodifiableSet(
            p_configuration.<Map<String, Object>>getOrDefault( Collections.emptyMap(), "agent", "constant" )
            .entrySet()
            .parallelStream()
            .map( i -> new CConstant<>( i.getKey(), i.getValue() ) )
            .collect( Collectors.toSet() )
        );

        m_agentdefinition = Collections.unmodifiableMap(
            p_configuration.<Map<String, Object>>getOrDefault( Collections.emptyMap(), "agent", "source" )
                    .entrySet()
                    .parallelStream()
                    .collect( Collectors.toMap( Map.Entry::getKey, i -> parse( i.getValue().toString() ) ) )
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
        m_currentrun++;
        return this;
    }



    @Override
    public void run()
    {

    }

    @Override
    public final SummaryStatistics get()
    {
        return null;
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
