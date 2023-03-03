package ch.unisg.kafka.spring.model;


import java.io.Serializable;


public class BetItResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String betGameNumber;
    private boolean homeTeamWillWin;

    public BetItResult() { }

    public BetItResult(String betGameNumber, boolean homeTeamWillWin) {
        super();
        this.betGameNumber = betGameNumber;
        this.homeTeamWillWin = homeTeamWillWin;
    }

    public String getBetGameNumber() {
        return betGameNumber;
    }

    public void setBetGameNumber(String betGameNumber) {
        this.betGameNumber = betGameNumber;
    }

    public boolean isHomeTeamWillWin() {
        return homeTeamWillWin;
    }

    public void setHomeTeamWillWin(boolean homeTeamWillWin) {
        this.homeTeamWillWin = homeTeamWillWin;
    }

    @Override
    public String toString() {
        return "BetItResult [bet-Topic=" + betGameNumber + ", homeTeamWillWin="
                + homeTeamWillWin + "]";
    }

}