/*
 * Programming Language: Java
 * Implementation Time: 1392/09/01 (2013/11/22)
 * Platform: NetBeans IDE 7.0.1
 */
package syntactic.analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Saeed Aghajani
 */
public class LexicalAnalyzer {
    static  boolean  isComment = false;//specifies the current position is within the comment or not?
    String filePath = "test5.txt";
    ArrayList<String> tokens = new ArrayList<String>();//arraylist that contains the program tokens after scanning the program.
    
    //reads the text file and return the string that contains the file content. 
    public String readFile(String path){
        String line;
        String content = "";     
        try {  
            BufferedReader br = new BufferedReader(new FileReader(path));
            while((line = br.readLine()) != null){
                content += line;
            }
        } catch (IOException ex) {
            Logger.getLogger(LexicalAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return content;
    }
    //show the lexeme and it's class name.
    public void showClass(String lexeme, String className){
            System.out.println(lexeme + " ........ " + className);
            /*
            if(className.equals("Unknown")){
                System.out.println("Unknown token detected!");
                System.exit(0);
            }   */  
            tokens.add(className);
    }
    
    //check if the character is allowable for an Identifier or not.
    public boolean isAllowable(char ch){
        return ((ch > 64 && ch < 91) || (ch > 96 && ch < 123) || (ch > 47 && ch < 58) || (ch == '_'));       
    }
    
    //check if the character is a number(0-9) or not. 
    public boolean isNumber(char ch){
        return (ch > 47 && ch < 58);
    }
    
    //check if the character is a Separator or not.
    public boolean isSeparator(char ch){
        return (ch == ' ' || ch == ',' || ch == '(' || ch == ')' || ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == ':' || ch == '$');
    }
    
    //scan the input character array that represent an instruction line.
    public void scanLine(char[] lineArray){
        boolean isNum = false, hasDot = false;//boolean variables specify that the current lexeme is a number and has dot.   
        char nextch;
        String lexeme = "", className = "Identifier";//default value for className is "Identifier".
        int i = 0;       
        label:
        while(i < lineArray.length){
            //passing the comment
            if(isComment){
                while(lineArray[i] != '$')
                    i++;
                i++;
                isComment = false;
                if(lexeme.length() > 0){
                    showClass(lexeme, className);
                    lexeme = "";
                    className = "Identifier";
                }
            }
            //passing the white spaces
            if(lineArray[i] == ' '){
                while(lineArray[i] == ' ')
                    i++;
                if(lexeme.length() > 0){
                    showClass(lexeme, className);
                    lexeme = "";
                    className = "Identifier";
                }
            }
            
            try{
                switch(lineArray[i]){
                    case '$':
                        isComment = true;
                        do{
                            i++;
                        }while(lineArray[i] != '$');
                        isComment = false;
                        i++;
                        continue label;
                    case 'r': case 'R':               
                        lexeme += lineArray[i];
                        nextch = lineArray[++i];
                        if(nextch == 'e' || nextch == 'E'){
                            lexeme += nextch;
                            nextch = lineArray[++i];
                            if(nextch == 'a' || nextch == 'A'){
                                lexeme += nextch;
                                nextch = lineArray[++i];
                                if(nextch == 'd' || nextch == 'D'){
                                    lexeme += nextch;
                                    i++;
                                    if(lexeme.length() == 4){//specifies that the lexeme is just "read" that has 4 characters.
                                        className = "Reserved_READ";
                                        if(isSeparator(lineArray[i])){
                                            showClass(lexeme, className);
                                            lexeme = "";
                                            className = "Identifier";
                                            continue label;
                                        }
                                        else
                                            className = "Identifier";
                                    }            
                                }                       
                            }                  
                        }          
                        break;
                    case 'w': case 'W':
                        lexeme += lineArray[i];
                        nextch = lineArray[++i];
                        if(nextch == 'r' || nextch == 'R'){
                            lexeme += nextch;
                            nextch = lineArray[++i];
                            if(nextch == 'i' || nextch == 'I'){
                                lexeme += nextch;
                                nextch = lineArray[++i];
                                if(nextch == 't' || nextch == 'T'){
                                    lexeme += nextch;
                                    nextch = lineArray[++i];
                                    if(nextch == 'e' || nextch == 'E'){
                                        lexeme += nextch;
                                        i++;
                                        if(lexeme.length() == 5){//specifies that the lexeme is just "write" that has 5 characters.
                                            className = "Reserved_WRITE";
                                            if(isSeparator(lineArray[i])){
                                                showClass(lexeme, className);
                                                lexeme = "";
                                                className = "Identifier";
                                                continue label;
                                            }
                                            else
                                                className = "Identifier";
                                        }      
                                    }
                                }                      
                            }                   
                        }
                        break;
                    case 'e': case 'E':
                        lexeme += lineArray[i];
                        nextch = lineArray[++i];
                        if(nextch == 'n' || nextch == 'N'){
                            lexeme += nextch;
                            nextch = lineArray[++i];
                            if(nextch == 'd' || nextch == 'D'){
                                lexeme += nextch;
                                i++;
                                if(lexeme.length() == 3){//specifies that the lexeme is just "end" that has 3 characters.
                                    className = "Reserved_END";
                                    if(isSeparator(lineArray[i])){
                                        showClass(lexeme, className);
                                        lexeme = "";
                                        className = "Identifier";
                                        continue label;
                                    }
                                    else
                                        className = "Identifier";
                                }      
                            }             
                        } 
                        break;
                    case '(':
                        showClass("(", "LeftParen");
                        i++;
                        continue label;
                    case ')':
                        showClass(")", "RightParen");
                        i++;
                        continue label;
                    case ',': 
                        showClass(",", "Separator");
                        i++;
                        continue label;
                    case '+':  
                        showClass("+", "AddSign");
                        i++;
                        continue label;
                    case '-':
                        showClass("-", "MinusSign");
                        i++;                        
                        continue label;
                    case '*':
                        showClass("*", "MulSign");
                        i++;
                        continue label;
                    case '/':
                        showClass("/", "DivSign");
                        i++;
                        continue label;  
                    case ':':
                        lexeme += ':';
                        className = "Unknown";
                        i++;
                        if(lineArray[i] == '='){
                            showClass(":=", "Assign");
                            i++;
                            lexeme = "";
                            className = "Identifier";
                        }
                        continue label;  
                    default: break;
                }//end of switch
            
        if(lexeme.length() == 0){
            if((lineArray[i] > 64 && lineArray[i] < 91) || (lineArray[i] > 96 && lineArray[i] < 123)){
                lexeme += lineArray[i];
                className = "Identifier";  
                i++;
            }
            else{
                if((lineArray[i] > 47 && lineArray[i] < 58)){                   
                    lexeme += lineArray[i];
                    className = "DecimalNum";
                    isNum = true;
                    i++;
                }
                else{
                    if(lineArray[i] == '.'){
                        lexeme += lineArray[i];
                        className = "Unknown";
                        isNum = hasDot = true;
                        i++;
                    }
                    else{
                        lexeme += lineArray[i];
                        className = "Unknown";
                        i++;
                    }
                }
            }                
        }
        
        else{
            if(isNum){
                if(lineArray[i] > 47 && lineArray[i] < 58){
                    lexeme += lineArray[i];
                    i++;
                }
                else{
                    if((lineArray[i] == '.') && (!hasDot)){                  
                        lexeme += lineArray[i];
                        hasDot = true;
                        i++;                       
                    }
                    else{                        
                        lexeme += lineArray[i];
                        className = "Unknown";                        
                        isNum = hasDot = false;
                        i++;
                    }                                             
                }                   
            }
            else{
                if(isAllowable(lineArray[i])){
                    lexeme += lineArray[i];
                    i++;
                }
                else{
                    if(!isSeparator(lineArray[i])){
                        lexeme += lineArray[i];
                        className = "Unknown";
                        i++;
                    }                   
                }                
            }
        }
        //check the next character and showing the class if needed.
        if(i < lineArray.length){
            if(isSeparator(lineArray[i])){
                showClass(lexeme, className);
                lexeme = "";
                className = "Identifier";
                isNum = hasDot = false;
            }
            else{
                if(isNum && hasDot){
                    if(isNumber(lineArray[i]))
                        className = "DecimalNum";
                    else{
                        className = "Unknown";
                        isNum = hasDot = false;
                    }                        
                }
                else{
                    if(isNum){
                        if(!isNumber(lineArray[i]) && (lineArray[i] != '.')){
                            className = "Unknown";
                            isNum = hasDot = false;
                        }
                    }
                    else{
                        if(!isAllowable(lineArray[i]))
                            className = "Unknown";
                    }
                }                            
            }                       
        }
        else{
            showClass(lexeme, className);
            lexeme = "";
            className = "Identifier";
            isNum = hasDot = false;
        }
            }catch(ArrayIndexOutOfBoundsException ex){//if overpass the lineArray and if there is any lexeme, it'll be showed in this part.
                if(lexeme.length() > 0){
                    showClass(lexeme, className);                    
                }
                break;
            }
        }//end of while       
    }//end of method scanLine
    
    public void scanProgram(){
        String content = readFile(filePath);
        StringTokenizer stringTokenizer = new StringTokenizer(content, ";");//a StringTokenizer Object that is used to parsing the string from ';' token.
        char[] lineArray;
        while(stringTokenizer.hasMoreTokens()){
            lineArray = stringTokenizer.nextToken().toCharArray();
            scanLine(lineArray);
            if(stringTokenizer.hasMoreTokens() && !isComment)//avoid to print semicolons are located in the comment.
                showClass(";", "Semicolon");
        }
    }
    //return the program tokens.
    public ArrayList<String> getProgramTokens(){
        return tokens;
    }
    
    public void clearTokenList(){
        tokens.clear();
    }
}
