package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActorTest {
    @Test
    public void Move_FromCell2_1_WithDxDy1_0_IntoCell3_1() {
        GameMap testGameMap = new GameMap(5, 4, CellType.FLOOR);
        Cell cell = testGameMap.getCell(2,1);
        Player player = new Player(cell);

        player.move(1,0);

        assertEquals(testGameMap.getCell(3,1), player.getCell());
    }
    
    
    

}