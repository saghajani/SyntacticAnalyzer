/*
 * Programming Language: Java
 * Implementation Time: 1392/10/28 (2014/01/18)
 * Platform: NetBeans IDE 7.0.1
 */
package syntactic.analyzer;


/**
 *
 * @author Saeed Aghajani
 */
public class SyntacticAnalyzer {
    LexicalAnalyzer lexicalAnalyzer;
    String[] tokens;
    int tokenNumber;
    boolean errorDetected;
    
    public SyntacticAnalyzer(){
        lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.scanProgram();
        tokens = new String[lexicalAnalyzer.getProgramTokens().size()];
        tokens = lexicalAnalyzer.getProgramTokens().toArray(tokens); 
        tokenNumber = -1;
        errorDetected = false;
        //for(int i = tokenNumber; i<lexicalAnalyzer.getProgramTokens().size(); )
    }
    
    public String nextToken(){
        tokenNumber++;
        return tokens[tokenNumber];
    }
    
    //Grammer Rules Methods
    public void prog(){
        stmtList();
        if(nextToken().equals("Reserved_END")){
            System.out.println("END");
            if(!errorDetected)
                System.out.println("Syntactic Analyze is Successfully Completed.");
        }
        else{
            errorDetected = true;
            System.out.println("err in prog");
            System.out.println("Unexpected Token");
            System.exit(0);
        }                      
    }
    
    public void stmtList(){
        stmt();
        stmtListTail();
    }
    
    public void stmt(){ 
        if(nextToken().equals("Identifier")){
            System.out.println("ID");
            if(nextToken().equals("Assign")){
                System.out.println(":=");
                expr();
            }
            else{
                tokenNumber--;
                errorDetected = true;
                System.out.println("err in stmt");
                System.out.println("Unexpected Token");
                System.exit(0);
            }               
        }
        else{
            tokenNumber--;
            if(nextToken().equals("Reserved_READ")){
                System.out.println("READ");
                if(nextToken().equals("LeftParen")){
                    System.out.println("(");
                    idList();
                    if(nextToken().equals("RightParen"))
                        System.out.println(")");
                    else{
                        tokenNumber--;
                        errorDetected = true;
                        System.out.println("err in stmt");
                        System.out.println("Unexpected Token");
                        System.exit(0);
                    }                       
                }
                else{
                    tokenNumber--;
                    errorDetected = true;
                    System.out.println("err in stmt");
                    System.out.println("Unexpected Token");
                    System.exit(0);
                }                   
            }
            else{
                tokenNumber--;
                if(nextToken().equals("Reserved_WRITE")){
                    System.out.println("WRITE");
                    if(nextToken().equals("LeftParen")){
                        System.out.println("(");
                        exprList();
                        if(nextToken().equals("RightParen"))
                            System.out.println(")");
                        else{
                            tokenNumber--;
                            errorDetected = true;
                            System.out.println("err in stmt");
                            System.out.println("Unexpected Token");
                            System.exit(0);
                        }
                    }
                    else{
                        tokenNumber--;
                        errorDetected = true;
                        System.out.println("err in stmt");
                        System.out.println("Unexpected Token");
                        System.exit(0);
                    }
                }
                else{
                    tokenNumber--;
                    errorDetected = true;
                    System.out.println("err in stmt");
                    System.out.println("Unexpected Token");
                    System.exit(0);
                }
            }  
        }
    }
    
    public void stmtListTail(){
        if(nextToken().equals("Semicolon")){
            System.out.println(";");
            stmtList();
        }
        else
           tokenNumber--;           
    }
    
    public void expr(){
        term();
        exprTail();
    }
    
    public void idList(){
        if(nextToken().equals("Identifier")){
            System.out.println("ID");
            idListTail();
        }
        else{
            tokenNumber--;
            errorDetected = true;
            System.out.println("err in idList");
            System.out.println("Unexpected Token");
            System.exit(0);
        }
    }
    
    public void exprList(){
        expr();
        exprListTail();
    }
    
    public void term(){
        primary();
        termTail();
    }
    
    public void exprTail(){
       if(nextToken().equals("AddSign")) {
           System.out.println("+");
           term();
           exprTail();
       }
       else{
           tokenNumber--;
           if(nextToken().equals("MinusSign")){
               System.out.println("-");
               term();
               exprTail();
           }
           else
               tokenNumber--;
       }
    }
    
    public void primary(){
        if(nextToken().equals("MinusSign")){
            System.out.println("-");
            primary();
        }
        else{
            tokenNumber--;
            if(nextToken().equals("LeftParen")){
                System.out.println("(");
                expr();
                if(nextToken().equals("RightParen"))
                    System.out.println(")");
                else{
                    tokenNumber--;
                    errorDetected = true;
                    System.out.println("err in primary");
                    System.out.println("Unexpected Token");
                    System.exit(0);
                }
            }
            else{
                tokenNumber--;
                if(nextToken().equals("Identifier"))
                    System.out.println("ID");
                else{
                    tokenNumber--;
                    if(nextToken().equals("DecimalNum"))
                        System.out.println("CONSTANT");
                    else{
                        tokenNumber--;
                        errorDetected = true;
                        System.out.println("err in primary");
                        System.out.println("Unexpected Token");
                        System.exit(0);
                    }
                }
            }
        }
    }
    
    public void termTail(){
       if(nextToken().equals("MulSign")) {
           System.out.println("*");
           primary();
           termTail();
       }
       else{
           tokenNumber--;
           if(nextToken().equals("DivSign")){
               System.out.println("/");
               primary();
               termTail();
           }
           else
               tokenNumber--;
       }        
    }
    
    public void exprListTail(){
        if(nextToken().equals("Separator")){
            System.out.println(",");
            exprList();
        }
        else
            tokenNumber--;
    }
    
    public void idListTail(){
        if(nextToken().equals("Separator")){
            System.out.println(",");
            idList();
        }
        else
            tokenNumber--;
    }
    
    
    public static void main(String[] args) {
        SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer();
        System.out.println(syntacticAnalyzer.tokens.length);
        syntacticAnalyzer.prog();
        //System.out.println(syntacticAnalyzer.tokens[0]+syntacticAnalyzer.tokens[syntacticAnalyzer.tokens.length-1]);
        //LexicalAnalyzer la = new LexicalAnalyzer();
        //la.scanProgram();
        //System.out.println(la.getProgramTokens().toString());
    }
} 
