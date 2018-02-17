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

!main.

+!main <-
    +fib( value(5), index(0) )
.

+fib( value(V), index(I) )
    : V > 2 <-
        I++;
        X = V - 1;
        Y = V - 2;

        +fib( value(X), index(I) );
        +fib( value(Y), index(I) )

    : V == 2 <-
        !sum

   :  V == 1 <-
        !sum     
.

+!sum(I)
    : I > 0 <-
        generic/print(I);

        X = I - 1;
        >>fib( value(N), index(X) );
        Y = I - 2;
        >>fib( value(M), index(Y) );
        
        >>fibsum(Z);
        -fibsum(Z);
        Z = Z + X + Y;
        +fibsum(Z);
        
        I--;
        !sum(I)

    : I == 0 <-
        >>fibsum(X);
        generic/print(X)
.
        
