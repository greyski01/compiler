import java.util.ArrayList;
import java.util.Collections;

public class Token
{
    static String[] keyWord={"private","protected","public","abstract","class","extends","final","implements",
            "interface","native","new","static","strictfp","break","continue","return","do","while","if","else","for",
            "instanceof","switch","case","default","boolean","byte","char","double","float","int","long","short",
            "String","null","true","false","void","this","goto","main","then"};

    static String[] operation={"+","-","*","/","&","==","!=",">","<","=",">=","<=",".","(",")","{","}","[","]",",",";"};

    static String[] operationName={"plus","minus", "mul","div","and","eq","ne","gt","lt","assign","ge","le","point", "LPAREN",
            "RPAREN", "LBRACE", "RBRACE","LBRACK","RBRACK","COMMA","SEMICOLON"};

    public String tokenName;
    public String value;
    public int errorLine;
    public int errorPlace;
    public int errorInfo;

    static ArrayList<String> operations=null;
    static ArrayList<String> keyWords=null;

    public Token(){
        keyWords=new ArrayList<>();
        operations=new ArrayList<>();
        Collections.addAll(keyWords,keyWord);
        Collections.addAll(operations,operation);
    }

    public void setTokenName(String val,String tn){
        if (tn!=null){
            this.tokenName=tn;
        }
        else {
        if (keyWords.contains(val))
            this.tokenName="TK_"+val.toUpperCase();
        else if (operations.contains(val))
        {
            this.tokenName="TK_"+operationName[operations.indexOf(val)].toUpperCase();
        }
        else if(this.tokenName!=null&&this.tokenName.equals("digit")) {
        }
        else
            this.tokenName="ID";
        }
    }


}
