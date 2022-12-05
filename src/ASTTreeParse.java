import java.awt.event.HierarchyBoundsAdapter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.lang.Error;
import java.util.Locale;

public class ASTTreeParse
{
    public Token[] tokens=null;
    public int cursor=0;
    public Token currentToken;
    public List<astNode> actual_parameter_nodes=null;
    public astNode theNode;
    public String current_function_name;

    public ASTTreeParse(Token[] theTokens){
        tokens=theTokens;
        currentToken=theTokens[0];
        theNode=new astNode();
        current_function_name="";
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

    private astNode program(){
        //program-->function_definition*
        //p-->fds
        return theNode.new programNode(function_definitions());
    }

    private List<astNode> function_definitions(){
//        System.out.println("function_definitions");
        //function_definition-->function_definition *
        List<astNode> function_definitions = null;
        while (tokens[cursor]!=null) {
            function_definitions.add(function_definition());
        }
        return function_definitions;
    }

    private astNode function_definition(){
//        System.out.println("function_definition");
        // function_definition-->type_specification identifier
        // "(" formal_parameters ")" block
        //fd-->ts id (fps) block
        astNode ts=null,bs=null;
        String functionName = null;
        List<astNode> formal_params=null;
        if (currentToken.tokenName.equals("TK_INT"))
            ts=type_specification();
        else
            errorReport();
        if (currentToken.tokenName.equals("ID"))
        {
            functionName=currentToken.value;
            to_next_token();
            if (currentToken.tokenName.equals("TK_LPAREN")){
                to_next_token();
                if (!currentToken.tokenName.equals("TK_RPAREN"))
                    formal_params=formal_parameters();
                eat("TK_RPAREN");
            }
            else
                errorReport();
            if (currentToken.tokenName.equals("TK_LBRACE"))
            {
                bs=block();
            }
            else
                errorReport();
        }
        else
            errorReport();
        current_function_name=functionName;
        return theNode.new FunctionDef_Node(ts,bs,functionName,formal_params);
    }

    //type_specification = int
    private astNode type_specification(){
//        System.out.println("type_specification");
        astNode ts=theNode.new Type_Node(currentToken);
        eat("TK_INT");
        return ts;
    }

    private List<astNode> formal_parameters()   {
//        System.out.println("formal_parameters");
        // formal_parameters-->formal_parameter formal_parameters
        //fps-->fp fps
        List<astNode> fps=null;
        fps.add(formal_parameter());
        while (!currentToken.tokenName.equals("TK_RPAREN")){
             if (currentToken.tokenName.equals("TK_COMMA"))
             eat("TK_COMMA");
            fps.add(formal_parameter());
        }
        return fps;
    }

    private astNode formal_parameter()   {
//        System.out.println("formal_parameter");
        astNode fp=null,ts=null;
        if (currentToken.tokenName.equals("TK_INT"))
            ts=type_specification();
        else
            errorReport();
        fp=theNode.new FormalParam_Node(ts,currentToken);
        eat("ID");
        return fp;
    }

    private astNode block()   {
//        System.out.println("block");
        eat("TK_LBRACE");
        astNode bn=compound_statement();
//        System.out.println(currentToken.tokenName);
//        System.out.println(cursor);
        eat("TK_RBRACE");
        return bn;
    }

    //compound_statement = (variable_declaration | statement)*
    private astNode compound_statement()   {
        List<List<astNode>> vds=null;
        List<astNode> statements=null;
//        System.out.println("compound_statement");
        while (!currentToken.tokenName.equals("TK_RBRACE")){
            if (currentToken.tokenName.equals("TK_INT")){
                vds.add(variable_declaration());
            }
            else
                statements.add(statement());
        }
        return theNode.new compound_Statement_Node(vds,statements);
    }

    //statement = expression-statement
    //    | "return" expression-statement
    //    | block
    //    | "if" "(" expression ")" statement ("else" statement)?
    private astNode statement()   {
//        System.out.println("statement");
        //"return" expression-statement
        if (currentToken.tokenName.equals("TK_RETURN")){
            to_next_token();
            expression_statement();
            return theNode.new Return_Node(expression_statement(),current_function_name);
        }
        //block
        else if (currentToken.tokenName.equals("TK_LBRACE"))
            return block();
            //"if" "(" expression ")" statement ("else" statement)?
        else if (currentToken.tokenName.equals("TK_IF")){
            to_next_token();
            astNode condition=null,thens=null,elses=null;
            if (currentToken.tokenName.equals("TK_LPAREN")) {
                to_next_token();
                condition=expression();
                if (currentToken.tokenName.equals("TK_RPAREN")){
                    to_next_token();
                    thens=compound_statement();
                    if (currentToken.tokenName.equals("TK_ELSE"))
                    {
                        to_next_token();
                        elses=compound_statement();
                    }
                }
            }
            return theNode.new If_Node(condition,thens,elses);
        }
        else if (currentToken.tokenName.equals("TK_WHILE"))
        {
            astNode condition=null,statement=null;
            to_next_token();
            if (currentToken.tokenName.equals("TK_LBRACE"))
            {to_next_token(); condition=expression();}
            if (currentToken.tokenName.equals("TK_RBRACE"))
                eat("TK_RBRACE");
            statement=compound_statement();
            return theNode.new While_Node(condition,statement);
        }
        return null;
    }

// # variable_declaration := type_specification identifier ("," indentifier)* ";"

    private List<astNode> variable_declaration()   {
//        System.out.println("variable_declaration");
        astNode ts=null;
        List<astNode> vds=null;
        ts=type_specification();
        while (!currentToken.tokenName.equals("TK_SEMICOLON")){
            if (currentToken.tokenName.equals("ID")){
                vds.add(theNode.new VarDecl_Node(ts,theNode.new Var_Node(currentToken)));
                to_next_token();
            }
            if (currentToken.tokenName.equals("TK_COMMA"))
                eat("TK_COMMA");
        }
        eat("TK_SEMICOLON");
        return vds;
    }


    // # primary := "(" expression ")" | identifier args? | num | identifier "[" expression "]"
    //    # args := "(" (expression ("," expression)*)? ")"
    private astNode primary()   {
//        System.out.println("primary");
        //是一个或,(expr)|ident args?|num
        //分别审查(expr)
        if (currentToken.tokenName.equals("TK_LPAREN")){
            to_next_token();
            astNode ex=expression();
            eat("TK_RPAREN");
            return ex;
        }
////        ident
//        else if (currentToken.tokenName.equals("ID")) {
//            to_next_token();
//            //function call
//            //id (args)
//            if (currentToken.tokenName.equals("TK_LPAREN")){
//                to_next_token();
//                if (!currentToken.tokenName.equals("TK_RPAREN"))
//                {//即函数的参数列表不为空
//                    expression();
//                }
//                while (currentToken.tokenName.equals("TK_COMMA"))
//                {
//                    eat("TK_COMMA");
//                    expression();
//                }
//                eat("TK_RPAREN");
//            }
//            //id [(expression)?]
//            else if (currentToken.tokenName.equals("TK_LBRACK")){
//                to_next_token();
//                if (!currentToken.tokenName.equals("TK_RBRACK"))
//                    expression();
//                eat("TK_RBRACK");
//            }
//        }
//        //num
//        else if (currentToken.tokenName.equals("digit"))
//            to_next_token();
        return null;
    }

    //# expression := logic ("=" expression)?
    private astNode expression()   {
//        System.out.println("expression");
        astNode left=null,right=null;
        Token tok=null;
        left=logic();
        if (currentToken.tokenName.equals("TK_ASSIGN")){
            tok=currentToken;
            to_next_token();
            right=expression();
        }
        return theNode.new BinaryOp_Node(tok,left,right);
    }

    //expression-statement = expression? ";"
    private astNode expression_statement()   {
        astNode ex=null;
//        System.out.println("expression_statement");
        if (currentToken.tokenName.equals("TK_SEMICOLON"))
            to_next_token();
        else {
            ex=expression();
            eat("TK_SEMICOLON");
        }
        return ex;
    }

    //# unary := ("+" | "-" | "!") unary
    //    #        | primary
    private astNode unary()   {
//        System.out.println("unary");
        astNode right=null;
        Token tok=null;
        switch (currentToken.tokenName) {
            case "TK_PLUS":
            case "TK_MINUS":
            case "TK_NOT":
                tok = currentToken;
                to_next_token();
                right = unary();
                return theNode.new UnaryOp_Node(tok, right);
            default:
                return primary();
        }

    }

    // mul_div := unary ("*" unary | "/" unary)*
    private astNode mul_div()   {
        astNode left=null,right=null;
        Token tok=null;
        List<astNode> us=null;
//        System.out.println("mul_div");
        left=unary();
        label:
        while (true){
            switch (currentToken.tokenName) {
                case "TK_MUL":
                case "TK_DIV":
                    tok=currentToken;
                    to_next_token();
                    right=unary();
                    us.add(theNode.new BinaryOp_Node(tok,left,right));
                    break;
                default:
                    break label;
            }
        }
        return theNode.new PartExpression_Node(us);
    }

    // # add-sub := mul_div ("+" mul_div | "-" mul_div)*
    private astNode add_sub()   {
        astNode left=null,right=null;
        Token tok=null;
        List<astNode> us=null;
//        System.out.println("mul_div");
        left=mul_div();
        label:
        while (true){
            switch (currentToken.tokenName) {
                case "TK_PLUS":
                case "TK_MINUS":
                    tok=currentToken;
                    to_next_token();
                    right=mul_div();
                    us.add(theNode.new BinaryOp_Node(tok,left,right));
                    break;
                default:
                    break label;
            }
        }
        return theNode.new PartExpression_Node(us);
    }

    //# relational := add_sub ("<" add_sub | "<=" add_sub | ">" add_sub | ">=" add_sub)*
    private astNode relational()   {
//        System.out.println("relational");
        astNode left=null,right=null;
        Token tok=null;
        List<astNode> us=null;
        left=add_sub();
        label:
        while (true){
            switch (currentToken.tokenName) {
                case "TK_LT":
                case "TK_LE":
                case "TK_GT":
                case "TK_GE":
                    tok=currentToken;
                    to_next_token();
                    right=add_sub();
                    us.add(theNode.new BinaryOp_Node(tok,left,right));
                    break;
                default:
                    break label;
            }
        }
        return theNode.new PartExpression_Node(us);
    }

    //# equality := relational ("==" relational | "! =" relational)*
    private astNode equality()   {
//        System.out.println("equality");
        astNode left=null,right=null;
        Token tok=null;
        List<astNode> us=null;
        left=relational();
        label:
        while (true){
            switch (currentToken.tokenName) {
                case "TK_EQ":
                case "TK_NE":
                    tok=currentToken;
                    to_next_token();
                    right=relational();
                    us.add(theNode.new BinaryOp_Node(tok,left,right));
                    break;
                default:
                    break label;
            }
        }
        return theNode.new PartExpression_Node(us);
    }

    //    # logic := equality ("&&" equality | "||" equality)*
    private astNode logic()   {
//        System.out.println("logic");
        astNode left=null,right=null;
        Token tok=null;
        List<astNode> us=null;
        left=equality();
        label:
        while (true){
            switch (currentToken.tokenName) {
                case "TK_AND":
                case "TK_OR":
                    tok=currentToken;
                    to_next_token();
                    right=equality();
                    us.add(theNode.new BinaryOp_Node(tok,left,right));
                    break;
                default:
                    break label;
            }
        }
        return theNode.new PartExpression_Node(us);
    }

    public static void main(String[] args) {
        compile c=new compile();
        Token[] tokens=c.getTokens();
        Parser parser=new Parser(tokens);
        parser.program();
    }
}
