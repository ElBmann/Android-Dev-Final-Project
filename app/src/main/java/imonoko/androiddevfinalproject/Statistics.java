package imonoko.androiddevfinalproject;


public class Statistics {
    private int ID;
    private int wins;
    private int losses;
    private int reRolls;
    private int score;
    private int draws;
    public Statistics()
    {
        //this.ID = ID;needs to be changed
        //should be created along with an account to generate proper id
        wins = 0;
        losses = 0;
        reRolls = 0;
        score = 0;
        draws = 0;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

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
        return "Statistics\n" +
                "\nID = \t" + ID +
                "\nScore \t= " + score +
                "\n" + wins + " wins" +
                "\n" + losses + " losses" +
                "\n" + reRolls + " re-rolls ";
    }
}

