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

package org.lightjason.benchmark.common;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * configuration
 */
public final class CConfiguration extends ITree.CTree
{
    /**
     * singleton instance
     */
    public static final CConfiguration INSTANCE = new CConfiguration();


    /**
     * ctor
     */
    private CConfiguration()
    {
        super( new ConcurrentHashMap<>() );
    }

    /**
     * set the internal data defintion
     * @param p_data map with dara
     * @throws Exception is thrown on action error
     */
    private void setdata( final Map<String, ?> p_data ) throws Exception
    {
        m_data.clear();
        m_data.putAll( p_data );
    }

    /**
     * loads the configuration from a string
     *
     * @param p_yaml yaml string
     * @return self reference
     */
    @SuppressWarnings( "unchecked" )
    public final CConfiguration loadstring( final String p_yaml )
    {
        try
        {
            final Map<String, ?> l_result = (Map<String, Object>) new Yaml().load( p_yaml );
            if ( l_result != null )
                this.setdata( l_result );
        }
        catch ( final Exception l_exception )
        {
            throw new RuntimeException( l_exception );
        }

        return this;
    }

    /**
     * loads the configuration
     * @param p_path path elements
     * @return self reference
     */
    @SuppressWarnings( "unchecked" )
    public final CConfiguration loadfile( final String p_path )
    {
        try
            (
                final InputStream l_stream = new FileInputStream( p_path )
            )
        {

            final Map<String, ?> l_result = (Map<String, Object>) new Yaml().load( l_stream );
            if ( l_result != null )
                this.setdata( l_result );

        }
        catch ( final Exception l_exception )
        {
            throw new RuntimeException( l_exception );
        }

        return this;
    }
}
