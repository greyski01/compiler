import java.awt.event.HierarchyBoundsAdapter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.lang.Error;

public class Parser
{
    public Token[] tokens=null;
    public int cursor=0;
    public Token currentToken;
    public List<astNode> actual_parameter_nodes=null;
    public astNode theNode;
    public String current_function_name;

    public Parser(Token[] theTokens){
        tokens=theTokens;
        currentToken=theTokens[0];
        theNode=new astNode();
    }

    private void to_next_token(){
        cursor++;
        currentToken=tokens[cursor];
    }

    private void errorReport()   {
        System.out.format("\33[31;4merrorType--------->%s%n", currentToken.tokenName);
        System.out.format("\33[31;4merrorValue--------->%s%n", currentToken.value);
        System.out.format("\33[31;4merrorLine--------->%d%n", currentToken.errorLine);
        System.out.format("\33[31;4merrorLine--------->%d%n", currentToken.errorPlace);
        System.out.format("\33[31;4merrorLine--------->%d%n", currentToken.errorPlace);
//        System.out.println("error---->"+currentToken.tokenName);
//        System.out.println("error---->"+currentToken.errorLine);
//        System.out.println("error---->"+currentToken.errorPlace);
        System.exit(-1);
    }

//    private void match(String tokenName,String methodName)   {
//        if (currentToken.tokenName.equals(tokenName)) {
//                //根据方法名调用方法
//                Class parser=Class.forName("Parser");
//                Method method=parser.getMethod(methodName);
//                method.invoke(parser);
//        }
//        else {
//            errorReport();
//        }
//    }

    private void eat(String tokenName){
        if (currentToken.tokenName.equals(tokenName)){
            to_next_token();
        }
        else
            errorReport();
    }

    private void program(){
//        System.out.println("program");
        //program-->function_definition*
        //p-->fds
        function_definitions();
    }

    private void function_definitions(){
//        System.out.println("function_definitions");
        //function_definition-->function_definition *
        while (tokens[cursor]!=null) {
                function_definition();
        }
    }

    private void function_definition(){
//        System.out.println("function_definition");
        // function_definition-->type_specification identifier
        // "(" formal_parameters ")" block
        //fd-->ts id (fps) block
        if (currentToken.tokenName.equals("TK_INT"))
            type_specification();
        else
            errorReport();
        if (currentToken.tokenName.equals("ID"))
        {
            to_next_token();
            if (currentToken.tokenName.equals("TK_LPAREN")){
            to_next_token();
            if (!currentToken.tokenName.equals("TK_RPAREN"))
                formal_parameters();
            eat("TK_RPAREN");
        }
        else
            errorReport();
        if (currentToken.tokenName.equals("TK_LBRACE"))
        {
            block();
        }
        else
                errorReport();
        }
        else
            errorReport();
    }

    //type_specification = int
    private void type_specification(){
//        System.out.println("type_specification");
        eat("TK_INT");
    }

    private void formal_parameters()   {
//        System.out.println("formal_parameters");
        // formal_parameters-->formal_parameter formal_parameters
        //fps-->fp fps
        formal_parameter();
        while (!currentToken.tokenName.equals("TK_RPAREN")){
            eat("TK_COMMA");
            formal_parameter();
        }
    }

    private void formal_parameter()   {
//        System.out.println("formal_parameter");
        if (currentToken.tokenName.equals("TK_INT"))
            type_specification();
        else
            errorReport();
        eat("ID");
    }

    private void block()   {
//        System.out.println("block");
        eat("TK_LBRACE");
        compound_statement();
//        System.out.println(currentToken.tokenName);
//        System.out.println(cursor);
        eat("TK_RBRACE");
    }

    //compound_statement = (variable_declaration | statement)*
    private void compound_statement()   {
//        System.out.println("compound_statement");
        while (!currentToken.tokenName.equals("TK_RBRACE")){
            if (currentToken.tokenName.equals("TK_INT")){
                variable_declaration();
            }
            else
                statement();
        }
    }

    //statement = expression-statement
    //    | "return" expression-statement
    //    | block
    //    | "if" "(" expression ")" statement ("else" statement)?
    private void statement()   {
//        System.out.println("statement");
        //"return" expression-statement
        if (currentToken.tokenName.equals("TK_RETURN")){
            to_next_token();
            expression_statement();
        }
        //block
        else if (currentToken.tokenName.equals("TK_LBRACE"))
            block();
            //"if" "(" expression ")" statement ("else" statement)?
        else if (currentToken.tokenName.equals("TK_IF")){
            to_next_token();
            if (currentToken.tokenName.equals("TK_LPAREN")) {
                to_next_token();
                expression();
                if (currentToken.tokenName.equals("TK_RPAREN")){
                    to_next_token();
                    compound_statement();
                    if (currentToken.tokenName.equals("TK_ELSE"))
                    {
                        to_next_token();
                        compound_statement();
                    }
                }
                else
                    errorReport();
            }
            else
                errorReport();
        }
        else if (currentToken.tokenName.equals("TK_WHILE"))
        {
            to_next_token();
            if (currentToken.tokenName.equals("TK_LBRACE"))
            {to_next_token(); expression();}
            else
                errorReport();
            if (currentToken.tokenName.equals("TK_RBRACE"))
                eat("TK_RBRACE");
            compound_statement();
        }
    }

    //variable_declaration = type_specification (indentifier ("=" expr)?
    // ("," indentifier ("=" expr)?)*)? ";"
    private void variable_declaration()   {
//        System.out.println("variable_declaration");
        type_specification();
        while (!currentToken.tokenName.equals("TK_SEMICOLON")){
            if (currentToken.tokenName.equals("ID")){
                to_next_token();
                if (currentToken.tokenName.equals("TK_ASSIGN"))
                {to_next_token(); expression();}
                eat("TK_COMMA");
            }
            else
                errorReport();
        }
        eat("TK_SEMICOLON");
    }


    // # primary := "(" expression ")" | identifier args? | num | identifier "[" expression "]"
    //    # args := "(" (expression ("," expression)*)? ")"
    private void primary()   {
//        System.out.println("primary");
        //是一个或,(expr)|ident args?|num
        //分别审查(expr)
        if (currentToken.tokenName.equals("TK_LPAREN")){
            to_next_token();
            expression();
            eat("TK_RPAREN");
        }
        //ident
        else if (currentToken.tokenName.equals("ID")) {
            to_next_token();
            //function call
            //id (args)
            if (currentToken.tokenName.equals("TK_LPAREN")){
                to_next_token();
                if (!currentToken.tokenName.equals("TK_RPAREN"))
                {//即函数的参数列表不为空
                    expression();
                }
                while (currentToken.tokenName.equals("TK_COMMA"))
                {
                    eat("TK_COMMA");
                    expression();
                }
                eat("TK_RPAREN");
            }
            //id [(expression)?]
            else if (currentToken.tokenName.equals("TK_LBRACK")){
                to_next_token();
                if (!currentToken.tokenName.equals("TK_RBRACK"))
                    expression();
                eat("TK_RBRACK");
            }
        }
        //num
        else if (currentToken.tokenName.equals("digit"))
            to_next_token();

    }

    //# expression := logic ("=" expression)?
    private void expression()   {
//        System.out.println("expression");
        logic();
        if (currentToken.tokenName.equals("TK_ASSIGN")){
            to_next_token();
            expression();
        }
    }

    //expression-statement = expression? ";"
    private void expression_statement()   {
//        System.out.println("expression_statement");
        if (currentToken.tokenName.equals("TK_SEMICOLON"))
            to_next_token();
        else {
        expression();
        eat("TK_SEMICOLON");
        }
    }

    //# unary := ("+" | "-" | "!") unary
    //    #        | primary
    private void unary()   {
//        System.out.println("unary");
        if (currentToken.tokenName.equals("TK_PLUS")){
            to_next_token();
            unary();
        }
        else if (currentToken.tokenName.equals("TK_MINUS")){
            to_next_token();
            unary();
        }
        else if (currentToken.tokenName.equals("TK_NOT")) {
            to_next_token();
            unary();
        }
        else
            primary();

    }

   // mul_div := unary ("*" unary | "/" unary)*
    private void mul_div()   {
//        System.out.println("mul_div");
        unary();
        label:
        while (true){
            switch (currentToken.tokenName) {
                case "TK_MUL":
                case "TK_DIV":
                    to_next_token();
                    unary();
                    break;
                default:
                    break label;
            }
        }
    }

   // # add-sub := mul_div ("+" mul_div | "-" mul_div)*
    private void add_sub()   {
//        System.out.println("add_sub");
        mul_div();
        label:
        while (true){
            switch (currentToken.tokenName) {
                case "TK_PLUS":
                case "TK_MINUS":
                    to_next_token();
                    mul_div();
                    break;
                default:
                    break label;
            }
        }
    }

    //# relational := add_sub ("<" add_sub | "<=" add_sub | ">" add_sub | ">=" add_sub)*
    private void relational()   {
//        System.out.println("relational");
        add_sub();
        label:
        while (true){
            switch (currentToken.tokenName) {
                case "TK_LT":
                case "TK_LE":
                case "TK_GT":
                case "TK_GE":
                    to_next_token();
                    add_sub();
                    break;
                default:
                    break label;
            }
        }
    }

    //# equality := relational ("==" relational | "! =" relational)*
    private void equality()   {
//        System.out.println("equality");
        relational();
        label:
        while (true){
            switch (currentToken.tokenName) {
                case "TK_EQ":
                case "TK_NE":
                    to_next_token();
                    relational();
                    break;
                default:
                    break label;
            }
        }
    }

    //    # logic := equality ("&&" equality | "||" equality)*
    private void logic()   {
//        System.out.println("logic");
        equality();
        label:
        while (true){
            switch (currentToken.tokenName) {
                case "TK_AND":
                case "TK_OR":
                    to_next_token();
                    equality();
                    break;
                default:
                    break label;
            }
        }
    }

    public static void main(String[] args) {
        compile c=new compile();
        Token[] tokens=c.getTokens();
        Parser parser=new Parser(tokens);
        parser.program();
    }
}
