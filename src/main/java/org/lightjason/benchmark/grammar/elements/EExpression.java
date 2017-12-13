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

package org.lightjason.benchmark.grammar.elements;

import javax.annotation.Nonnull;
import java.text.MessageFormat;


/**
 * operation
 */
public enum EExpression implements IFunction
{
    POW( "^" ),
    MULTIPLY( "*" ),
    DIVIDE( "/" ),
    MODULO( "%" ),
    PLUS( "+" ),
    MINUS( "-" );

    /**
     * operator
     */
    private final String m_operator;

    /**
     * ctor
     *
     * @param p_operator string value
     */
    EExpression( @Nonnull final String p_operator )
    {
        m_operator = p_operator;
    }


    @Override
    public final Number apply( final Number p_lhs, final Number p_rhs )
    {
        switch ( this )
        {
            case POW:
                return Math.pow( p_lhs.doubleValue(), p_rhs.doubleValue() );

            case MULTIPLY:
                return p_lhs.doubleValue() * p_rhs.doubleValue();

            case DIVIDE:
                return p_lhs.doubleValue() / p_rhs.doubleValue();

            case PLUS:
                return p_lhs.doubleValue() + p_rhs.doubleValue();

            case MINUS:
                return p_lhs.doubleValue() - p_rhs.doubleValue();

            case MODULO:
                return p_lhs.doubleValue() % p_rhs.doubleValue();

            default:
                throw new RuntimeException( MessageFormat.format( "unknown operator", this ) );
        }
    }


    @Override
    public final String toString()
    {
        return m_operator;
    }
}
