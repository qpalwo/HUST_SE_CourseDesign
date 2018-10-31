package data.pricecalculate;

public class WuHanCard implements Calculator {
    @Override
    public float calculate(float length) {
        return (float) (new NormalTicket().calculate(length) * 0.9);
    }
}
