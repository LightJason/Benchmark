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

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;


/**
 * statistic interface
 */
public interface IStatistic extends BiConsumer<String, Number>, Supplier<Map<String, DescriptiveStatistics>>
{
    /**
     * starts a timer
     *
     * @param p_name name of the timer
     * @return timer
     */
    ITimer star( final String p_name );

    /**
     * interface of a timer object
     */
    interface ITimer
    {
        /**
         * name of the timer
         */
        String name();

        /**
         * stop timer
         *
         * @param p_value any value
         * @return returns value
         */
        <T> T stop( final T p_value );
    }
}
