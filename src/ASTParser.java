//import java.util.List;
//
//public class ASTParser
//{
//    public Token[] tokens=null;
//    public int cursor=0;
//    public Token currentToken;
//    public List<astNode> actual_parameter_nodes=null;
//    public astNode theNode;
//    public String current_function_name;
//
//    public ASTParser(Token[] theTokens){
//        tokens=theTokens;
//        currentToken=theTokens[0];
//        theNode=new astNode();
//    }
//
//    private void to_next_token(){
//        currentToken=tokens[++cursor];
//    }
//
//    private void errorReport(){
//        System.out.println("error---->"+currentToken.tokenName);
//        System.out.println("error---->"+currentToken.errorLine);
//        System.out.println("error---->"+currentToken.errorPlace);
//    }
//
//    private void eat(String tokenType){
//        if (currentToken.tokenName.equals(tokenType))
//            to_next_token();
//        else
//            errorReport();
//    }
//
//    private astNode program(){
//        //program-->function_definition*
//        //p-->fds
//        return theNode.new programNode(function_definitions());
//    }
//
//    private List<astNode> function_definitions(){
//        //function_definition-->function_definition *
//        List<astNode> function_definitions = null;
//        while (tokens[cursor+1]!=null)
//            function_definitions.add(function_definition());
//        return function_definitions;
//    }
//
//    private astNode function_definition(){
//        // function_definition-->type_specification identifier
//        // "(" formal_parameters ")" block
//        //fd-->ts id (fps) block
//        astNode ts=type_specification();
//        String function_name=currentToken.value;
//        eat("ID");
//        List<astNode> formal_params=null;
//        if (currentToken.tokenName.equals("TK_LPAREN")){
//            eat("TK_LPAREN");
//            if (!currentToken.tokenName.equals("TK_RPAREN"))
//                formal_params=formal_parameters();
//            if (currentToken.tokenName.equals("TK_RPAREN"))
//                eat("TK_RPAREN");
//            else
//                errorReport();
//        }
//        else
//            errorReport();
//        astNode blockNode=null;
//        if (currentToken.tokenName.equals("TK_LBRACE"))
//            blockNode=block();
//        else
//            errorReport();
//        return theNode.new FunctionDef_Node(ts,blockNode,function_name,formal_params);
//    }
//
//    //type_specification = int
//    private astNode type_specification(){
//        if (currentToken.tokenName.equals("TK_INT"))
//            eat("TK_INT");
//        return theNode.new Type_Node(currentToken);
//    }
//
//    private List<astNode> formal_parameters(){
//        // formal_parameters-->formal_parameter formal_parameters
//        //fps-->fp fps
//        List<astNode> formal_params = null;
//        formal_params.add(formal_parameter());
//        while (!currentToken.tokenName.equals("TK_RPAREN")){
//            if (currentToken.tokenName.equals("TK_COMMA"))
//                eat("TK_COMMA");
//            formal_params.add(formal_parameter());
//        }
//        return formal_params;
//    }
//
//    private astNode formal_parameter(){
//        astNode ts = null,param = null;
//        if (currentToken.tokenName.equals("TK_INT"))
//            ts = type_specification();
//        else
//            errorReport();
//        if (currentToken.tokenName.equals("ID"))
//            param= theNode.new Var_Node(currentToken);
//        else
//            errorReport();
//        eat("ID");
//        return theNode.new FormalParam_Node(ts, param);
//    }
//
//    private astNode block(){
//        List<astNode> statementsNode=null;
//        if (currentToken.tokenName.equals("TK_LBRACE")){
//           eat("TK_LBRACE");
//        }
//        else
//            errorReport();
//        statementsNode = compound_statement();
//        if (currentToken.tokenName.equals("TK_RBRACE")){
//            eat("TK_RBRACE");
//        }
//        else
//            errorReport();
//        return theNode.new Block_Node(statementsNode);
//    }
//
//    //compound_statement = (variable_declaration | statement)*
//    private List<astNode> compound_statement(){
//        List<List<astNode>> statement_nodes=null,vd=null;
//        while (!currentToken.tokenName.equals("TK_RBRACE")&&!currentToken.tokenName.equals("TK_EOF")){
//            if (currentToken.tokenName.equals("TK_INT")){
//                vd.add(variable_declaration());
//            }
//            else
//                statement_nodes.add(statement());
//        }
//        return
//    }
//
//    //statement = expression-statement
//    //    | "return" expression-statement
//    //    | block
//    //    | "if" "(" expression ")" statement ("else" statement)?
//    private astNode statement(){
//        //"return" expression-statement
//        if (currentToken.tokenName.equals("TK_RETURN")){
//            eat("TK_RETURN");
//            return theNode.new Return_Node(expression_statement(),currentToken,current_function_name);
//        }
//        //block
//        else if (currentToken.tokenName.equals("TK_LBRACE"))
//            return block();
//            //"if" "(" expression ")" statement ("else" statement)?
//        else if (currentToken.tokenName.equals("TK_IF")){
//            eat("TK_IF");
//            astNode condition = null;
//            astNode then_statement = null;
//            astNode else_statement = null;
//            if (currentToken.tokenName.equals("TK_LPAREN")) {
//                eat("TK_LPAREN");
//                condition = expression();
//                eat("TK_LPAREN");
//                if (currentToken.tokenName.equals("TK_THEN")){
//                    eat("TK_THEN");
//                    then_statement=statement();
//                    if (currentToken.tokenName.equals("TK_ELSE"))
//                    {
//                        eat("TK_ELSE");
//                        else_statement=statement();
//                    }
//                }
//            }
//            return theNode.new If_Node(condition,then_statement,else_statement);
//        }
//        else
//            return expression_statement();
//    }
//
//    //variable_declaration = type_specification (indentifier ("=" expr)?
//    // ("," indentifier ("=" expr)?)*)? ";"
//    private List<astNode> variable_declaration(){
//        astNode node=type_specification();
//        List<astNode> variable_node=null;
//        while (!currentToken.tokenName.equals("TK_SEMICOLON")){
//            if (currentToken.tokenName.equals("ID")){
//                variable_node.add(theNode.new VarDecl_Node(node,theNode.new Var_Node(currentToken)));
//                eat("ID");
//                if (currentToken.tokenName.equals("TK_COMMA"))
//                    eat("TK_COMMA");
//            }
//        }
//        eat("TK_SEMICOLON");
//        return variable_node;
//    }
//
//    private astNode primary(){
//        //是一个或,(expr)|ident args?|num
//        astNode node;
//        //分别审查(expr)
//        if (currentToken.tokenName.equals("TK_LPAREN")){
//            eat("TK_LPAREN");
//            node=expression();
//            eat("TK_RPAREN");
//            return node;
//        }
//
//        //ident
//        if (currentToken.tokenName.equals("id")) {
//            eat("id");
//            //function call
//            if (currentToken.tokenName.equals("TK_LPAREN")){
//                current_function_name=currentToken.value;
//                eat("TK_LPAREN");
//                if (!currentToken.tokenName.equals("TK_RPAREN"))
//                {//即函数的参数列表不为空
//                    actual_parameter_nodes.add(assign());
//                }
//
//                while (currentToken.tokenName.equals("TK_COMMA"))
//                {
//                    eat("TK_COMMA");
//                    actual_parameter_nodes.add(assign());
//
//                }
//            }
//        }
//
//        return null;
//    }
//
//
//    private astNode equality(){
//
//    }
//
//    //assign = equality ("=" assign)?
//    private astNode assign(){
//        astNode node=equality();
//        if (currentToken.tokenName.equals("TK_ASSIGN"))
//        {
//            eat("TK_ASSIGN");
//            node=theNode.new Assign_Node(currentToken,node,assign());
//        }
//        return node;
//    }
//
//    //expression = assign
//    private astNode expression(){
//        return assign();
//    }
//
//    //expression-statement = expression? ";"
//    private astNode expression_statement(){
//        astNode node=null;
//        if (currentToken.tokenName.equals("TK_SEMICOLON"))
//            eat("TK_SEMICOLON");
//        else {
//            node = expression();
//            if (currentToken.tokenName.equals("TK_SEMICOLON"))
//                eat("TK_SEMICOLON");
//
//        }
//        return node;
//
//    }
//
//
//
//
//
//
//
//
//}
