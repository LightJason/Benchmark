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

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.lightjason.benchmark.grammar.elements.CConstant;
import org.lightjason.benchmark.grammar.elements.CVariable;
import org.lightjason.benchmark.grammar.elements.EOperator;
import org.lightjason.benchmark.grammar.elements.IFunction;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.Function;


/**
 * formular parser
 */
public final class CFormularParser implements IParser
{
    @Override
    public final Function<Number, Number> apply( final InputStream p_stream )
    {
        final FormularLexer l_lexer;
        try
        {
            l_lexer = new FormularLexer( CharStreams.fromStream( p_stream ) );
            l_lexer.removeErrorListeners();

            final FormularParser l_parser = new FormularParser( new CommonTokenStream( l_lexer ) );
            l_parser.removeErrorListeners();

            final IFunction l_function = new CASTVisitorFormular().visit( l_parser.formular() );
            return i -> l_function.apply( i, 0 );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }


    /**
     * formular parser
     */
    @SuppressWarnings( {"all", "warnings", "unchecked", "unused", "cast"} )
    private static final class CASTVisitorFormular extends AbstractParseTreeVisitor<IFunction> implements FormularVisitor<IFunction>
    {

        @Override
        public IFunction visitFormular( final FormularParser.FormularContext p_context )
        {
            return this.visit( p_context.expression() );
        }

        @Override
        public IFunction visitBracketexpression( final FormularParser.BracketexpressionContext p_context )
        {
            return this.visit( p_context.expression() );
        }

        @Override
        public IFunction visitExpression( final FormularParser.ExpressionContext p_context )
        {
            if ( p_context.bracketexpression() != null )
                return this.visit( p_context.bracketexpression() );

            if ( p_context.element() != null )
                return this.visit( p_context.element() );

            if ( p_context.POW() != null )
                return EOperator.POW.get( this.visit( p_context.expression( 0 ) ), this.visit( p_context.expression( 1 ) ) );

            if ( p_context.MULTIPLY() != null )
                return EOperator.MULTIPLY.get( this.visit( p_context.expression( 0 ) ), this.visit( p_context.expression( 1 ) ) );

            if ( p_context.DIVIDE() != null )
                return EOperator.DIVIDE.get( this.visit( p_context.expression( 0 ) ), this.visit( p_context.expression( 1 ) ) );

            if ( p_context.MODULO() != null )
                return EOperator.MODULO.get( this.visit( p_context.expression( 0 ) ), this.visit( p_context.expression( 1 ) ) );

            if ( p_context.PLUS() != null )
                return EOperator.PLUS.get( this.visit( p_context.expression( 0 ) ), this.visit( p_context.expression( 1 ) ) );

            if ( p_context.MINUS() != null )
                return EOperator.MINUS.get( this.visit( p_context.expression( 0 ) ), this.visit( p_context.expression( 1 ) ) );

            throw new RuntimeException( "parsing error, unknown expression" );
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


}
