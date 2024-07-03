package com.lestarieragemilang.app.desktop.Utilities;

import java.util.Random;

public class GenerateRandomID {
    private int id;

    public GenerateRandomID() {
        Random random = new Random();
        id = random.nextInt(1000);
    }

    public int getId() {
        return id;
    }
}
