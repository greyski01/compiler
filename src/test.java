import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class test
{
    Scanner scan=null;
    File f=new File("src/temc.txt");

    public test(){
        try {
            scan=new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String str;
        while (scan.hasNextLine()){
            str=scan.nextLine();//next获取的值是用空格隔起来的词语。
            System.out.println(str);
    }

}

    public static void main(String[] args) {
        test t=new test();
    }
    }

