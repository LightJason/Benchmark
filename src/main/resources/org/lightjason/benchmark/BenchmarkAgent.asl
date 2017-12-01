!main.



+!main <-
    !counting( Upperbound )
.    

+!couting(X)
    : X > 0 <-
        X--;
        !counting(X)
    : X == 0 <-
        U = generic/uuid;
        +token(U);
        message/send( NextIndex, U )
.

+!message/receive( message(M), from(F) )
    : >>token(T) && (M == U) && ( F == MyIndex - 1 ) <-
        terminate
    : >>token(T) && (M != U) && ( F == MyIndex - 1 ) <-
        message/send( NextIndex, M )
    : ~>>token(T) && ( F == MyIndex - 1 ) <-
        message/send( NextIndex, M )
.

