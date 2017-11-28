package org.lightjason.benchmark.agents;

import org.lightjason.benchmark.environment.IEnvironment;

import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Map;
import java.util.stream.Stream;

/**
 * agent class to represent
 * a type of agent
 */

public final class CDefaultAgent extends IEnvironmentAgent<CDefaultAgent>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 1L;
    /**
     * constructor
     *
     * @param p_configuration agent configuration
     * @param p_environment environment reference
     * @param p_name name of the agent
     */
    private CDefaultAgent( @Nonnull final IAgentConfiguration<IEnvironmentAgent<CDefaultAgent>> p_configuration,
                                     @Nonnull final IEnvironment p_environment, @Nonnull final String p_name )
    {
        super( p_configuration, p_environment, p_name );
    }



        /**
     * generator of a specified type of agents
     */
    public static final class CGenerator extends IGenerator<CDefaultAgent>
    {

        /**
         * constructor
         *
         * @param p_stream ASL input stream
         * @param p_defaultaction default actions
         * @param p_environment environment reference
         * @param p_agents map with agents and names
         * @throws Exception on parsing error
         */
        public CGenerator( @Nonnull final InputStream p_stream, @Nonnull final Stream<IAction> p_defaultaction, @Nonnull final IEnvironment p_environment,
                           @Nonnull final Map<String, IAgent<?>> p_agents ) throws Exception
        {
            super( p_stream, Stream.concat( p_defaultaction, CCommon.actionsFromAgentClass( CDefaultAgent.class ) ), p_environment, p_agents );
        }

        @Nullable
        @Override
        public final CDefaultAgent generatesingle( @Nullable final Object... p_data )
        {
            return this.initializeagent(
                    new CDefaultAgent(
                            m_configuration,
                            m_environment,
                            MessageFormat.format( "{0} {1}", "DefaultAgent", m_counter.getAndIncrement() )
                    )
            );
        }

    }
}
