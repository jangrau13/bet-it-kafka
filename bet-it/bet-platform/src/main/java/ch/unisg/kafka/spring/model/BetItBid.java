package ch.unisg.kafka.spring.model;


import java.io.Serializable;


public class BetItBid implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String bidderName;
    private String betGameNumber;
    private float betBase;
    private boolean homeTeamWillWin;

    public BetItBid() { }

    public BetItBid(String name, String bidderName, String betGameNumber, int betBase, boolean homeTeamWillWin) {
        super();
        this.name = name;
        this.bidderName = bidderName;
        this.betGameNumber = betGameNumber;
        this.betBase = betBase;
        this.homeTeamWillWin = homeTeamWillWin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public String getBetGameNumber() {
        return betGameNumber;
    }

    public void setBetGameNumber(String betGameNumber) {
        this.betGameNumber = betGameNumber;
    }

    public float getBetBase() {
        return betBase;
    }

    public void setBetBase(float betBase) {
        this.betBase = betBase;
    }

    public boolean isHomeTeamWillWin() {
        return homeTeamWillWin;
    }

    public void setHomeTeamWillWin(boolean homeTeamWillWin) {
        this.homeTeamWillWin = homeTeamWillWin;
    }

    @Override
    public String toString() {
        return "BetItBid [name=" + name + ", bidderName=" + bidderName
                + ", bet-Topic=" + betGameNumber + ", bet-Base=" + betBase + ", homeTeamWillWin="
                + homeTeamWillWin + "]";
    }

}