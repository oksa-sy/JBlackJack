# JBlackJack

A Java implementation of the classic Blackjack card game. The project was developed as a university exam (M-Z attendance course), focusing on step-by-step Software Engineering principles, structured design, and object-oriented logic.

## Project Description

This video game offers a complete Blackjack experience where the user can challenge the Dealer and interact with artificial players. The entire system was designed starting from deep research on game rules and design patterns, sketching class diagrams to clarify roles and relations between them.

## Architecture and Design Patterns

The code was developed following the **MVC (Model-View-Controller)** architectural pattern. Several Design Patterns were integrated to ensure a structured implementation:

* **Observer-Observable:** Used to manage state changes automatically between Model and View. The `Card` and `Entity` classes (with its subclasses) are observable: they notify the View when a card is flipped, drawn, or when the player's balance and points are updated.


* **Template Method Pattern:** Employed in the View for managing various window panels. An abstract superclass `Panel` (extending `JPanel`) provides support methods and an abstract `initialize()` method implemented by concrete subclasses.


* **Singleton Pattern:** Guarantees that panels like `HomePanel`, `UserProfilePanel`, `NumberOfPlayersPanel`, `PlaceBetPanel`, and `GameView` are instantiated only once via lazy initialization to maintain a unique state. It is also applied to the `AudioManager` class.


* **State Pattern (Inspired):** Turn management is handled by the `GameStateManager` class and its `gameLoop()` method. This adapts the game behavior based on the current state via a `GameState` enum, using a `Timer` to ensure fluid transitions.



## System Components

### Model

* Centralizes game logic and rules through the `GameModel` class.


* Implements core physical components such as `Card` and `Deck`.


* Abstracts shared behaviors in the `Entity` class, extended by `Player` (featuring a `Level` enum based on games won) and `Dealer`.


* Integrates automatic bots via the `ArtificialPlayer` class, which extends `Player` and overrides methods to make automated decisions.


* Uses **Java Streams** to efficiently filter data and iterate rapidly among up to five potential players.



### View

* Built entirely in **Java Swing** for its practicality and simplicity.


* The `GameFrame` class manages the main window and dynamic transitions between panels.


* **User Interface:** Features dedicated panels for starting the game, placing bets, selecting the number of players, and a `UserProfilePanel` to change the avatar and display statistics.


* **Card Rendering:** The `EntityView` superclass utilizes a custom `OverlapLayout` to visually stack cards. Scores remain hidden for all entities except the player until cards are revealed.


* **Animations:** Managed via Swing's `Timer` component rather than manual threads. `CardView` animates drawn or flipped cards, while `TextAnimation` provides a zoom in/zoom out text effect for victory conditions.


* **Audio:** Controlled by `AudioManager`, featuring a `loop()` method for continuous playback and dynamic sound effects for card interactions.



### Controller

* Initializes Model and View components within its constructor.


* Assigns button listeners to execute actions based on game control logic, seamlessly bridging visual representation with internal state mechanics.
