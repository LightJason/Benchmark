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

import org.lightjason.benchmark.scenario.CScenario;

import java.util.logging.LogManager;


/**
 * main application with runtime
 *
 * @todo java -jar -XX:+UseConcMarkSweepGC -Xmx3584M
 * @todo https://stackoverflow.com/questions/21163108/custom-thread-pool-in-java-8-parallel-stream
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
     * @throws Exception thrown on any error
     */
    public static void main( final String[] p_args ) throws Exception
    {
        if ( p_args.length != 1 )
            throw new RuntimeException( "argument with scenario configuration must be set" );

        /*
        https://bl.ocks.org/mbostock/4061502
        http://bl.ocks.org/mbostock/3943967
        https://bl.ocks.org/mbostock/1256572
        http://square.github.io/crossfilter/
        */
        CScenario.build( p_args[0] ).run();
    }

}
