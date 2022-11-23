import java.io.File;
import java.io.FileNotFoundException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class compile
{
   ////////////////////////////////1.定义关键字、运算符、界符/////////////////////////
    //关键字
    static String[] keyWord={"private","protected","public","abstract","class","extends","final","implements",
            "interface","native","new","static","strictfp","break","continue","return","do","while","if","else","for",
            "instanceof","switch","case","default","boolean","byte","char","double","float","int","long","short",
            "String","null","true","false","void","this","goto","main","then"};
    //运算符
    static String[] operation={"+","-","*","/","&","==","!=",">","<","=",">=","<=",".","(",")","{","}","[","]",",",";"};
    //界符
    static ArrayList<String> keyWords=null;
    static ArrayList<String> operations=null;

    private static String tokenName;
    private static int eLine=0;
    private static int ePlace=0;
    private static ArrayList<Token> lt=null;

    public compile(){
        init();
    }
    //////////////////////////2.定义初始化函数//////////////////////////////////////////////
    public void init(){
        keyWords=new ArrayList<>();
        operations=new ArrayList<>();
        lt=new ArrayList<>();
        Collections.addAll(keyWords,keyWord);
        Collections.addAll(operations,operation);
    }

    public void getNextToken(){
        Scanner scan=null;
        try {
            File f=new File("src/temc.txt");
            scan=new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String str,longStr;
        while (scan.hasNextLine()){
            longStr=scan.nextLine();//next获取的值是用空格隔起来的词语。
            eLine++;
            ePlace=0;
            while (true){
                int start=splitLineStart(longStr);
//                System.out.println("start"+start);
                int end=splitLineEnd(start,longStr);
//                System.out.println("end"+end);
                if (end==0)
                    break;
                str=longStr.substring(start,end);
//                System.out.println("length"+longStr.length());
                analyse(str);
                if (end==longStr.length())
                    break;
                longStr=longStr.substring(end+1);
//                System.out.println("longstr"+longStr);
            }
//            System.out.println("braek");
        }
    }

    private int splitLineStart(String str){//找到第一个不为空格的索引值
        int i = 0;
        for (; i < str.length(); i++) {
            if (str.charAt(i)!=' ')
                break;
        }
        return i;//return的是索引值
    }

    private int splitLineEnd(int start,String str){//找到第一个为空格的索引值
        int i = start;
        for (; i < str.length(); i++) {
            if (str.charAt(i)==' ')
                break;
        }
        return i;//return的是索引值
    }

    public void analyse(String str){
        //首先对该词语进行分析
        while (true) {
            Token token=new Token();
            tokenName=null;
            int placeNum=cut(str);
//            if (keyWords.contains(str.substring(0,placeNum)))
//                tokenName="keyWord";
            token.setTokenName(str.substring(0,placeNum),tokenName);
            token.errorLine=eLine;
            token.errorPlace=ePlace;
            token.value=str.substring(0,placeNum);
            lt.add(token);
//            System.out.println("placeNum"+placeNum);
            ePlace+=placeNum+1;
            if (str.length()==placeNum)
                break;
            str=str.substring(placeNum);//例如placeNum是3，那么substring就会切掉最前面的3个字符
        }
//        if(Character.isDigit(str.charAt(0))){//词语的第一个字符是数字的情况
//            tokenName=ifDigit(str);
//        }
//        if (Character.isLetter(str.charAt(0))){//词语的第一个字符是字母的情况
//            tokenName=ifLetter(str);
//        }
    }

    public int cut(String str){
        if (operations.contains(String.valueOf(str.charAt(0)))){//分割前先判断首字母是不是特殊字符operation
            if (str.length()!=1){
            if (operations.contains(String.valueOf(str.substring(0,2))))//前两个字符都为特殊字符
                return 2;
            else
                return 1;
            }
            else
                return 1;
        }
        else {//然后对数字或标志符进行分割
            if (Character.isLetter(str.charAt(0))){//首字符为字母的情况下，只要截取完整的字符串就可以
                int i=0;
                for (; i < str.length(); i++) {
                    if (!(Character.isLetterOrDigit(str.charAt(i))||str.charAt(i)=='_'))//只要某一字符不为数字或字母或_就break
                        break;
                }
//                if (i==str.length())
//                    i--;
                return i;
            }

            if (Character.isDigit(str.charAt(0))){//只取整数就可以，因为点的分隔而确定的小数是句法分析器的作用
                //所以就只用判断是整数还是标识符
                tokenName="digit";
                Boolean flag=true;//用flag来确定是标志符还是纯数字
                int i = 1;
                for (; i < str.length(); i++) {
                    if (flag) {//是纯数字的情况
                        if (Character.isLetter(str.charAt(i)))
                            flag = false;
                        else if (Character.isDigit(str.charAt(i))){
                        }
                        else break;
                    }
                    else{
                        tokenName="ID";
                        if (!(Character.isLetterOrDigit(str.charAt(i))||str.charAt(i)=='_'))//只要某一字符不为数字或字母或_就break
                            break;
                    }
                }
//                if (i==str.length())
//                    i--;
                return i;
            }
        }
        return 1;
    }

    public Token[] getTokens(){
        compile c=new compile();
        c.getNextToken();
        Token[] tokens=new Token[lt.size()+1];
        for (int i = 0; i < lt.size(); i++) {
            tokens[i]=lt.get(i);
        }
        return tokens;
    }


    public static void main(String[] args) {
        compile c=new compile();
        c.getNextToken();
        for (int i = 0; i < lt.size(); i++) {
            System.out.print(i+"      |");
            System.out.print(lt.get(i).tokenName+"  |  ");
            System.out.println(lt.get(i).value+"    |  ");
//            System.out.print(lt.get(i).errorLine+"    |  ");
//            System.out.println(lt.get(i).errorPlace);
    }
}
}