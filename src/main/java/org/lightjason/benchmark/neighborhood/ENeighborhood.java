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

package org.lightjason.benchmark.neighborhood;

import javax.annotation.Nonnull;
import java.util.Locale;


/**
 * storage list
 */
public enum ENeighborhood
{
    LEFTRIGHT;

    /**
     * create an agent storage
     *
     * @return agent storage
     */
    public final INeighborhood build()
    {
        switch ( this )
        {
            case LEFTRIGHT:
                return new CLeftRightNeighbor();

            default:
                throw new RuntimeException( "unknown storage" );
        }
    }

    /**
     * returns storage type
     *
     * @param p_name storage name
     * @return storage type
     */
    public static ENeighborhood from( @Nonnull final String p_name )
    {
        return ENeighborhood.valueOf( p_name.toUpperCase( Locale.ROOT ) );
    }

}
