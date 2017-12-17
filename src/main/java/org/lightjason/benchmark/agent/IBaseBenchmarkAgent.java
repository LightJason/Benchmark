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
import org.lightjason.agentspeak.action.binding.IAgentAction;
import org.lightjason.agentspeak.action.binding.IAgentActionFilter;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.benchmark.neighborhood.INeighborhood;
import org.lightjason.benchmark.statistic.IStatistic;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * abstract class to define an
 * agent with environment
 */
@IAgentAction
public abstract class IBaseBenchmarkAgent extends IBaseAgent<IBenchmarkAgent> implements IBenchmarkAgent
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 639087239899834442L;
    /**
     * index of the agent
     */
    private final String m_identifier;
    /**
     * agent p_neighborhood
     */
    private final INeighborhood m_neighborhood;
    /**
     * statistic element
     */
    private final IStatistic m_statistic;
    /**
     * base name for statistic
     */
    private final String m_basename;

    /**
     * ctor
     *
     * @param p_configuration agent configuration
     * @param p_id id of the agent
     */
    protected IBaseBenchmarkAgent( @Nonnull final IAgentConfiguration<IBenchmarkAgent> p_configuration, @Nonnull final String p_id,
                                   @Nonnull final INeighborhood p_neighborhood, @Nonnull final String p_basename,
                                   @Nonnull final IStatistic p_statistic
    )
    {
        super( p_configuration );
        m_identifier = p_id;
        m_basename = p_basename;
        m_statistic = p_statistic;
        m_neighborhood = p_neighborhood;
    }

    @Nonnull
    @Override
    @Nonnegative
    public final String id()
    {
        return m_identifier;
    }

    @Override
    public final IBenchmarkAgent call() throws Exception
    {
        return m_statistic.starttimer( "cycle-" + m_basename ).stop( super.call() );
    }

    @Override
    public final int hashCode()
    {
        return m_identifier.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IBaseBenchmarkAgent ) && ( p_object.hashCode() == this.hashCode() );
    }

    @Override
    public final boolean terminate()
    {
        return ( !this.sleeping() ) && ( this.runningplans().isEmpty() );
    }

    /**
     * agent action to get neighbours
     *
     * @param p_neighbor neighbor name
     * @return name list
     */
    @IAgentActionFilter
    private List<String> neighbor( final String p_neighbor )
    {
        return m_neighborhood.neighbor( p_neighbor, m_identifier ).collect( Collectors.toList() );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * abstract agent generatorclass for all agents
     */
    protected abstract static class IBaseGenerator extends IBaseAgentGenerator<IBenchmarkAgent> implements IBenchmarkAgentGenerator
    {
        /**
         * agent list
         */
        protected final INeighborhood m_neighborhood;
        /**
         * name of tha agents
         */
        protected final String m_basename;
        /**
         * agent number counter
         */
        private final AtomicInteger m_counter = new AtomicInteger();

        /**
         * constructor
         *
         * @param p_stream ASL input stream
         * @param p_actions action stream
         * @param p_variablebuilder variable builder
         * @param p_basename base name
         * @param p_neighborhood neighborhhod
         * @throws Exception on any error
         */
        protected IBaseGenerator( @Nonnull final InputStream p_stream, @Nonnull final Set<IAction> p_actions,
                                  @Nonnull final IVariableBuilder p_variablebuilder,
                                  @Nonnull final String p_basename,
                                  @Nonnull final INeighborhood p_neighborhood ) throws Exception
        {
            super( p_stream,
                   p_actions,
                   p_variablebuilder
            );

            m_basename = p_basename;
            m_neighborhood = p_neighborhood;
        }

        /**
         * returns a new agent name
         *
         * @return name
         */
        protected String name()
        {
            return m_basename + "-" + m_counter.getAndIncrement();
        }

        @Override
        public final int hashCode()
        {
            return m_basename.hashCode();
        }

        @Override
        public final IBenchmarkAgentGenerator reset()
        {
            m_counter.set( 0 );
            return this;
        }

        @Override
        public final boolean equals( final Object p_object )
        {
            return ( p_object != null ) && ( p_object instanceof IBaseGenerator ) && ( p_object.hashCode() == this.hashCode() );
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
            m_neighborhood.accept( p_agent );
            return p_agent;
        }

    }
}
