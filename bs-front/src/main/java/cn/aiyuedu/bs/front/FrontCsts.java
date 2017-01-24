package cn.aiyuedu.bs.front;

/**
 * 前台常量
 *
 * @author Scott
 */
public class FrontCsts {
    public static enum Rank1Type {
        人气(1),//人气
        飙升(2),//飙升
        新作(3),//新作
        搜索(4);//搜索
        Integer val;

        Rank1Type(int val) {
            this.val = val;
        }

        public Integer val() {
            return this.val;
        }
    }
    public static enum Rank2Type {
        bookBig(1),//大图书籍推荐
        bookSmall(2);//小图书籍推荐 h_book_small.html
        Integer val;

        Rank2Type(int val) {
            this.val = val;
        }

        public Integer val() {
            return this.val;
        }
    }
}
