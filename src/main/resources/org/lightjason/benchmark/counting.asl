!main.

+!main <-
    !count( MaxCount )
.

+!count(X)
    : X > 0 <-
        X--;
        !count(X)
.
