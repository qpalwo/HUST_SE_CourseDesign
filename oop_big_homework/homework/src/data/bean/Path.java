package data.bean;

import data.pricecalculate.Calculator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Path {
    public static final String CALCU_DAILY = "data.pricecalculate.DailyTicket";
    public static final String CALCU_WUHAN = "data.pricecalculate.WuHanCard";
    public static final String CALCU_NORMAL = "data.pricecalculate.NormalTicket";

    private float length;

    private int number;

    private Map<String, LinkedList<String>> stationOfLine;

    private Map<String, String> numberOfLine;

    private String currentLine;

    public Path(){
        stationOfLine = new LinkedHashMap<>();
        numberOfLine = new HashMap<>();
        number = 0;
    }

    public void addLine(String lineName){
        currentLine = number++ + "";
        numberOfLine.put(currentLine, lineName);
        stationOfLine.put(currentLine, new LinkedList<>());
    }

    public void addStation(String station, float distance){
        stationOfLine.get(currentLine).add(station);
        length += distance;
    }

    public float price(Calculator calculator){
        return calculator.calculate(length);
    }

    public float price(String type){
        try {
            Class calculator = Class.forName(type);
            Method calcu = calculator.getMethod("calculate", float.class);
            Object obj = calculator.getConstructor().newInstance();
            return (float)calcu.invoke(obj, length);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        boolean isNext = true;
        for (Map.Entry<String, LinkedList<String>> entry : stationOfLine.entrySet()){
            if(isNext){
                isNext = false;
                stringBuffer
                        .append(numberOfLine.get(entry.getKey()))
                        .append(" [")
                        .append(entry.getValue().getFirst())
                        .append("->")
                        .append(entry.getValue().getLast())
                        .append("]")
                        .append("\n");
                continue;
            }
            stringBuffer
                    .append("换乘")
                    .append(numberOfLine.get(entry.getKey()))
                    .append(" [")
                    .append(entry.getValue().getFirst())
                    .append("->")
                    .append(entry.getValue().getLast())
                    .append("]")
                    .append("\n");
        }
        return stringBuffer.toString();
    }
}
