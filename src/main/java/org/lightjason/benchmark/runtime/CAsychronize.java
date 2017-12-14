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

import org.lightjason.benchmark.agent.IBenchmarkAgent;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * asychronized execution
 */
public final class CAsychronize extends IBaseRuntime
{
    /**
     * stealing pool
     */
    private final ExecutorService m_pool = Executors.newWorkStealingPool();

    @Override
    public final void accept( final Collection<IBenchmarkAgent> p_agents )
    {
        p_agents.parallelStream().forEach( CExecution::new );
    }

    /**
     * execution agent wrapper
     */
    private final class CExecution implements Runnable
    {
        private final IBenchmarkAgent m_agent;

        /**
         * execution
         *
         * @param p_agent agent
         */
        CExecution( final IBenchmarkAgent p_agent )
        {
            m_agent = p_agent;
            m_pool.submit( this );
        }

        @Override
        public final void run()
        {
            execute( m_agent );

            if ( m_agent.active() )
                m_pool.submit( this );
        }
    }
}
