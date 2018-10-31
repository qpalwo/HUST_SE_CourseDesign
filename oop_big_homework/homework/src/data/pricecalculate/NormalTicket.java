package data.pricecalculate;

public class NormalTicket implements Calculator {
    @Override
    public float calculate(float length) {
        if (length <= 9) {
            return 2;
        } else if (length <= 14) {
            return 3;
        } else {
            length -= 14;
            int temp = 2;
            float price = 3;
            while (length - temp >= 0) {
                length -= temp;
                price++;
                temp += 2;
            }
            price++;
            return price;
        }
    }
}
