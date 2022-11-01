public class astNode {

    public astNode(){}

    class UnaryOp_Node extends astNode{
        Token token,op;
        astNode right;
        public UnaryOp_Node(Token op,astNode right){
            this.token=this.op=op;
            this.right=right;
        }
    }

    class If_Node extends astNode{
        astNode condition,then_s,else_s;
        public If_Node(astNode condition,astNode then_s,astNode else_s){
            this.condition=condition;
            this.then_s=then_s;
            this.else_s=else_s;
        }
    }

    class Return_Node extends astNode{
        astNode right;
        Token tok;
        String functionName;
        public Return_Node(astNode right, Token tok, String functionName){
            this.right = right;
            this.tok = tok;
            this.functionName = functionName;
        }
    }

    class Block_Node extends astNode{
        Token ltok,rtok;
        astNode state_n;
        public Block_Node(Token ltok,Token rtok,astNode state_n){
            this.ltok = ltok;
            this.rtok = rtok;
            this.state_n = state_n;
        }
    }

    class BinaryOp_Node extends astNode{
        Token token,op;
        astNode left,right;
        public BinaryOp_Node(Token token, astNode left, astNode right){
            this.token =this.op= token;
            this.left = left;
            this.right = right;
        }
    }

    class Assign_Node extends astNode{
        Token token,op;
        astNode left,right;
        public Assign_Node(Token token, astNode left, astNode right){
            this.token = this.op=token;
            this.left = left;
            this.right = right;
        }
    }

    class FunctionCall_Node extends astNode{
        astNode right;
        Token tok;
        String functionName;
        astNode actual_parameter;
        public FunctionCall_Node(astNode actual_parameter, Token tok, String functionName){
            this.actual_parameter = actual_parameter;
            this.tok = tok;
            this.functionName = functionName;
        }
    }

    class Num_Node extends astNode{
        Token tok;
        String value;
        public Num_Node(Token tok){
            this.tok = tok;
            this.value=tok.value;
        }
    }

    class Var_Node extends astNode{
        Token tok;
        String value;
        Symbol symbol;
        public Var_Node(Token tok){
            this.tok = tok;
            this.value=tok.value;
            this.symbol=null;
        }
    }

    class Type_Node extends astNode{
        Token tok;
        String value;
        public Type_Node(Token tok){
            this.tok = tok;
            this.value=tok.value;
        }
    }

    class VarDecl_Node extends astNode{
        astNode type_node,var_node;
        public VarDecl_Node(astNode type_node,astNode var_node){
            this.type_node = type_node;
            this.var_node = var_node;
        }
    }

    class FormalParam_Node extends astNode{
        astNode type_node,parameter_node;
        Symbol parameter_symbol;
        public FormalParam_Node(astNode type_node, astNode parameter_node){
            this.type_node = type_node;
            this.parameter_node = parameter_node;
            parameter_symbol=null;
        }
    }

    class FunctionDef_Node extends astNode{
        astNode type_node,block_node;
        String functionName;
        String[] formal_parameters;
        int offset;
        public FunctionDef_Node(astNode type_node,astNode block_node,String functionName,String[] formal_parameters){
            this.type_node = type_node;
            this.block_node = block_node;
            this.functionName = functionName;
            this.formal_parameters = formal_parameters;
            this.offset=0;
        }
    }

}
