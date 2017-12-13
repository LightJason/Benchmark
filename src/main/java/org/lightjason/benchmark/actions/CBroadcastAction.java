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

package org.lightjason.benchmark.actions;

import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.benchmark.agent.IBenchmarkAgent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;


/**
 * external broadcast action for sending
 * messages to a set of agents based on
 * a regular expression
 */
public final class CBroadcastAction extends ICommunication
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 923344428639087998L;
    /**
    * action name
     */
    private static final IPath NAME = CPath.from( "message/broadcast" );

    /**
     * ctor
     *
     * @param p_agents agents
     */
    public CBroadcastAction( final List<IBenchmarkAgent> p_agents )
    {
        super( p_agents );
    }

    @Nonnull
    @Override
    public final IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        final ITerm l_sender = CLiteral.from( FROMFUNCTOR, CRawTerm.from( p_context.agent().<IBenchmarkAgent>raw().index() ) );
        final List<ITrigger> l_trigger = l_arguments.parallelStream()
                                                    .map( ITerm::raw )
                                                    .map( CRawTerm::from )
                                                    .map( i -> CTrigger.from(
                                                        RECEIVETRIGGER,
                                                        CLiteral.from( RECEIVEFUNCTOR, CLiteral.from( MESSAGEFUNCTOR, i ), l_sender )
                                                    ) )
                                                    .collect( Collectors.toList() );

        m_agents.parallelStream()
                .forEach( i -> l_trigger.forEach( j -> i.trigger( j ) ) );

        return CFuzzyValue.from( true );
    }
}
