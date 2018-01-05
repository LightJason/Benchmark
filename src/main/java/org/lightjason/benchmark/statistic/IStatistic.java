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

import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;


/**
 * statistic interface
 */
public interface IStatistic extends BiConsumer<String, Number>, Supplier<Map<String, StatisticalSummary>>
{
    /**
     * empty statistic
     */
    IStatistic EMPTY = new IStatistic()
    {
        @Override
        public final void clear()
        {
        }

        @Override
        public final ITimer starttimer( final String p_name )
        {
            return ITimer.EMPTY;
        }

        @Override
        public final void accept( final String p_name, final Number p_number )
        {

        }

        @Override
        public final Map<String, StatisticalSummary> get()
        {
            return Collections.emptyMap();
        }
    };

    /**
     * clears all data
     */
    void clear();

    /**
     * starts a timer
     *
     * @param p_name name of the timer
     * @return timer
     */
    ITimer starttimer( final String p_name );

}
