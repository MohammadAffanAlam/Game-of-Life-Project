import java.util.*;

public class World {
    private Random random = new Random();
    private int alive = 0;
    private Boolean[][] world;
    private Boolean changeState; // false if the world pattern becomes static

    public World() {
        world = new Boolean[1][1];
        changeState = false;
        world[0][0] = false;
    }

    public World(int Size) {
        world = new Boolean[Size][Size];
        changeState = true;

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world.length; j++) {
                if (random.nextBoolean()) {
                    alive++;
                    world[i][j] = true;
                }
                else {
                    world[i][j] = false;
                }
            }
        }
    }

    public int GetAlive() {
        return alive;
    }

    public Boolean[][] GetWorld() {
        return world;
    }

    public Boolean GetChangeState() {
        return changeState;
    }

    public void SetWorld(Boolean[][] World2) {
        world = new Boolean[World2.length][World2.length];
        changeState = true;

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world.length; j++) {
                if (World2[i][j]) {
                    alive++;
                    world[i][j] = true;
                }
                else {
                    world[i][j] = false;
                }
            }
        }
    }


    public void Generation() {
        Boolean[][] World2 = new Boolean[world.length][world.length];
        changeState = false;
        alive = 0;

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world.length; j++) {
                int neighbours = 0;

                if (world[i][j]) {
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if (x == 0 && y == 0) {
                                continue;
                            }

                            int neighbourX = (j + x + world.length) % world.length; // Loops back if overloaded
                            int neighbourY = (i + y + world.length) % world.length;

                            if (world[neighbourY][neighbourX]) {
                                neighbours++;
                            }
                        }
                    }

                    if (neighbours == 2 || neighbours == 3) {
                        World2[i][j] = true;
                    }
                    else {
                        World2[i][j] = false;
                    }
                }
                else {
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if (x == 0 && y == 0) {
                                continue;
                            }

                            int neighbourX = (j + x + world.length) % world.length;
                            int neighbourY = (i + y + world.length) % world.length;

                            if (world[neighbourY][neighbourX]) {
                                neighbours++;
                            }
                        }
                    }

                    if (neighbours == 3) {
                        World2[i][j] = true;
                    }
                    else {
                        World2[i][j] = false;
                    }
                }

                if (World2[i][j] != world[i][j]) {
                    changeState = true;
                }
            }
        }

        for (int i = 0; i < World2.length; i++) {
            for (int j = 0; j < World2.length; j++) {
                if (World2[i][j]) {
                    alive++;
                    world[i][j] = true;
                }
                else {
                    world[i][j] = false;
                }
            }
        }
    }
}
