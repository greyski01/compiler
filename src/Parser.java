public class Parser
{
    static Token[] tokens=null;
    static int cursor=0;
    Token currentToken;
    public Parser(Token[] theTokens){
        tokens=theTokens;
        currentToken=theTokens[0];
    }

    private void to_next_token(){
        currentToken=tokens[++cursor];
    }

    private void eat(String tokenType){
        if (currentToken.tokenName.equals(tokenType))
            to_next_token();
    }



}
