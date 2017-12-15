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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;


/**
 * statistic interface
 */
public interface IStatistic extends BiConsumer<String, Number>, Supplier<Map<String, DescriptiveStatistics>>
{
    /**
     * empty statistic
     */
    IStatistic EMPTY = new IStatistic()
    {
        @Override
        public final IStatistic clear( @Nonnull final String p_name )
        {
            return this;
        }

        @Override
        public final ITimer starttimer( final String p_name )
        {
            return ITimer.EMPTY;
        }

        @Override
        public final void accept( final String p_name, final Number p_number )
        {

        }

        @Override
        public final Map<String, DescriptiveStatistics> get()
        {
            return Collections.emptyMap();
        }
    };


    /**
     * clears a single statistic
     *
     * @param p_name name
     * @return self reference
     */
    IStatistic clear( @Nonnull final String p_name );

    /**
     * starts a timer
     *
     * @param p_name name of the timer
     * @return timer
     */
    ITimer starttimer( final String p_name );



    /**
     * interface of a timer object
     */
    interface ITimer
    {
        /**
         * empty time
         */
        ITimer EMPTY = new ITimer()
        {
            @Override
            public final String name()
            {
                return "";
            }

            @Override
            public final <T> T stop( final T p_value )
            {
                return p_value;
            }

            @Override
            public final void stop()
            {

            }
        };

        /**
         * name of the timer
         */
        String name();

        /**
         * stop timer
         *
         * @param p_value any value
         * @return returns value
         */
        <T> T stop( final T p_value );

        /**
         * stops the timer
         */
        void stop();
    }


    /**
     * json object writer
     */
    class CStatisticSerializer extends StdSerializer<DescriptiveStatistics>
    {
        /**
         * class definition
         */
        public static final Class<DescriptiveStatistics> CLASS = DescriptiveStatistics.class;

        /**
         * serial id
         */
        private static final long serialVersionUID = 1017456322556218672L;

        /**
         * ctor
         */
        CStatisticSerializer()
        {
            this( null );
        }

        /**
         * ctor
         *
         * @param p_class class
         */
        CStatisticSerializer( final Class<DescriptiveStatistics> p_class )
        {
            super( p_class );
        }

        @Override
        public final void serialize( final DescriptiveStatistics p_statistic, final JsonGenerator p_generator,
                                     final SerializerProvider p_serializer ) throws IOException
        {

            p_generator.writeStartObject();
            p_generator.writeNumberField( "geometricmean", p_statistic.getGeometricMean() );
            if ( !Double.isNaN( p_statistic.getKurtosis() ) )
                p_generator.writeNumberField( "kurtosis", p_statistic.getKurtosis() );
            p_generator.writeNumberField( "max", p_statistic.getMax() );
            p_generator.writeNumberField( "mean", p_statistic.getMean() );
            p_generator.writeNumberField( "min", p_statistic.getMin() );
            p_generator.writeNumberField( "count", p_statistic.getN() );
            p_generator.writeNumberField( "25-percentile", p_statistic.getPercentile( 25 ) );
            p_generator.writeNumberField( "50-percentile", p_statistic.getPercentile( 50 ) );
            p_generator.writeNumberField( "75-percentile", p_statistic.getPercentile( 75 ) );
            if ( !Double.isNaN( p_statistic.getSkewness() ) )
                p_generator.writeNumberField( "skewness", p_statistic.getSkewness() );
            p_generator.writeNumberField( "standarddeviation",  p_statistic.getStandardDeviation() );
            p_generator.writeNumberField( "sum", p_statistic.getSum() );
            p_generator.writeNumberField( "variance", p_statistic.getVariance() );
            p_generator.writeArrayFieldStart( "values" );
            p_generator.writeArray(  p_statistic.getValues(), 0, p_statistic.getValues().length );
            p_generator.writeEndArray();
            p_generator.writeEndObject();



        }

    }

}
