import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Cards {

        private final Suit suit;

        private final Point point;


        public Cards(Suit suit,Point point) {
            this.point = point;
            this.suit = suit;
        }

        /**
         * 制定牌的花色
         * 牌的花色是固定的，所以最好使用{enum}而不是{String}类型
         */
        public enum Suit {

            /**
             * 红桃
             */
            Heart("红桃"),
            /**
             * 梅花
             */
            Plum("梅花"),
            /**
             * 黑桃
             */
            Spade("黑桃"),
            /**
             * 方片
             */
            Block("方片"),

            /**
             * 大小王没有花色，所以制定一个null类型
             */
            Null("");

            Suit(String val) {
                this.val = val;
            }

            private String val;

            /**
             * 获取中文类型的花色
             * @return
             */
            private String getVal (){
                return val;
            }

        }

        /**
         * 制定牌的点数
         */
        public enum Point{

            A("A",12),
            Two("2",13),
            Three("3",1),
            Four("4",2),
            Five("5",3),
            Six("6",4),
            Seven("7",5),
            Eight("8",6),
            Nine("9",7),
            Ten("10",8),
            J("J",9),
            Q("Q",10),
            K("K",11),
            Queen("Queen",15),
            King("King",14);

            String val;
            int value;
            Point(String val,int value) {
                this.val = val;
                this.value = value;
            }
        }

        public String getSuit() {
            return suit.getVal();
        }

        public String getPoint() {
            return point.val;
        }

        public int getValue(){
            return point.value;
        }



    @Override
    public String toString() {
        return "com.yyt.card.model.Card{" +
                "suit='" + suit + '\'' +
                ", point=" + point +
                '}';
    }

    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>(10);
        list.add(1);
        list.add(2);
        list.add(4);
        list.add(3);
        list.add(6);


        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2;
            }
        });

        List<Integer> tempList = list.subList(1,4);

        tempList.addAll(new ArrayList<>(1));

        System.out.println(tempList.get(2));
    }

}
