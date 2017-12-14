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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.benchmark.agent.IBenchmarkAgent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * agent storage
 */
public class CLeftRightNeighbor implements INeighborhood
{
    /**
     * map with agent
     */
    private final Map<String, IBenchmarkAgent> m_agents = new ConcurrentHashMap<>();
    /**
     * neighbor structure
     */
    private final Map<String, Pair<String, String>> m_neighbor = new ConcurrentHashMap<>();

    @Nonnull
    @Override
    public final INeighborhood buildneighbor()
    {
        m_neighbor.clear();

        final List<String> l_neighbor = new ArrayList<>( m_agents.keySet() );

        IntStream.range( 0, l_neighbor.size() )
                 .parallel()
                 .forEach( i ->  m_neighbor.put(
                     l_neighbor.get( i ),
                     new ImmutablePair<>(
                         l_neighbor.get( i == 0 ? l_neighbor.size() - 1 : i - 1 ),
                         l_neighbor.get( ( i + 1 ) % l_neighbor.size() )
                     )
                 ) );

        return this;
    }

    @Nonnull
    @Override
    public Stream<String> neighbor( @Nullable final String... p_id )
    {
        if ( ( p_id == null ) || ( p_id.length != 2 ) )
            return Stream.empty();

        switch ( p_id[0].toLowerCase( Locale.ROOT ) )
        {
            case "left":
                final Pair<String, String> l_left = m_neighbor.get( p_id[1] );
                return l_left == null ? Stream.empty() : Stream.of( l_left.getLeft() );

            case "right":
                final Pair<String, String> l_right = m_neighbor.get( p_id[1] );
                return l_right == null ? Stream.empty() : Stream.of( l_right.getRight() );

            default:
                throw new RuntimeException( MessageFormat.format( "unknown parameter [{0}]", p_id[0] ) );
        }
    }

    @Override
    public final Stream<IBenchmarkAgent> stream()
    {
        return m_agents.values().stream();
    }

    @Override
    public final void accept( final IBenchmarkAgent p_agent )
    {
        m_agents.putIfAbsent( p_agent.id(), p_agent );
    }

    @Override
    public final IBenchmarkAgent apply( final String p_id )
    {
        return m_agents.get( p_id );
    }
}
