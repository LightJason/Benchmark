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

import org.lightjason.agentspeak.agent.IAgent;
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
import org.lightjason.benchmark.agent.IBaseBenchmarkAgent;
import org.lightjason.benchmark.agent.IBenchmarkAgent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;


/**
 * external send action for sending messages
 * to a specified agent based on the name
 */
public final class CSendAction extends ICommunication
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -444388639290879293L;
    /**
     * action name
     */
    private static final IPath NAME = CPath.from( "message/send" );

    /**
     * ctor
     *
     * @param p_agents agents
     */
    protected CSendAction( final List<IBenchmarkAgent> p_agents )
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
        if ( l_arguments.size() < 2 )
            return CFuzzyValue.from( false );

        final IAgent<?> l_receiver = m_agents.get( l_arguments.get( 0 ).<Number>raw().intValue() % m_agents.size() );
        final ITerm l_sender = CLiteral.from( FROMFUNCTOR, CRawTerm.from( p_context.agent().<IBaseBenchmarkAgent>raw().index() ) );
        l_arguments.stream()
                   .skip( 1 )
                   .map( ITerm::raw )
                   .map( CRawTerm::from )
                   .map( i -> CTrigger.from(
                                RECEIVETRIGGER,
                                CLiteral.from( RECEIVEFUNCTOR, CLiteral.from( MESSAGEFUNCTOR, i ), l_sender )
                   ) )
                   .forEach( i -> l_receiver.trigger( i ) );

        return CFuzzyValue.from( true );
    }

}
