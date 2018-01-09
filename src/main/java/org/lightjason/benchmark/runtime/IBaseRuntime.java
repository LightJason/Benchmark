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

package org.lightjason.benchmark.runtime;

import org.lightjason.benchmark.agent.IBenchmarkAgent;
import org.pmw.tinylog.Logger;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.function.Consumer;


/**
 * base runtime structure
 */
public abstract class IBaseRuntime implements IRuntime
{
    /**
     * runtime type
     */
    private final ERuntime m_type;
    /**
     * value of the pool
     */
    private final int m_value;


    /**
     * ctor
     *  @param p_type runtime type
     * @param p_value runtime value
     */
    protected IBaseRuntime( @Nonnull final ERuntime p_type, @Nonnegative final int p_value
    )
    {
        m_type = p_type;
        m_value = p_value;
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} {1}", m_type, m_value );
    }

    /**
     * agent execution
     *
     * @param p_agent agent execution
     * @param p_errorfunction error functino
     */
    protected final void execute( @Nonnull final IBenchmarkAgent p_agent, @Nonnull final Consumer<Exception> p_errorfunction )
    {
        try
        {
            p_agent.call();
        }
        catch ( final Exception l_exception )
        {
            Logger.trace( l_exception );
            p_errorfunction.accept( l_exception );
        }
    }
}
