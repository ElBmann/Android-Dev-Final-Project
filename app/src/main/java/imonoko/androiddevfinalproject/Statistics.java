package imonoko.androiddevfinalproject;

/**
 * Created by ImonokoI on 12/3/2017.
 */
public class Statistics {
    private int id;
    private int wins;
    private int losses;
    private int reRolls;
    private int score;
    private int draws;

    public Statistics(int id, int wins, int losses, int reRolls, int score, int draws) {
        this.id=id;
        this.wins = wins;
        this.losses = losses;
        this.reRolls = reRolls;
        this.score = score;
        this.draws = draws;
    }
    public int getID()
    {return id;}
    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getReRolls() {
        return reRolls;
    }

    public void setReRolls(int reRolls) {
        this.reRolls = reRolls;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }
    public String toString() {
        return "Statistics{" +id+//id in toString is only used for debugging purposes
                ", wins=" + wins +
                ", losses=" + losses +
                ", reRolls=" + reRolls +
                ", score=" + score +
                ", draws=" + draws +
                '}';
    }
}
