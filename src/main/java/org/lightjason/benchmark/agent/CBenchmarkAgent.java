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
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.benchmark.neighborhood.INeighborhood;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Stream;

/**
 * agent class to represent
 * a type of agent
 */

public final class CBenchmarkAgent extends IBaseBenchmarkAgent
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 1L;


    /**
     * constructor
     *
     * @param p_configuration agent configuration
     * @param p_name name of the agent
     */
    private CBenchmarkAgent( @Nonnull final IAgentConfiguration<IBenchmarkAgent> p_configuration,
                             @Nonnull final String p_name, @Nonnull final INeighborhood p_neighborhood )
    {
        super( p_configuration, p_name, p_neighborhood );
    }



    /**
     * generator of a specified type of agents
     */
    public static final class CGenerator extends IGenerator
    {

        /**
         * constructor
         *
         * @param p_stream ASL input stream
         * @param p_actions actions
         * @param p_neighborhood neighborhood
         * @throws Exception on parsing error
         */
        public CGenerator( @Nonnull final InputStream p_stream, @Nonnull final Set<IAction> p_actions,
                           @Nonnull final IVariableBuilder p_variablebuilder,
                           @Nonnull final String p_basename,
                           @Nonnull final INeighborhood p_neighborhood ) throws Exception
        {
            super(
                    p_stream,
                    p_actions,
                    p_variablebuilder,
                    p_basename,
                    p_neighborhood
            );
        }

        @Nullable
        @Override
        public final IBenchmarkAgent generatesingle( @Nullable final Object... p_data )
        {
            return this.initializeagent(
                    new CBenchmarkAgent(
                            m_configuration,
                            this.name(),
                            m_neighborhood
                    )
            );
        }

    }

    /**
     * variable builder
     */
    public static final class CVariableBuilder implements IVariableBuilder
    {
        /**
         * variables
         */
        private final Set<IVariable<?>> m_variables;

        /**
         * ctor
         *
         * @param p_variables variables
         */
        public CVariableBuilder( final Set<IVariable<?>> p_variables )
        {
            m_variables = p_variables;
        }

        @Override
        public final Stream<IVariable<?>> apply( final IAgent<?> p_agent, final IInstantiable p_instantiate )
        {
            return m_variables.stream();
        }
    }
}
