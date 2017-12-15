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

import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SynchronizedSummaryStatistics;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;


/**
 * summary statistic
 */
public class CSummaryStatistic implements IStatistic
{
    /**
     * statistic map
     */
    private final Map<String, SummaryStatistics> m_statistic = new ConcurrentSkipListMap<>( String.CASE_INSENSITIVE_ORDER );

    @Override
    public final IStatistic clear( @Nonnull final String p_name )
    {
        final SummaryStatistics l_statistic = m_statistic.get( p_name );
        if ( l_statistic != null )
            l_statistic.clear();

        return this;
    }

    @Override
    public final ITimer starttimer( final String p_name )
    {
        return new CTimer( p_name, this );
    }

    @Override
    public final void accept( final String p_name, final Number p_number )
    {
        final SummaryStatistics l_statistic = m_statistic.getOrDefault( p_name, new SynchronizedSummaryStatistics() );
        m_statistic.putIfAbsent( p_name, l_statistic );
        l_statistic.addValue( p_number.doubleValue() );
    }

    @Override
    public final Map<String, StatisticalSummary> get()
    {
        return Collections.unmodifiableMap( m_statistic );
    }
}
