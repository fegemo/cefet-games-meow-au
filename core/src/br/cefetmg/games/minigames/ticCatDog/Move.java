package br.cefetmg.games.minigames.ticCatDog;

public class Move
{
    private int x, y; //Index da matrix
    private int score; //Utilizado no algoritmo minmax

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Move(int score) {
        this.score = score;
    }
    
    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }
    
    
}
