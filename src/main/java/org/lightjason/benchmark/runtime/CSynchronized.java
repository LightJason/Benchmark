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
import org.lightjason.benchmark.statistic.IStatistic;
import org.lightjason.benchmark.statistic.ITimer;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;


/**
 * synchronized step-based execution
 */
public final class CSynchronized extends IBaseRuntime
{

    /**
     * ctor
     *
     * @param p_type runtime type
     * @param p_value runtime value
     */
    public CSynchronized( @Nonnull final ERuntime p_type, final int p_value )
    {
        super(
            p_type,
            p_value
        );
    }

    @Override
    public final void accept( @Nonnull final Collection<IBenchmarkAgent> p_agents, @Nonnull final Pair<String, IStatistic> p_statistic )
    {
        final ITimer l_timer = p_statistic.getValue().starttimer( p_statistic.getLeft() );
        final AtomicReference<Exception> l_exception = new AtomicReference<>();

        // set bracking with the if confition, because within the while loop the terminate will be
        // optimized by the compilere, so it will not be executed anymore (voilatile)
        while ( l_exception.get() == null )
        {
            p_agents.parallelStream().forEach( i -> this.execute( i, l_exception::set ) );
            if ( p_agents.parallelStream().allMatch( IBenchmarkAgent::terminate ) )
                break;
        }

        l_timer.stop();

        if ( l_exception.get() != null )
            throw new RuntimeException( l_exception.get() );
    }

}
