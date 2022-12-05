import java.awt.event.TextEvent;
import java.util.List;

public class astNode {

    public astNode() {
    }

    class UnaryOp_Node extends astNode {
        Token token, op;
        astNode right;

        public UnaryOp_Node(Token op, astNode right) {
            this.token = this.op = op;
            this.right = right;
        }
    }

    class If_Node extends astNode {
        astNode condition, then_s, else_s;

        public If_Node(astNode condition, astNode then_s, astNode else_s) {
            this.condition = condition;
            this.then_s = then_s;
            this.else_s = else_s;
        }
    }

    class Return_Node extends astNode {
        astNode right;
        String functionName;

        public Return_Node(astNode right,String functionName) {
            this.right = right;
            this.functionName = functionName;
        }
    }

    class While_Node extends astNode{
        astNode condition,statement;
        public While_Node(astNode condition,astNode statement) {
            this.condition = condition;
            this.statement = statement;
        }
    }

    class Block_Node extends astNode {
        List<astNode> state_n;
        public Block_Node(List<astNode> state_n) {
            this.state_n = state_n;
        }
    }

    class PartExpression_Node extends astNode{
        List<astNode> partExpression;
        public PartExpression_Node(List<astNode> partExpression) {
            this.partExpression = partExpression;
        }
    }

    class BinaryOp_Node extends astNode {
        Token token, op;
        astNode left, right;

        public BinaryOp_Node(Token token, astNode left, astNode right) {
            this.token = this.op = token;
            this.left = left;
            this.right = right;
        }
    }

    class Assign_Node extends astNode {
        Token token, op;
        astNode left, right;

        public Assign_Node(Token token, astNode left, astNode right) {
            this.token = this.op = token;
            this.left = left;
            this.right = right;
        }
    }

    class FunctionCall_Node extends astNode {
        astNode right;
        Token tok;
        String functionName;
        astNode actual_parameter;

        public FunctionCall_Node(astNode actual_parameter, Token tok, String functionName) {
            this.actual_parameter = actual_parameter;
            this.tok = tok;
            this.functionName = functionName;
        }
    }

    class Num_Node extends astNode {
        Token tok;
        String value;

        public Num_Node(Token tok) {
            this.tok = tok;
            this.value = tok.value;
        }
    }

    class Var_Node extends astNode {
        Token tok;
        String value;
        Symbol symbol;

        public Var_Node(Token tok) {
            this.tok = tok;
            this.value = tok.value;
            this.symbol = null;
        }
    }

    class compound_Statement_Node extends astNode {
        List<astNode> st = null;
        List<List<astNode>> vds = null;

        public compound_Statement_Node(List<List<astNode>> vds, List<astNode> st) {
            this.vds = vds;
            this.st = st;
        }
    }


    class Type_Node extends astNode {
        Token tok;

        public Type_Node(Token tok) {
            this.tok = tok;
        }
    }

    class VarDecl_Node extends astNode {
        astNode type_node, var_node;

        public VarDecl_Node(astNode type_node, astNode var_node) {
            this.type_node = type_node;
            this.var_node = var_node;
        }
    }

    class FormalParam_Node extends astNode {
        astNode type_node;
        Token parameter;
        Symbol parameter_symbol;

        public FormalParam_Node(astNode type_node, Token parameter) {
            this.type_node = type_node;
            this.parameter=parameter;
            parameter_symbol = null;
        }
    }

    class FunctionDef_Node extends astNode {
        astNode type_node, block_node;
        String functionName;
        List<astNode> formal_parameters;
        int offset;

        public FunctionDef_Node(astNode type_node, astNode block_node, String functionName, List<astNode> formal_parameters) {
            this.type_node = type_node;
            this.block_node = block_node;
            this.functionName = functionName;
            this.formal_parameters = formal_parameters;
            this.offset = 0;
        }
    }

    class programNode extends astNode {
        List<astNode> fdsNode;

        public programNode(List<astNode> fdsNode) {
            this.fdsNode = fdsNode;
        }
    }
}
