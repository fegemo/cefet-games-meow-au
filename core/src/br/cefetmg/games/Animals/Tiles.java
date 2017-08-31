/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.Animals;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Dota
 */
public class Tiles {
    public Vector2 tileVector;
    public int tile_Type;

    public Tiles(Vector2 tileVector) {
        this.tileVector = tileVector;
        this.tile_Type = MathUtils.random(4);
    }
    private void tileChange(){
        this.tile_Type = MathUtils.random(4);
    }
}
