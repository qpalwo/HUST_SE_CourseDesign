package data;

import data.bean.Path;

import java.util.List;

public class Test {
    public static void main(String[] args){
        DataModel dataModel = new DataModel();
        List<String> temp = dataModel.getStationsOfLine("3号线", "宏图大道");

        Path p = dataModel.getPath("天河机场", "光谷广场");
        //Path p = dataModel.getPath("市民之家", "楚河汉街");
        //Path p = dataModel.getPath("江城大道", "楚河汉街");
        String s = p.toString();
        float b = p.price(Path.CALCU_NORMAL);
        int a = 0;
    }
}
