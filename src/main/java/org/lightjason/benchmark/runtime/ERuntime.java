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

package org.lightjason.benchmark.runtime;

import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.benchmark.agent.IBenchmarkAgent;
import org.lightjason.benchmark.scenario.IStatistic;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Locale;


/**
 * runtime
 */
public enum ERuntime implements IRuntime
{
    SYNCHRONIZED( new CSynchronize() ),
    ASYNCHRONIZED( new CAsychronize() );

    /**
     * runtime instance
     */
    private final IRuntime m_runtime;

    /**
     * @param p_runtime runtime
     */
    ERuntime( final IRuntime p_runtime )
    {
        m_runtime = p_runtime;
    }

    @Override
    public final void accept( @Nonnull final Collection<IBenchmarkAgent> p_agents,
                              @Nonnull final Pair<String, IStatistic> p_statistic )
    {
        m_runtime.accept( p_agents, p_statistic );
    }

    /**
     * returns runtime instance of string value
     *
     * @param p_name name
     * @return runtime instance
     */
    public static IRuntime from( @Nonnull final String p_name )
    {
        return ERuntime.valueOf( p_name.toUpperCase( Locale.ROOT ) );
    }

    @Override
    public final String toString()
    {
        return super.toString().toLowerCase( Locale.ROOT );
    }
}
