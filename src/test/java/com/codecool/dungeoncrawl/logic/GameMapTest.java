package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {
    @Test
    public void GameMap_DefaultCellTypeEmpty_CellsDoesNotContainAnyOtherCellType () {
        //arrange + act
        GameMap testGameMap = new GameMap(3, 3, CellType.EMPTY);
//        Cell modifiedCell = testGameMap.getCell(0,0);
//        modifiedCell.setType(CellType.FLOOR2);

        boolean foundWrongType = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Cell cell = testGameMap.getCell(i, j);
                if (!(cell.getType().equals(CellType.EMPTY))){
                    foundWrongType = true;
                    break;
                }
            }
        }

        assertFalse(foundWrongType);

    }

    @Test
    public void GetPlayer_SetPlayerToTestPlayerAtCell1_1_ReturnTestPlayer () {
        // arrange
        // construct testGameMap here
        GameMap testGameMap = new GameMap(4,5, CellType.FLOOR);

        Cell testCell = testGameMap.getCell(1,1);
        Player testPlayer = new Player(testCell);


        // act
        testGameMap.setPlayer(testPlayer);

        //assert
        assertEquals(testPlayer, testGameMap.getPlayer());
    }
}