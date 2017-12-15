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

/**
 * timer
 */
public final class CTimer implements ITimer
{
    /**
     * name of the timer
     */
    private final String m_name;
    /**
     * statistic reference
     */
    private final IStatistic m_statistic;
    /**
     * start timer
     */
    private final long m_starttime = System.nanoTime();

    /**
     * ctor
     *
     * @param p_name name of the timer
     * @param p_statistic statistic reference
     */
    CTimer( final String p_name, final IStatistic p_statistic )
    {
        m_name = p_name;
        m_statistic = p_statistic;
    }

    @Override
    public final String name()
    {
        return m_name;
    }

    @Override
    public final <T> T stop( final T p_value )
    {
        m_statistic.accept( m_name, System.nanoTime() - m_starttime );
        return p_value;
    }

    @Override
    public final void stop()
    {
        this.stop( null );
    }

}
