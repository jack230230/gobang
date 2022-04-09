# gobang

For User Player:


```
        Player whitePlayer = new SmartPlayer("AI1", Stone.STONE_COLOR_WHITE);
        Player blackPlayer = new UserPlayer("Player", stonePanel);
//        Player blackPlayer = new SmartPlayer("AI2", Stone.STONE_COLOR_BLACK);
```

For AI Player:


```
        Player whitePlayer = new SmartPlayer("AI1", Stone.STONE_COLOR_WHITE);
//        Player blackPlayer = new UserPlayer("Player", stonePanel);
        Player blackPlayer = new SmartPlayer("AI2", Stone.STONE_COLOR_BLACK);
```

To run it:

```shell
./gradlew run
```