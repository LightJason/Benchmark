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

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Locale;


/**
 * statistic factory
 */
public enum EStatistic
{
    SUMMARY,
    DESCRIPTIVE;

    /**
     * build a new statistic reference
     *
     * @return statistic
     */
    public IStatistic build()
    {
        switch ( this )
        {
            case SUMMARY:
                return new CSummaryStatistic();

            case DESCRIPTIVE:
                return new CDescriptiveStatistic();

            default:
                throw new RuntimeException( MessageFormat.format( "unknown statistic [{0}]", this ) );
        }
    }

    /**
     * factory
     *
     * @param p_name name
     * @return statistic
     */
    public static EStatistic from( @Nonnull final String p_name )
    {
        return EStatistic.valueOf( p_name.toUpperCase( Locale.ROOT ) );
    }
}
