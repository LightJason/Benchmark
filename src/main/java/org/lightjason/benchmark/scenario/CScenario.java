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

package org.lightjason.benchmark.scenario;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.benchmark.common.CConfiguration;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * scenario
 */
public final class CScenario implements IScenario
{
    /**
     * warum-up simulation steps
     */
    private final int m_warmup;
    /**
     * agent constant values
     */
    private final Set<IVariable<?>> m_agentconstants;

    /**
     * instantiate scneario
     */
    private CScenario()
    {
        m_warmup = CConfiguration.INSTANCE.<Number>getOrDefault( 0, "runtime", "warmup" ).intValue();
        m_agentconstants = Collections.unmodifiableSet(
            CConfiguration.INSTANCE.<Map<String, Object>>getOrDefault( Collections.emptyMap(), "agent", "constant" )
            .entrySet()
            .parallelStream()
            .map( i -> new CConstant<>( i.getKey(), i.getValue() ) )
            .collect( Collectors.toSet() )
        );
    }

    @Override
    public void run()
    {

    }

    @Override
    public SummaryStatistics get()
    {
        return null;
    }

    /**
     * returns a new scneario instance
     *
     * @return scenario instance
     */
    public static IScenario build()
    {
        return new CScenario();
    }

}
