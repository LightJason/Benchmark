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
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.IOException;


/**
 * descriptive statistic json object writer
 */
public final class CDescriptiveStatisticSerializer extends IBaseStatisticSerializer<DescriptiveStatistics>
{
    /**
     * class definition
     */
    public static final Class<DescriptiveStatistics> CLASS = DescriptiveStatistics.class;
    /**
     * serial id
     */
    private static final long serialVersionUID = -8274926127518408697L;

    /**
     * ctor
     */
    public CDescriptiveStatisticSerializer()
    {
        this( null );
    }

    /**
     * ctor
     *
     * @param p_class class
     */
    public CDescriptiveStatisticSerializer( final Class<DescriptiveStatistics> p_class )
    {
        super( p_class );
    }

    @Override
    public final void serialize( final DescriptiveStatistics p_statistic, final JsonGenerator p_generator,
                                 final SerializerProvider p_serializer ) throws IOException
    {
        p_generator.writeStartObject();
        this.writejson( p_statistic, p_generator );

        p_generator.writeNumberField( "populationvariance", p_statistic.getPopulationVariance() );
        p_generator.writeNumberField( "quadraticmean", p_statistic.getQuadraticMean() );
        p_generator.writeNumberField( "geometricmean", p_statistic.getGeometricMean() );
        p_generator.writeNumberField( "25-percentile", p_statistic.getPercentile( 25 ) );
        p_generator.writeNumberField( "50-percentile", p_statistic.getPercentile( 50 ) );
        p_generator.writeNumberField( "75-percentile", p_statistic.getPercentile( 75 ) );

        if ( !Double.isNaN( p_statistic.getKurtosis() ) )
            p_generator.writeNumberField( "kurtosis", p_statistic.getKurtosis() );

        if ( !Double.isNaN( p_statistic.getSkewness() ) )
            p_generator.writeNumberField( "skewness", p_statistic.getSkewness() );

        p_generator.writeArrayFieldStart( "values" );
        p_generator.writeArray(  p_statistic.getValues(), 0, p_statistic.getValues().length );
        p_generator.writeEndArray();

        p_generator.writeEndObject();
    }

}
