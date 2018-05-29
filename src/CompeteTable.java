import java.lang.reflect.Array;
import java.util.*;


public class CompeteTable implements GiveoutCardsRule{
    public static void main(String[] args) {
        CompeteTable test = new CompeteTable();
        test.competition();
    }

    /**
     * 比赛过程
     */

    public void competition(){

        //创建一副牌
        List<Cards> cards = initCardV2() ;

        System.out.println("欢迎来到婷婷斗地主游戏！");
        System.out.println("请创建您的角色名称：");

        //创建玩家
        Player player1 = initComPlayer(1);
        Player player2 = initComPlayer(2);
        Player player3 = initYourPlayer();

        System.out.println("恭喜您成功创建角色:"+ player3.getName() + "!系统已自动为您匹配另外两位玩家：" + player1.getName() +"和" + player2.getName());

        //随机抢地主
        System.out.println("现在开始随机抽选地主：");
        Player landLordPlayer;
        int numId = selectLandlord();               //numId用于存储本次抽到的地主ID，用于后面寻找对应的玩家
        if(findLandLord(player1, numId)){           //这里注意要使得landLordPlayer一定会被初始化，采用if else if else结构
            landLordPlayer = player1;
        }else if(findLandLord(player2, numId)){
            landLordPlayer = player2;
        }else{
            landLordPlayer = player3;
            player3.setLandLord(true);             //由于没有进入findLandLord函数，因此得补上设置操作
        }

        System.out.println("恭喜玩家" + landLordPlayer.getName() + "成为地主！农民们反抗起来吧！");

        //洗牌发牌
        System.out.println("游戏开始，下面开始洗牌。。。");
        List<Cards> allCards = washCard(cards);

        ArrayList<Cards> tableList = new ArrayList<>();  //tableList存储桌面上前一个人出的牌
        ArrayList<Cards> rubbishList = new ArrayList<>(); //rubblishList存储桌面之前已经出过过期的牌

        System.out.println("洗牌完成！下面开始发牌。。。");
        //自动发牌
        List<Cards> cardsP1 = allCards.subList(0,17);
        List<Cards> cardsP2 = allCards.subList(17,34);
        List<Cards> cardsP3 = allCards.subList(34,51);
        List<Cards> landLordP4 = allCards.subList(51,54);

        //自动整牌
        player1.setCardsList(orderCards(cardsP1));
        player2.setCardsList(orderCards(cardsP2));     //此处使用的setCardsv2方法是把list型的数据传入queue型的数据  queue.addAll(List)
        player3.setCardsList(orderCards(cardsP3));

        landLordPlayer.getCardsList().addAll(orderCards(landLordP4));  //地主多发三张牌
        orderCards(landLordPlayer.getCardsList());

//        List<Cards> temp = new ArrayList<Cards>(landLordPlayer.getCardQueue());         //使用List temp = new ArrayList (queue)实现queue转化成list
//        orderCards(temp);         //上面的排序使得地主的牌分成两部分排序，所以需要再次对所有牌进行重新排序
//        landLordPlayer.getCardQueue().clear();    //注意需要清空原先队列的数据，然后重新添加排序后的数据
//        landLordPlayer.getCardQueue().addAll(temp);


        System.out.println("发牌完成，您的牌为：");
        for(Cards ca : player3.getCardsList()){
            System.out.println(ca.getSuit() + ca.getPoint());
        }



        //出牌，地主先出牌，其余按照Id顺序
        System.out.println("请地主先出牌：");





    }

    /**
     * 遍历牌的花色和点数来创建牌
     */
    public List<Cards> initCardV2() {
        List<Cards> cards = new ArrayList<>(54);
        for (Cards.Suit suit : Cards.Suit.values()) {
            if (suit == Cards.Suit.Null) {
                continue;
            }
            for (Cards.Point point : Cards.Point.values()) {
                if(point == Cards.Point.Queen || point == Cards.Point.King){
                    continue;
                }
                cards.add(new Cards(suit,point));
            }
        }
        cards.add(new Cards(Cards.Suit.Null, Cards.Point.King));
        cards.add(new Cards(Cards.Suit.Null, Cards.Point.Queen));

        return cards;
    }

    /**
     * 创建你的玩家
     */
    public Player initYourPlayer(){
        System.out.println("请输入您想创建的角色姓名");
        Scanner input = new Scanner(System.in);
        String yourName = input.next();
        return new Player(3,yourName);
    }

    /**
     * 创建电脑玩家
     */
    public Player initComPlayer(int index){
        String var = "战神";
        return new Player(index,var+index);
    }

    /**
     * 随机抢地主
     */
    public int selectLandlord(){
        Random r = new Random();
        int num = r.nextInt(3);   //随机生成[0,3)之间的整数，包括0，1，2
        return num;
    }

    /**
     * 找到地主
     */
    public boolean findLandLord(Player player, int numId){
        if (player.getId() == numId + 1){
            player.setLandLord(true);
            return true;
        }
        return false;
    }

    /**
     *洗牌方法
     */
    public List<Cards> washCard(List<Cards> interList) {
        Collections.shuffle(interList);
        return interList;
    }

    /**
     * 自动整牌方法
     * 重写比较方法，使其按照牌的value值排序
     */
    public List<Cards> orderCards(List<Cards> myCards){
        Collections.sort(myCards, new Comparator<Cards>() {
            @Override
            public int compare(Cards o1, Cards o2) {
                return o1.getValue() - o2.getValue();
            }
        });
        return myCards;
    }

    /**
     * 发牌方法
     */

    public int  whetherGive(Player player){
        List<Cards> chooseCards = player.getChooseCards();
        orderCards(chooseCards);                 //一切判断的基础是排序
        if(chooseCards.size() == 0){
            return 0;
        }else if(chooseCards.size() == 1){        //单张牌为1
            return 1;
        }else if(chooseCards.size() == 2){
            if(isDoubleCards(chooseCards) != 0){    //一对牌为2
                return isDoubleCards(chooseCards);
            }else{
                return isKingBomb(chooseCards);
            }
        }else if(chooseCards.size() == 3){       //三张不带为4
            return isThreeCards(chooseCards);
        }else if(chooseCards.size() == 4){          //炸弹为5
            return isBomb(chooseCards);
        }else if(chooseCards.size() == 5){     //5张牌有两种情况：三带一对； 顺子
            if(isThreeTwo(chooseCards)!= 0){
                return isThreeTwo(chooseCards);
            }else {
                return isShunzi(chooseCards);
            }
        }else if(chooseCards.size() == 6){     //6张牌有三种情况： 连对； 飞机； 顺子
            if(isLiandui(chooseCards) != 0){
                return isLiandui(chooseCards);
            }
            if(isShunzi(chooseCards) != 0) {
                return isShunzi(chooseCards);
            }
            return isPlane(chooseCards);
        }else if(chooseCards.size() == 8){
            if(isShunzi(chooseCards) != 0){
                return isShunzi(chooseCards);
            }
            return isSmallPlane(chooseCards);   //如果不是顺子，则返回是否为小飞机的函数值，保证该if判断总会有返回值
        }else if(chooseCards.size() == 10){
            if(isShunzi(chooseCards) != 0){
                return isShunzi(chooseCards);
            }else {
                return isBigPlane(chooseCards);
            }
        }else {
            return isShunzi(chooseCards);    //其他张数都只需判断是否为顺子
        }
    }




    /**
     * 判断是不是飞机主体
     * @param partCards
     * @return
     */
    public int  isPlane(List<Cards> partCards){
        if(partCards.size() == 6 && isThreeCards(partCards.subList(0,3)) != 0 && isThreeCards(partCards.subList(3,6)) != 0 && partCards.get(2).getValue() == partCards.get(3).getValue()){
            return 9;
        }else {
            return 0;
        }
    }

    /**
     * 判断是不是顺子
     * @param chooseCards
     * @return                        这里为了处理方便，禁止出A234开头的顺子，并且最大的顺子结尾处应该是A
     */
    public int isShunzi(List<Cards> chooseCards){
        int i = 0;
        for(;i < chooseCards.size();i++){
            if(chooseCards.get(i).getValue() != (chooseCards.get(i + 1).getValue() - 1)){
                break;
            }
        }
        if(i == chooseCards.size() && chooseCards.get(chooseCards.size() - 1).getValue() < 13){
            return 8;
        }else {
            return 0;
        }
    }

    /**
     * 判断是否是连对
     * @param chooseCards
     * @return                           三个连对的情况，其中排除KKAA22 ，同时需要允许出AA2233,223344
     */
    public int isLiandui(List<Cards> chooseCards){
        if(chooseCards.get(0).getValue() == chooseCards.get(1).getValue() && chooseCards.get(1).getValue() == (chooseCards.get(2).getValue() + 1) && chooseCards.get(2).getValue() == chooseCards.get(3).getValue() && chooseCards.get(3).getValue() == (chooseCards.get(4).getValue() + 1) && chooseCards.get(4).getValue() == chooseCards.get(5).getValue() && chooseCards.get(4).getValue() < 13){
            return 7;
        }else if(chooseCards.get(0).getValue() == 1 && chooseCards.get(1).getValue() == 1 && chooseCards.get(2).getValue() == 12 && chooseCards.get(3).getValue() == 12 && chooseCards.get(4).getValue() == 13 && chooseCards.get(5).getValue() == 13){
            return 7;
        }else if(chooseCards.get(0).getValue() == 1 && chooseCards.get(1).getValue() == 1 && chooseCards.get(2).getValue() == 2 && chooseCards.get(3).getValue() == 2 && chooseCards.get(4).getValue() == 13 && chooseCards.get(5).getValue() == 13){
            return 7;
        }else {
            return 0;
        }
    }

    /**
     * 判断是否为三带一对
     * @param chooseCards
     * @return
     */
    public int isThreeTwo(List<Cards> chooseCards){
        if(chooseCards.get(0).getPoint().equals(chooseCards.get(1).getPoint()) && chooseCards.get(1).getPoint().equals(chooseCards.get(2).getPoint()) && chooseCards.get(3).getPoint().equals(chooseCards.get(4).getPoint())){
            return 6;
        }else if(chooseCards.get(0).getPoint().equals(chooseCards.get(1).getPoint()) && chooseCards.get(2).getPoint().equals(chooseCards.get(3).getPoint()) && chooseCards.get(3).getPoint().equals(chooseCards.get(4).getPoint())){
            return 6;
        }else {
            return 0;
        }
    }

    /**
     * 判断是否为小飞机，带两张单牌                   该方法只能判断8张牌是否为小飞机，不包括为顺子的情况，默认只能为小飞机或者不符合规则的牌，因此需经过两次判断
     * @param chooseCards
     * @return
     */
    public int isSmallPlane(List<Cards> chooseCards){
        if(chooseCards.size() == 8 && chooseCards.get(0).getValue() == chooseCards.get(1).getValue()){    //判断 AAA BBB D F 情况
            if(isPlane(chooseCards.subList(0,6)) != 0 && chooseCards.get(6).getValue() != chooseCards.get(7).getValue()){
                return 10;
            }
            return 0;
        }                                                                                                //判断 A B DDD EEE 情况
        if(chooseCards.size() == 8 && chooseCards.get(0).getValue() != chooseCards.get(1).getValue() && chooseCards.get(1).getValue() != chooseCards.get(2).getValue()){
            if(isPlane(chooseCards.subList(2,8)) != 0 ){
                return 10;
            }
            return 0;
        }                                                                                                //判断 A DDD EEE F
        if(chooseCards.get(0).getValue() != chooseCards.get(1).getValue() && chooseCards.get(1).getValue() == chooseCards.get(2).getValue()){
            if(isPlane(chooseCards.subList(1,7)) != 0){
                return 10;
            }
            return 0;
        }
        return 0;
    }

    /**
     * 判断牌是否为一对
     * @param chooseCards
     * @return
     */
    public int isDoubleCards(List<Cards> chooseCards){
        if(chooseCards.size() == 2 && chooseCards.get(0).getValue() == chooseCards.get(1).getValue()){
            return 2;
        }else {
            return 0;
        }
    }

    /**
     * 判断是否为王炸
     * @param chooseCards
     * @return
     */
    public int isKingBomb(List<Cards> chooseCards){
        if(chooseCards.size() == 2 && chooseCards.get(0).getValue() == 14 && chooseCards.get(1).getValue() == 15){
            return 3;
        }else if(chooseCards.size() == 2 && chooseCards.get(0).getValue() == 15 && chooseCards.get(1).getValue() == 14){
            return 3;
        }else {
            return 0;
        }
    }

    /**
     * 判断是否为三张不带
     * @param chooseCards
     * @return
     */
    public int isThreeCards(List<Cards> chooseCards){
        if(chooseCards.size() == 3 && chooseCards.get(0).getValue() == chooseCards.get(1).getValue() && chooseCards.get(1).getValue() == chooseCards.get(2).getValue()){
            return 4;
        }else {
            return 0;
        }
    }

    /**
     * 判断是否为炸弹
     * @param chooseCards
     * @return
     */
    public int isBomb(List<Cards> chooseCards){
        if(chooseCards.size() == 4 && chooseCards.get(0).getValue() == chooseCards.get(1).getValue() && chooseCards.get(1).getValue() == chooseCards.get(2).getValue() && chooseCards.get(2).getValue() == chooseCards.get(3).getValue()){
            return 5;
        }else {
            return 0;
        }
    }

    /**
     * 判断是否为大飞机
     * @param chooseCards
     * @return
     */
    public int isBigPlane(List<Cards> chooseCards){
        if(chooseCards.size() == 10 && chooseCards.get(1).getValue() == chooseCards.get(2).getValue()){ //判断 AAA BBB CC DD
            if(isPlane(chooseCards.subList(0,6)) != 0 && isDoubleCards(chooseCards.subList(6,8)) != 0 && isDoubleCards(chooseCards.subList(8,10)) != 0){
                return 11;
            }
        }
        if(chooseCards.size() == 10 && chooseCards.get(3).getValue() == chooseCards.get(4).getValue()){ //判断 AA BBB CCC DD
            if(isDoubleCards(chooseCards.subList(0,2)) != 0 && isPlane(chooseCards.subList(2,8)) != 0 && isDoubleCards(chooseCards.subList(8,10)) != 0){
                return 11;
            }
        }                                                                                               //判断 AA BB CCC DDD
        if(isDoubleCards(chooseCards.subList(0,2)) != 0 && isDoubleCards(chooseCards.subList(2,4)) != 0 && isPlane(chooseCards.subList(4,10)) != 0){
            return 11;
        }
        return 0;
    }

    /**
     * 玩家出牌方法
     * @param player
     */
    public void playerGive(Player player){

    }


}
