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

import org.pmw.tinylog.Logger;

import javax.annotation.Nonnegative;
import java.util.concurrent.atomic.AtomicReference;


/**
 * alive logger
 */
public final class CLoggerAlive
{
    /**
     * instance
     */
    private static final AtomicReference<CLoggerAlive> INSTANCE = new AtomicReference<>();
    /**
     * running thread
     */
    private final Thread m_thread;

    /**
     * ctor
     *
     * @param p_alivetime alive time
     */
    private CLoggerAlive( @Nonnegative final long p_alivetime )
    {
        m_thread = new Thread( () ->
        {
            while ( true )
            {
                Logger.info( "benchmark is currently running" );
                try
                {
                    Thread.sleep( p_alivetime );
                }
                catch ( final InterruptedException l_exception )
                {
                    break;
                }
            }
        } );

        m_thread.start();
    }

    /**
     * buld logger
     *
     * @param p_alivetime alive thread
     */
    public static void build( @Nonnegative final long p_alivetime )
    {
        if ( p_alivetime > 0 )
            INSTANCE.compareAndSet( null, new CLoggerAlive( p_alivetime ) );
    }

    /**
     * interrupts logging
     */
    public static void interrupt()
    {
        INSTANCE.getAndUpdate( i ->
        {
            if ( i != null )
                i.m_thread.interrupt();

            return i;
        } );
    }
}
