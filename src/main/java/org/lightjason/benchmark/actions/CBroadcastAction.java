package org.lightjason.benchmark.actions;

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.benchmark.agents.IBenchmarkAgent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * external broadcast action for sending
 * messages to a set of agents based on
 * a regular expression
 */
public final class CBroadcastAction extends ICommunication
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 923344428639087998L;
    /**
    * action name
     */
    private static final IPath NAME = CPath.from( "message/broadcast" );

    /**
     * ctor
     *
     * @param p_agents agents
     */
    protected CBroadcastAction( final List<IAgent<?>> p_agents )
    {
        super( p_agents );
    }

    @Nonnull
    @Override
    public final IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        final ITerm l_sender = CLiteral.from( FROMFUNCTOR, CRawTerm.from( p_context.agent().<IBenchmarkAgent<?>>raw().index() ) );
        final List<ITrigger> l_trigger = l_arguments.parallelStream()
                                                    .map( ITerm::raw )
                                                    .map( CRawTerm::from )
                                                    .map( i -> CTrigger.from(
                                                        RECEIVETRIGGER,
                                                        CLiteral.from( RECEIVEFUNCTOR, CLiteral.from( MESSAGEFUNCTOR, i ), l_sender )
                                                    ) )
                                                    .collect( Collectors.toList() );

        m_agents.parallelStream()
                .forEach( i -> l_trigger.forEach( j -> i.trigger( j ) ) );

        return CFuzzyValue.from( true );
    }
}
