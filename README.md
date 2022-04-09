# gobang

For User Player:

https://github.com/jack230230/gobang/blob/main/src/GobangFrame.java#L35

```
        Player whitePlayer = new SmartPlayer("AI1", Stone.STONE_COLOR_WHITE);
        Player blackPlayer = new UserPlayer("Player", stonePanel);
//        Player blackPlayer = new SmartPlayer("AI2", Stone.STONE_COLOR_BLACK);
```

For AI Player:

https://github.com/jack230230/gobang/blob/main/src/GobangFrame.java#L35

```
        Player whitePlayer = new SmartPlayer("AI1", Stone.STONE_COLOR_WHITE);
//        Player blackPlayer = new UserPlayer("Player", stonePanel);
        Player blackPlayer = new SmartPlayer("AI2", Stone.STONE_COLOR_BLACK);
```
