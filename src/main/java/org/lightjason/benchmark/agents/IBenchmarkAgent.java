package org.lightjason.benchmark.agents;

import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;

import org.lightjason.benchmark.environment.IEnvironment;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * abstract class to define an
 * agent with environment
 */

public abstract class IBenchmarkAgent<T extends IBenchmarkAgent<?>> extends IBaseAgent<T>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 639087239899834442L;
    /**
     * index of the agent
     */
    private final int m_index;

    /**
     * ctor
     *
     * @param p_configuration agent configuration
     * @param p_index index of the agent
     */
    protected IBenchmarkAgent(@Nonnull final IAgentConfiguration<T> p_configuration, @Nonnegative final int p_index )
    {
        super( p_configuration );
        m_index = p_index;
    }

    /**
     * returns the agent index
     *
     * @return agent index
     */
    @Nonnegative
    public final int index()
    {
        return m_index;
    }

    @Override
    public final int hashCode()
    {
        return m_index;
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IBenchmarkAgent<?>) && ( p_object.hashCode() == this.hashCode() );
    }



    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * abstract agent generatorclass for all agents
     */
    protected abstract static class IGenerator<T extends IBenchmarkAgent<?>> extends IBaseAgentGenerator<IBenchmarkAgent<T>>
    {
        /**
         * agent number counter
         */
        protected final AtomicInteger m_counter = new AtomicInteger();
        /**
         * agent list
         */
        private final List<IAgent<?>> m_agents;


        /**
         * constructor
         *
         * @param p_stream ASL input stream
         * @param p_actions action stream
         * @param p_variablebuilder variable builder
         * @param p_agents agent list
         * @throws Exception on any error
         */
        protected IGenerator( @Nonnull final InputStream p_stream, @Nonnull final Stream<IAction> p_actions,
                              @Nonnull final IVariableBuilder p_variablebuilder,
                              @Nonnull final List<IAgent<?>> p_agents ) throws Exception
        {
            super( p_stream,
                   Stream.concat(
                       CCommon.actionsFromAgentClass( IBenchmarkAgent.class ),
                       p_actions
                   ).collect( Collectors.toSet() ),
                   p_variablebuilder
            );

            m_agents = p_agents;
        }


        /**
         * initialize the agent for the simulation
         *
         * @param p_agent agent object
         * @return agent object
         */
        @Nonnull
        protected final T initializeagent( @Nonnull final T p_agent )
        {
            m_agents.add( p_agent );
            return p_agent;
        }

    }
}
