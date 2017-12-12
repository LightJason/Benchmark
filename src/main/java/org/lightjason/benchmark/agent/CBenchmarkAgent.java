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
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.List;
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
     * @param p_index index of the agent
     */
    private CBenchmarkAgent( @Nonnull final IAgentConfiguration<IBenchmarkAgent> p_configuration,
                             @Nonnegative int p_index )
    {
        super( p_configuration, p_index );
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
         * @param p_defaultaction default actions
         * @param p_agents map with agents and names
         * @throws Exception on parsing error
         */
        public CGenerator( @Nonnull final InputStream p_stream, @Nonnull final Stream<IAction> p_defaultaction,
                           @Nonnull final IVariableBuilder p_variablebuilder,
                           @Nonnull final List<IBenchmarkAgent> p_agents ) throws Exception
        {
            super(
                    p_stream,
                    Stream.concat( p_defaultaction, CCommon.actionsFromAgentClass( CBenchmarkAgent.class ) ),
                    p_variablebuilder,
                    p_agents
            );
        }

        @Nullable
        @Override
        public final IBenchmarkAgent generatesingle( @Nullable final Object... p_data )
        {
            /*
            return this.initializeagent(
                    new CBenchmarkAgent(
                            m_configuration,
                            m_counter.getAndIncrement() )
                    )
            );
            */
            return null;
        }

    }
}
