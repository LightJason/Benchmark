package org.lightjason.benchmark.agents;

import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.benchmark.environment.IEnvironment;

import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

/**
 * agent class to represent
 * a type of agent
 */

public final class CBenchmarkAgent extends IBenchmarkAgent<CBenchmarkAgent>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 1L;
    /**
     * constructor
     *
     * @param p_configuration agent configuration
     * @param p_index index of the agent
     */
    private CBenchmarkAgent(@Nonnull final IAgentConfiguration<CBenchmarkAgent> p_configuration,
                            @Nonnegative int p_index )
    {
        super( p_configuration, p_index );
    }



        /**
     * generator of a specified type of agents
     */
    public static final class CGenerator extends IGenerator<CBenchmarkAgent>
    {

        /**
         * constructor
         *
         * @param p_stream ASL input stream
         * @param p_defaultaction default actions
         * @param p_agents map with agents and names
         * @throws Exception on parsing error
         */
        public CGenerator( @Nonnull final InputStream p_stream, @Nonnull final Stream<IAction> p_defaultaction,
                           @Nonnull final IVariableBuilder p_variablebuilder,
                           @Nonnull final List<IAgent<?>> p_agents ) throws Exception
        {
            super(
                    p_stream,
                    Stream.concat( p_defaultaction, CCommon.actionsFromAgentClass( CBenchmarkAgent.class ) ),
                    p_variablebuilder,
                    p_agents
            );
        }

        @Nullable
        @Override
        public final CBenchmarkAgent generatesingle(@Nullable final Object... p_data )
        {
            return this.initializeagent(
                    new CBenchmarkAgent(
                            m_configuration,
                            m_counter.getAndIncrement() )
                    )
            );
        }

    }
}
