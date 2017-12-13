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

package org.lightjason.benchmark.grammar;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.lightjason.benchmark.grammar.elements.CConstant;
import org.lightjason.benchmark.grammar.elements.CVariable;
import org.lightjason.benchmark.grammar.elements.IFunction;


/**
 * formular parser
 */
@SuppressWarnings( {"all", "warnings", "unchecked", "unused", "cast"} )
public final class CASTVisitorFormular extends AbstractParseTreeVisitor<IFunction> implements FormularVisitor<IFunction>
{

    @Override
    public IFunction visitFormular( final FormularParser.FormularContext p_context )
    {
        return null;
    }

    @Override
    public IFunction visitBracketexpression( final FormularParser.BracketexpressionContext p_context )
    {
        return null;
    }

    @Override
    public IFunction visitExpression( final FormularParser.ExpressionContext p_context )
    {
        if ( p_context.element() != null )
            return this.visit( p_context.element() );

        return null;
    }

    @Override
    public IFunction visitElement( final FormularParser.ElementContext p_context )
    {
        return p_context.number() != null ? this.visit( p_context.number() ) : new CVariable();
    }

    @Override
    public IFunction visitNumber( final FormularParser.NumberContext p_context )
    {
        return new CConstant( Double.valueOf( p_context.getText() ) );
    }
}
