package org.lightjason.benchmark.actions;

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import java.util.List;


/**
 * communication class
 */
public abstract class ICommunication extends IBaseAction
{
    /**
     * receive literal functor
     */
    protected static final String RECEIVEFUNCTOR = "message/receive";
    /**
     * message literal functor
     */
    protected static final String MESSAGEFUNCTOR = "message";
    /**
     * from literal functor
     */
    protected static final String FROMFUNCTOR = "from";
    /**
     * receive trigger
     */
    protected static final ITrigger.EType RECEIVETRIGGER = CTrigger.EType.ADDGOAL;
    /**
     * list agent objects
     */
    protected final List<IAgent<?>> m_agents;

    /**
     * ctor
     *
     * @param p_agents agents
     */
    protected ICommunication( final List<IAgent<?>> p_agents )
    {
        m_agents = p_agents;
    }
}
