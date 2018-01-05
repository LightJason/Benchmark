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

import org.lightjason.benchmark.statistic.CTimeline;
import org.lightjason.benchmark.statistic.ITimeline;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


/**
 * memory logger
 */
public final class CLoggerMemory
{
    /**
     * instance
     */
    private static final AtomicReference<CLoggerMemory> INSTANCE = new AtomicReference<>();
    /**
     * running thread
     */
    private final Thread m_thread;
    /**
     * pause logger flag
     */
    private final AtomicBoolean m_pause = new AtomicBoolean( true );
    /**
     * memory
     */
    private final ITimeline m_memorystatistic = new CTimeline();

    /**
     * ctor
     *
     * @param p_loggingrate logging sleep rate
     */
    private CLoggerMemory( @Nonnegative final long p_loggingrate )
    {
        m_thread = new Thread( () ->
        {
            while ( true )
            {
                if ( !m_pause.get() )
                {
                    m_memorystatistic.accept( "totalmemory", Runtime.getRuntime().totalMemory() );
                    m_memorystatistic.accept( "freememory", Runtime.getRuntime().freeMemory() );
                    m_memorystatistic.accept( "usedmemory", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() );
                }

                try
                {
                    Thread.sleep( p_loggingrate );
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
     * @param p_lograte logging rate
     */
    public static void build( @Nonnegative final long p_lograte )
    {
        if ( p_lograte > 0 )
            INSTANCE.compareAndSet( null, new CLoggerMemory( p_lograte ) );
    }


    /**
     * clears the memory statistic
     */
    public static void clear()
    {
        INSTANCE.getAndUpdate( i ->
        {
            if ( i != null )
                i.m_memorystatistic.clear();

            return i;
        } );
    }

    /**
     * store memory data
     *
     * @param p_data output map
     * @return output map
     */
    @SuppressWarnings( "unchecked" )
    public static Map<String, Object> store( @Nonnull final Map<String, Object> p_data )
    {
        final CLoggerMemory l_instance = INSTANCE.get();
        if ( l_instance == null )
            return p_data;

        final Map<String, Collection<Double>> l_memory = (Map<String, Collection<Double>>) p_data.getOrDefault( "memory", new HashMap<>() );
        p_data.put( "memory", l_memory );

        l_instance.m_memorystatistic.get()
                                    .forEach( ( p_key, p_value ) ->
                                    {
                                        final Collection<Double> l_data = l_memory.getOrDefault( p_key, new ArrayList<>() );
                                        l_memory.put( p_key, l_data );
                                        l_data.addAll( p_value );
                                    } );

        Runtime.getRuntime().gc();
        return p_data;
    }

    /**
     * change pause state
     */
    public static void swappause()
    {
        INSTANCE.getAndUpdate( i ->
        {
            if ( i != null )
                i.m_pause.set( !i.m_pause.get() );

            return i;
        } );
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
