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

package org.lightjason.benchmark.agent;

import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * abstract class to define an
 * agent with environment
 */

public abstract class IBaseBenchmarkAgent extends IBaseAgent<IBenchmarkAgent> implements IBenchmarkAgent
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 639087239899834442L;
    /**
     * index of the agent
     */
    private final int m_index;

    /**
     * ctor
     *
     * @param p_configuration agent configuration
     * @param p_index index of the agent
     */
    protected IBaseBenchmarkAgent( @Nonnull final IAgentConfiguration<IBenchmarkAgent> p_configuration, @Nonnegative final int p_index )
    {
        super( p_configuration );
        m_index = p_index;
    }

    @Override
    @Nonnegative
    public final int index()
    {
        return m_index;
    }

    @Override
    public final int hashCode()
    {
        return m_index;
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IBaseBenchmarkAgent ) && ( p_object.hashCode() == this.hashCode() );
    }

    @Override
    public final boolean active()
    {
        return ( this.sleeping() ) || ( !this.runningplans().isEmpty() );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * abstract agent generatorclass for all agents
     */
    protected abstract static class IGenerator extends IBaseAgentGenerator<IBenchmarkAgent>
    {
        /**
         * agent number counter
         */
        protected final AtomicInteger m_counter = new AtomicInteger();
        /**
         * agent list
         */
        private final List<IBenchmarkAgent> m_agents;


        /**
         * constructor
         *
         * @param p_stream ASL input stream
         * @param p_actions action stream
         * @param p_variablebuilder variable builder
         * @param p_agents agent list
         * @throws Exception on any error
         */
        protected IGenerator( @Nonnull final InputStream p_stream, @Nonnull final Stream<IAction> p_actions,
                              @Nonnull final IVariableBuilder p_variablebuilder,
                              @Nonnull final List<IBenchmarkAgent> p_agents ) throws Exception
        {
            super( p_stream,
                   Stream.concat(
                       CCommon.actionsFromAgentClass( IBaseBenchmarkAgent.class ),
                       p_actions
                   ).collect( Collectors.toSet() ),
                   p_variablebuilder
            );

            m_agents = p_agents;
        }


        /**
         * initialize the agent for the simulation
         *
         * @param p_agent agent object
         * @return agent object
         */
        @Nonnull
        protected final IBenchmarkAgent initializeagent( @Nonnull final IBenchmarkAgent p_agent )
        {
            m_agents.add( p_agent );
            return p_agent;
        }

    }
}
