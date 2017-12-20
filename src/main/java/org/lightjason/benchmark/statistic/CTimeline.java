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

package org.lightjason.benchmark.statistic;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


/**
 * timeline statistic
 */
public class CTimeline implements ITimeline
{
    /**
     * statistic values
     */
    private final Multimap<String, Double> m_statistic = Multimaps.synchronizedMultimap( ArrayListMultimap.create() );

    @Override
    public final void accept( final String p_name, final Number p_number )
    {
        m_statistic.put( p_name, p_number.doubleValue() );
    }

    @Override
    public final Map<String, Collection<Double>> get()
    {
        return Collections.unmodifiableMap( m_statistic.asMap() );
    }
}
