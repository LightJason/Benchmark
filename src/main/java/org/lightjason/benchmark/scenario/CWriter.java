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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.lightjason.benchmark.agent.IBenchmarkAgentGenerator;
import org.lightjason.benchmark.statistic.CDescriptiveStatisticSerializer;
import org.lightjason.benchmark.statistic.CSummaryStatisticSerializer;
import org.lightjason.benchmark.statistic.IStatistic;
import org.pmw.tinylog.Logger;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * data writer
 */
public final class CWriter
{
    /**
     * ctor
     */
    private CWriter()
    {
    }


    /**
     * stores the statistic data,
     * on the first iteration all static data are
     * stores, after that the statistic data are only append
     * to the existing file, so memory consumption is reduced
     *
     * @param p_iteration iteration number (start with 1)
     * @throws IOException thrown on io errors
     */
    public static void store( @Nonnull final File p_file, @Nonnull final SerializationFeature p_serializefeature,
                              @Nonnegative final int p_iteration, @Nonnull final IStatistic p_statistic,
                              @Nonnull final Consumer<Map<String, Object>> p_static, @Nonnull final String p_numberpadding,
                              @Nonnegative final int p_runs,
                              @Nonnull final Map<IBenchmarkAgentGenerator, Function<Number, Number>> p_generator ) throws IOException
    {
        Runtime.getRuntime().gc();
        Logger.info( "store measurement result in [{}]", p_file );

        storewrite(

            storebynamemap(

                storebynamelist(

                    storebynamelist(

                        CLoggerMemory.store(
                            storestatic(
                                storeinitialize( p_file, p_iteration ),
                                p_runs,
                                p_static,
                                p_generator
                            )
                        ),

                        p_statistic,
                        "time",
                        "agentinitialize",
                        p_iteration,
                        p_numberpadding
                    ),

                    p_statistic,
                    "time",
                    "execution",
                    p_iteration,
                    p_numberpadding
                ),

                p_statistic,
                "time",
                "cycle"
            ),

            p_file,
            p_serializefeature
        );

        CLoggerMemory.clear();
        p_statistic.clear();
    }

    /**
     * adds the static configuration data to the output map
     *
     * @param p_data output map
     * @return output map
     */
    @Nonnull
    private static Map<String, Object> storestatic( @Nonnull final Map<String, Object> p_data, @Nonnegative final int p_runs,
                                                    @Nonnull final Consumer<Map<String, Object>> p_static,
                                                    @Nonnull final Map<IBenchmarkAgentGenerator, Function<Number, Number>> p_generator )
    {
        if ( !p_data.containsKey( "configuration" ) )
        {
            final Map<String, Object> l_configuration = new HashMap<>();
            p_static.accept( l_configuration );
            p_data.put( "configuration", l_configuration );
        }

        if ( !p_data.containsKey( "scenariosize" ) )
            p_data.put(
                    "scenariosize",
                    IntStream.rangeClosed( 1, p_runs )
                            .boxed()
                            .map( i -> p_generator.entrySet().stream().collect( Collectors.toMap( j -> j.getKey().basename(), j -> j.getValue().apply( i ) ) ) )
                            .collect( Collectors.toList() )
            );

        return p_data;
    }

    /**
     * append a statistic dataset to a child list
     *
     * @param p_data output map
     * @param p_statistic statistic data
     * @param p_parent parent map name
     * @param p_child child map name
     * @param p_iteration iteration number
     * @return output map
     */
    @SuppressWarnings( "unchecked" )
    private static Map<String, Object> storebynamelist( @Nonnull final Map<String, Object> p_data, @Nonnull final IStatistic p_statistic,
                                                        @Nonnull final String p_parent,  @Nonnull final String p_child,
                                                        @Nonnegative final int p_iteration, @Nonnull final String p_numberpadding )
    {
        final Map<String, Object> l_child = (Map<String, Object>) p_data.getOrDefault( p_parent, new HashMap<String, Object>() );
        p_data.putIfAbsent( p_parent, l_child );

        final List<Object> l_data = (List<Object>) l_child.getOrDefault( p_child, new ArrayList<>() );
        l_child.putIfAbsent( p_child, l_data );
        l_data.add( p_statistic.get().get( MessageFormat.format( "{0}-{1}", String.format( p_numberpadding, p_iteration ), p_child ) ) );

        Runtime.getRuntime().gc();
        return p_data;
    }

    /**
     * stores by name a map
     *
     * @param p_data output map
     * @param p_parent parent name
     * @param p_child  child name
     * @return output map
     */
    @SuppressWarnings( "unchecked" )
    private static Map<String, Object> storebynamemap( @Nonnull final Map<String, Object> p_data, @Nonnull final IStatistic p_statistic,
                                                       @Nonnull final String p_parent, @Nonnull final String p_child )
    {
        final Map<Integer, Map<String, Object>> l_data = new HashMap<>();
        p_statistic.get()
                   .entrySet()
                   .stream()
                   .filter( i -> i.getKey().startsWith( p_child ) )
                   .forEach( i ->
                   {
                       final String[] l_key = i.getKey().split( "-" );
                       final Map<String, Object> l_map = l_data.getOrDefault( Integer.parseInt( l_key[1] ), new HashMap<>() );

                       l_data.putIfAbsent( Integer.parseInt( l_key[1] ), l_map );
                       l_map.put( l_key[2], i.getValue() );
                   } );



        final Map<String, Object> l_parentdata = (Map<String, Object>) p_data.getOrDefault( p_parent, new HashMap<>() );
        p_data.put( p_parent, l_parentdata );

        final Collection<Object> l_childdata = (Collection<Object>) l_parentdata.getOrDefault( p_child, new ArrayList<>() );
        l_parentdata.put( p_child, l_childdata );

        l_childdata.addAll( l_data.entrySet().stream().map( Map.Entry::getValue ).collect( Collectors.toList() ) );
        return p_data;
    }

    /**
     * initialize storing
     *
     * @param p_file output filename
     * @param p_iteration number of iteration
     * @return data map
     */
    private static Map<String, Object> storeinitialize( @Nonnull final File p_file, @Nonnegative final int p_iteration ) throws IOException
    {
        final Map<String, Object> l_result = new HashMap<>();
        if ( p_iteration > 1 )
            l_result.putAll( new ObjectMapper().readerFor( Map.class ).readValue( p_file ) );

        return l_result;
    }

    /**
     * stores data into json file
     *
     * @param p_data data
     * @param p_file output filename
     * @throws IOException on writing error
     */
    private static void storewrite( @Nonnull final Map<String, Object> p_data, @Nonnull final File p_file,
                                    @Nonnull final SerializationFeature p_serializationfeature ) throws IOException
    {
        new ObjectMapper()
            .enable( p_serializationfeature )
            .registerModules(
                new SimpleModule().addSerializer( CDescriptiveStatisticSerializer.CLASS, new CDescriptiveStatisticSerializer() ),
                new SimpleModule().addSerializer( CSummaryStatisticSerializer.CLASS, new CSummaryStatisticSerializer() )
            )
            .writeValue( p_file, p_data );
    }
}
