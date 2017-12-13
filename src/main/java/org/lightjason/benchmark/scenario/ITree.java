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


import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * tree structure
 */
public interface ITree
{
    /**
     * returns a configuration value
     *
     * @param p_path path of the element
     * @tparam T returning type
     * @return value or null
     */
    <T> T get( final String... p_path );


    /**
     * returns a configuration value or on not
     * existing the default value
     *
     * @param p_default default value
     * @param p_path path of the element
     * @tparam T returning type
     * @return value / default vaue
     */
    @Nonnull
    <T> T getOrDefault( @Nonnull final T p_default, @Nonnull final String... p_path );

    /**
     * add a value to a path
     * @param p_value value
     * @param p_path path
     * @tparam T returning type
     * @return data object
     */
    <T> Map<String, Object> insert( @Nonnull final T p_value, @Nonnull final String... p_path );

    /**
     * base tree implementation
     */
    class CTree implements ITree
    {
        /**
         * data map
         */
        protected final Map<String, Object> m_data;

        /**
         * ctor
         *
         * @param p_data map data
         */
        public CTree( @Nonnull final Map<String, Object> p_data )
        {
            m_data = p_data;
        }

        @Override
        @SuppressWarnings( "unchecked" )
        public final <T> T get( final String... p_path )
        {
            if ( ( p_path == null ) || ( p_path.length == 0 ) )
                return (T) m_data;
            return recursivedescent( m_data, p_path );
        }


        @Override
        @Nonnull
        public final <T> T getOrDefault( @Nonnull final T p_default, @Nonnull final String... p_path )
        {
            final T l_result = recursivedescent( m_data, p_path );
            return l_result == null
                   ? p_default
                   : l_result;
        }

        /**
         * recursive descent
         *
         * @param p_map configuration map
         * @param p_path path
         * @tparam T returning type parameter
         * @return value
         */
        @SuppressWarnings( "unchecked" )
        private static <T> T recursivedescent( final Map<String, ?> p_map, final String... p_path )
        {
            final Object l_data = p_map.get( p_path[0].toLowerCase( Locale.ROOT ) );
            return ( p_path.length == 1 ) || ( l_data == null )
                   ? (T) l_data
                   : (T) recursivedescent( (Map<String, ?>) l_data, Arrays.copyOfRange( p_path, 1, p_path.length ) );
        }

        @Override
        public final String toString()
        {
            return m_data.toString();
        }


        @Override
        public <T> Map<String, Object> insert( @Nonnull final T p_value, @Nonnull final String... p_path )
        {
            if ( p_path.length == 0 )
                throw new RuntimeException( "path need not to be empty" );
            return insertrecursivedescent( m_data, p_value, p_path );
        }

        /**
         * insert recursive
         * @param p_map map
         * @param p_value value
         * @param p_path path
         * @param <T> T
         * @return map
         */
        @SuppressWarnings( "unchecked" )
        private static <T> Map<String, Object> insertrecursivedescent( final Map<String, Object> p_map, final T p_value, final String... p_path )
        {
            if ( p_path.length == 1 )
            {
                p_map.put( p_path[0], p_value );
                return p_map;
            }
            else
            {
                if ( !p_map.containsKey( p_path[0] ) )
                    p_map.put( p_path[0], new HashMap<>() );
                return insertrecursivedescent( (Map<String, Object>) p_map.get( p_path[0] ), p_value, Arrays.copyOfRange( p_path, 1, p_path.length ) );
            }
        }
    }
}

