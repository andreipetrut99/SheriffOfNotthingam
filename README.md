#### `Sheriff of Nottingham`
This project is a minimalist version of the Sheriff of Nottingham boardgame,
implemented in Java using basic concepts of Object Orientated Programming such
as: inheritance, aggregation, overriding, overloading, trying to respect the
SOLID principles.

I used Main class for initialisation purposes such as starting rounds,
initialisation of variables, instantiation of classes that calculates the
final bonus or printing the scoreboard. 

Each round presumes starting numberOfPlayers sub-rounds, so I set the Sheriff
and Merchants of the sub-round and start the game logic. In this method the
current player is setting up his bag after taking one hand of cards and then
he is inspected by the Sheriff. At this time, the cards which passed the
inspection are going to the 'stand', a Map that stores the ID of each player
and his cards that are going to the Kingdom, a list of cards Ids that are
going to assetsIds. This transfer between Main and Round class it's made
possible by Getters.

Each strategy is stored in 'strategies' package, Bribed and Greedy inherits
Basic strategy, because there are cases when Bribed and Greedy behave just
like a Basic player.

Profit, IllegalBonus, KingQueenBonus, ScoreBoard are classes instantiated at
the final part of the game in Main class. 

For explicit description of methods and more information check the code for
javadoc and comments. 
  