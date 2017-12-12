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

package org.lightjason.benchmark;

import org.lightjason.agentspeak.agent.IAgent;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.logging.LogManager;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * main application with runtime
 */
public final class CMain
{

    static
    {
        // logger
        LogManager.getLogManager().reset();
    }


    /**
     * private constructor to avoid any instantiation
     */
    private CMain()
    {}


    /**
     * main method
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {

    }

    /*
     * initialize the simulation structure, generate data-structure
     * for all agents with name, environment, actions and return
     * collection with agents for execution
     *
     * @param p_cli command-line parameter
     * @return collection with agents for execution
     *
    private static Collection<IAgent<?>> initialize( final CommandLine p_cli )
    {
        // runtime agent collection
        final List<IAgent<?>> l_agents = Collections.synchronizedList( new ArrayList<>() );

        // global set with all possible agent actions
        final Set<IAction> l_actions = Collections.unmodifiableSet(
                Stream.concat(
                        Stream.of(
                                new CSendAction( l_agents )
                        ),
                        CCommon.actionsFromPackage()
                ).collect( Collectors.toSet() )
        );


        StreamUtils.zip(
            // read counter values to generate the set of agents
            Arrays.stream( p_cli.getOptionValue( "agents", "" ).split( "," ) )
                    .map( String::trim )
                    .filter( i -> !i.isEmpty() )
                    .mapToInt( Integer::parseInt )
                    .boxed(),


            StreamUtils.zip(
                // read the generator type for each ASL file
                Arrays.stream( p_cli.getOptionValue( "generator", "" ).split( "," ) )
                        .map( String::trim )
                        .filter( i -> !i.isEmpty() ),

                // read each ASL file
                Arrays.stream( p_cli.getOptionValue( "asl", "" ).split( "," ) )
                        .map( String::trim )
                        .filter( i -> !i.isEmpty() ),

                // create a tuple for each ASL the generator and ASL file
                ( i, j ) -> new AbstractMap.SimpleImmutableEntry<>( EGenerator.from( i ), j )
            )
                // read the file data and create the generator
                .map( i ->
                {
                    try
                            (
                                    final FileInputStream l_stream = new FileInputStream( i.getValue() );
                            )
                    {
                        return i.getKey().generate( l_stream, l_actions.stream(), l_agents );
                    }
                    catch ( final Exception l_exception )
                    {
                        l_exception.printStackTrace();
                        return null;
                    }
                } )
                .filter( Objects::nonNull ),

            // create a tuple of generator and number of agents
            ( i, j ) -> new AbstractMap.SimpleImmutableEntry<>( j, i )
        )
            // generate the agents
            .flatMap( i -> i.getKey().generatemultiple( i.getValue() ) )
            .forEach( i ->
            {
            } );

        return l_agents.values();
    }
    */

    // === runtime execution ===================================================================================================================================

    /**
     * executes the simulation
     *
     * @param p_agents collection with all agents
     * @param p_steps number of simulation steps
     * @param p_parallel run agents in parallel
     */
    private static void execute( final Collection<IAgent<?>> p_agents, final int p_steps, final boolean p_parallel )
    {
        if ( p_agents.size() == 0 )
        {
            System.err.println( "no agents exists for execution" );
            System.exit( -1 );
        }

        IntStream.range( 0, p_steps )
                .forEach( i -> CMain.optionalparallelstream( p_agents.stream(), p_parallel ).forEach( CMain::execute ) );
    }

    /**
     * creates an optional parallel stream
     *
     * @param p_stream input stream
     * @param p_parallel parallel execution
     * @tparam T stream element type
     * @return stream
     */
    private static <T> Stream<T> optionalparallelstream( final Stream<T> p_stream, final boolean p_parallel )
    {
        return p_parallel ? p_stream.parallel() : p_stream;
    }

    /**
     * execute callable object with catching exception
     *
     * @param p_object callable
     */
    private static void execute( final Callable<?> p_object )
    {
        try
        {
            p_object.call();
        }
        catch ( final Exception l_exception )
        {
            l_exception.printStackTrace();
        }
    }

}
