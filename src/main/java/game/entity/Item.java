package game.entity;

public abstract class Item {
        private String name;
        private int price;

        public Item(int price) {
            this.price = price;
        }

        public int getPrice() {
            return price;
        }

}
