import java.util.*;

public class World {
    private Random random = new Random();
    private int Alive = 0;
    private Boolean[][] World;
    private Boolean ChangeState; // false if the world pattern becomes static

    public World() {
        World = new Boolean[1][1];
        ChangeState = false;
        World[0][0] = false;
    }

    public World(int Size) {
        World = new Boolean[Size][Size];
        ChangeState = true;

        for (int i = 0; i < World.length; i++) {
            for (int j = 0; j < World.length; j++) {
                if (random.nextBoolean()) {
                    Alive++;
                    World[i][j] = true;
                }
                else {
                    World[i][j] = false;
                }
            }
        }
    }

    public int GetAlive() {
        return Alive;
    }

    public Boolean[][] GetWorld() {
        return World;
    }

    public Boolean GetChangeState() {
        return ChangeState;
    }

    public void SetWorld(Boolean[][] World2) {
        World = new Boolean[World2.length][World2.length];
        ChangeState = true;

        for (int i = 0; i < World.length; i++) {
            for (int j = 0; j < World.length; j++) {
                if (World2[i][j]) {
                    Alive++;
                    World[i][j] = true;
                }
                else {
                    World[i][j] = false;
                }
            }
        }
    }


    public void Generation() {
        Boolean[][] World2 = new Boolean[World.length][World.length];
        ChangeState = false;
        Alive = 0;

        for (int i = 0; i < World.length; i++) {
            for (int j = 0; j < World.length; j++) {
                int neighbours = 0;

                if (World[i][j]) {
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if (x == 0 && y == 0) {
                                continue;
                            }

                            int neighbourX = (j + x + World.length) % World.length; // Loops back if overloaded
                            int neighbourY = (i + y + World.length) % World.length;

                            if (World[neighbourY][neighbourX]) {
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

                            int neighbourX = (j + x + World.length) % World.length;
                            int neighbourY = (i + y + World.length) % World.length;

                            if (World[neighbourY][neighbourX]) {
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

                if (World2[i][j] != World[i][j]) {
                    ChangeState = true;
                }
            }
        }

        for (int i = 0; i < World2.length; i++) {
            for (int j = 0; j < World2.length; j++) {
                if (World2[i][j]) {
                    Alive++;
                    World[i][j] = true;
                }
                else {
                    World[i][j] = false;
                }
            }
        }
    }
}
