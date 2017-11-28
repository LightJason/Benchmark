!main.



+!main <-
    !counting( Upperbound )
.    

+!couting(X)
    : X > 0 <-
        X--;
        !counting(X)
    : X == 0 <-
        !sendtoken
        
.

+!sendtoken
    : >>nexttoken <-
        NextIndex = MyIndex + 1;
        message/send( NextIndex, "finish" )
    : !>>nexttoken <-
        !sendtoken
.            

+!message/receive( message(M), from(F) ) 
    : (M == "finish") && ( F == MyIndex-1 ) <-
        +nexttoken
.    
