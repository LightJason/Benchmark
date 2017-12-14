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

import org.lightjason.benchmark.agent.IBenchmarkAgent;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * agent storage
 */
public interface IAgentStorage extends Function<String, IBenchmarkAgent>, Consumer<IBenchmarkAgent>
{
    /**
     * build the neighbor structure
     *
     * @return self reference
     */
    @Nonnull
    IAgentStorage buildneighbor();

    @Nonnull
    String left( @Nonnull final String p_id );

    @Nonnull
    String right( @Nonnull final String p_id );

    /**
     * returns agent stream
     *
     * @return stream
     */
    Stream<IBenchmarkAgent> stream();

}
