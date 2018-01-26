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

package org.lightjason.benchmark.statistic;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.io.IOException;


/**
 * summary statistic json object writer
 */
public class CSummaryStatisticSerializer extends IBaseStatisticSerializer<SummaryStatistics>
{
    /**
     * class definition
     */
    public static final Class<SummaryStatistics> CLASS = SummaryStatistics.class;
    /**
     * serial id
     */
    private static final long serialVersionUID = -4064447879946082636L;

    /**
     * ctor
     */
    public CSummaryStatisticSerializer()
    {
        this( null );
    }

    /**
     * ctor
     *
     * @param p_class class
     */
    public CSummaryStatisticSerializer( final Class<SummaryStatistics> p_class )
    {
        super( p_class );
    }

    @Override
    public final void serialize( final SummaryStatistics p_statistic, final JsonGenerator p_generator,
                                 final SerializerProvider p_serializer ) throws IOException
    {
        p_generator.writeStartObject();
        this.writejson( p_statistic, p_generator );

        p_generator.writeNumberField( "populationvariance", p_statistic.getPopulationVariance() );
        p_generator.writeNumberField( "quadraticmean", p_statistic.getQuadraticMean() );
        p_generator.writeNumberField( "secondmoment", p_statistic.getSecondMoment() );
        p_generator.writeNumberField( "sumoflogs", p_statistic.getSumOfLogs() );

        p_generator.writeEndObject();
    }
}
