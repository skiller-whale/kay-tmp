# Testing Layers - Java Wordle Exercise

Java implementation of Wordle demonstrating TDD at different layers. The answer is always "whale".


## Project Structure

```
src/
├── main/java/com/skillerwhale/wordle/
│   ├── core/                    # Core game logic
│   │   ├── Dictionary.java      # Word dictionary
│   │   ├── ValidateGuess.java   # Input validation (TODO)
│   │   ├── GameStatus.java      # Game state logic
│   │   └── EvaluateGuess.java   # Color evaluation (TODO)
│   ├── html/                    # HTML rendering components
│   │   ├── PageRenderer.java    # Main page layout
│   │   ├── GuessForm.java       # Input form
│   │   ├── GuessesFeedback.java # Display guesses
│   │   ├── StatusMessage.java   # Game status messages
│   │   └── ErrorMessage.java    # Error display
│   ├── GameState.java           # URL state management
│   ├── WordleController.java    # Web Server controller
│   └── WordleApplication.java   # Main application
└── test/java/com/skillerwhale/wordle/
    ├── core/                    # Unit tests for core logic
    │   ├── ValidateGuessTest.java
    │   ├── GameStatusTest.java
    │   └── EvaluateGuessTest.java
    ├── WordleControllerTest.java # Controller/server tests
    └── integration/
        └── GameTest.java        # End-to-end integration tests
```

## Quick Start

```bash
# Run development server
docker compose up dev-server
# ➡️ Game at http://localhost:3001

# Run layered tests with file watching
docker compose run tests
```

