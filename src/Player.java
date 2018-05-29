import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Player {
    private int id;
    private String name;
    private  boolean landLord;
    private List<Cards> cardsList;
    private List<Cards> chooseCards;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.landLord = false;
        this.cardsList = new ArrayList<>();  //玩家手中当前所有的牌
        this.chooseCards = new ArrayList<>();     //玩家当前选择想出的牌
    }

    public void setLandLord(boolean landLord) {
        this.landLord = landLord;
    }

    public boolean isLandLord() {
        return landLord;
    }

    public List<Cards> getChooseCards() {
        return chooseCards;
    }

    public void setChooseCards(List<Cards> chooseCards) {
        this.chooseCards = chooseCards;
    }

    public List<Cards> getCardsList() {
        return cardsList;
    }

    public void setCardsList(List<Cards> cardsList) {
        this.cardsList = cardsList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", landLord=" + landLord +
                ", cardsList=" + cardsList +
                ", chooseCards=" + chooseCards +
                '}';
    }
}
