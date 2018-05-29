import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Player {
    private int id;
    private String name;
    private  boolean landLord;
    private final List<Cards> cardsList; //不可变容器
    private final List<Cards> chooseCards; //不可变容器

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


    public List<Cards> getCardsList() {
        return cardsList;
    }


    public void addCards(List<Cards> cards) {
        this.cardsList.addAll(cards);
    }

    public void addChooseCards(List<Cards> cards) {
        this.chooseCards.addAll(cards);
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
