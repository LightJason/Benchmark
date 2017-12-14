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

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.benchmark.neighborhood.INeighborhood;


/**
 * communication class
 */
public abstract class ICommunication extends IBaseAction
{
    /**
     * receive literal functor
     */
    protected static final String RECEIVEFUNCTOR = "message/receive";
    /**
     * message literal functor
     */
    protected static final String MESSAGEFUNCTOR = "message";
    /**
     * from literal functor
     */
    protected static final String FROMFUNCTOR = "from";
    /**
     * receive trigger
     */
    protected static final ITrigger.EType RECEIVETRIGGER = CTrigger.EType.ADDGOAL;
    /**
     * serial id
     */
    private static final long serialVersionUID = 1104268283747211370L;
    /**
     * agent storage
     */
    protected final INeighborhood m_agents;

    /**
     * ctor
     *
     * @param p_agents agents
     */
    protected ICommunication( final INeighborhood p_agents )
    {
        m_agents = p_agents;
    }
}
