package com.example.shrikarpatel.expreval.src.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class Expression {
//final edit
	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	expr.replaceAll("\\s+", "");
    	ArrayList <String> varsAndArrays = new ArrayList<String>();
    StringTokenizer exprModified = new StringTokenizer(expr,delims); 

    
    
    	
   	while(exprModified.hasMoreTokens()) {
    		varsAndArrays.add(exprModified.nextToken());
    		//String expr2 = exprModified.nextToken(delims).toString();
    		
    	}
    	//isDigit
    	for(int i = 0; i<varsAndArrays.size();i++) {
    		if(varsAndArrays.get(i).charAt(0)>= 48 &&varsAndArrays.get(i).charAt(0)<= 57 ) {
    			varsAndArrays.remove(i);
    			i--;
    		}
    
    	}
    	// need to fix this for duplicates
     	for(int i = 0; i<varsAndArrays.size();i++) {
     		String placeholder = varsAndArrays.get(i);
     		for(int j = i+1; j<varsAndArrays.size();j++) {
     			if(placeholder.equals(varsAndArrays.get(j))) {
     				varsAndArrays.remove(j);
     				j--;
     	
     			}
     		}
    	}
  
  
    	
    	for(int i = 0; i< varsAndArrays.size(); i++) {
    		String search = varsAndArrays.get(i);
    		
    		int searchIndex = expr.indexOf(search);
    			
    				if(searchIndex + search.length()!=expr.length()) {
    					char arrayBracket = expr.charAt(searchIndex+search.length());
    						if(arrayBracket == '[') {
    	
    							Array a = new Array(search);
    							arrays.add(a);
    							expr = expr.substring(searchIndex+search.length());
    						}else {
    							Variable a = new Variable(search);
    							vars.add(a);
    							expr = expr.substring(searchIndex+search.length());
    						}
    				} else {
    					Variable a = new Variable(search);
    					vars.add(a);
    					expr = expr.substring(searchIndex+search.length());
    				}
    			
    	}
    	
    	
    }
    	
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }




    public static void loadVariablesForApp(String variable, int value2, ArrayList<Variable> vars, ArrayList<Array> arrays){
    	int indexOfStr = vars.indexOf(variable);

	}
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	Stack<Float> values = new Stack<Float>();
    	Stack <String> symbols = new Stack<String>();
    	Stack <Array> arrs = new Stack<Array>();
     	expr = expr.replaceAll("\\s+", "");
    	//ArrayList <String> valuesAndSymbols = new ArrayList<String>();
     	
     	float finalAnswer = 0;
     	String valuesOrSymbols = "";
     	
     	

     	
StringTokenizer exprModified = new StringTokenizer(expr,delims,true); 
    	expr.replaceAll("\\s+", "");
    	while(exprModified.hasMoreTokens()) {   //go token by token
    		 valuesOrSymbols = exprModified.nextToken();
    		
    		
    		if(isArray(valuesOrSymbols,arrays)==true) { //checks if it is an Array, pushes into an Array Stack
    			Array a = new Array(valuesOrSymbols);
    			arrs.push(a);
    			
    		
    			
    		}else if(valuesOrSymbols.equals("[")) {
    			symbols.push(valuesOrSymbols);
    			
    		}else if(valuesOrSymbols.equals("]")) {		//closing Bracket, perform operation on all within bracket
    			while(symbols.peek().equals("[") == false && values.size()>=2) {
    				values.push(operation(values.pop(),symbols.pop(),values.pop()));
    			}
    			symbols.pop();	//pop the opening bracket
    			float index = values.pop(); //get the value of the Array and the index it is asking for
    			Array app = arrs.pop();
    			int arrayIndex = arrays.indexOf(app);
    			int index2 = (int)index;
    			int ans =arrays.get(arrayIndex).values[index2];
    			float ans2 = (float) ans;
    			values.push(ans2);
    			
    	
    	
    		}else if(valuesOrSymbols.equals("(")) {	//same as Array
    			symbols.push(valuesOrSymbols);
    		
    		
    			
    		}	else if(valuesOrSymbols.equals(")")) {
    		while(symbols.peek().equals("(") ==false) {
    		values.push(operation(values.pop(), symbols.pop(),values.pop()));
    			
    		}
    		
    		
    		symbols.pop();

    			
			//symbols.pop(); //discards the (
    		
    		
    		
    		
    	}
    	
    	
    	else if(isNumber(valuesOrSymbols) ==true) {				//check if its a number
    			float value = Float.parseFloat(valuesOrSymbols);
    			values.push(value);
    			
    			
    		}else if(isVariable(valuesOrSymbols,vars)==true) {		//check if its variable, get value then store
    			Variable variable = new Variable (valuesOrSymbols);  //need to somehow make this general for any string getting to this point
         		int indexOfVariable = vars.indexOf(variable);
         		float variableValue = vars.get(indexOfVariable).value;
         		
         		values.push(variableValue);
         		
         		
    		}else if(isOperator(valuesOrSymbols) == true){
    			
        		if(symbols.isEmpty() == false) {
        			if(currentOperatorHasPres(valuesOrSymbols, symbols) == true) {
        				
        				symbols.push(valuesOrSymbols);
        			} else {
        				while(symbols.isEmpty() == false && values.isEmpty()==false && values.size()>=2 && currentOperatorHasPres(valuesOrSymbols, symbols) == false) {
        				
        	    			float ans = operation(values.pop(), symbols.pop(),values.pop());
        	    			
        	    			values.push(ans);
        	    			
        				}
        				symbols.push(valuesOrSymbols);
        				
        			} 
        			
        			
        		} else if(symbols.isEmpty() == true) {
    		
    			
    			String operator = valuesOrSymbols;
    			symbols.push(operator);
        		}
    			
    		}
    		
    		
    	}
    	
   
    	
    	if(symbols.isEmpty()== false) {
    		if(symbols.peek() == "(") {
    			symbols.pop();
    		}
    	}
    	
    	while(symbols.isEmpty()==false && values.size()>=2) {
    		float ans = operation(values.pop(),symbols.pop(),values.pop());
    		
    		values.push(ans);
    		
    	}
    	

   
    	finalAnswer = values.pop();
   
    	
    	return finalAnswer; //ultimate goal is to finally have final answer in values stack
    
 
    }
    
    private static boolean isNumber (String valuesAndSymbols) {
    			if(valuesAndSymbols.charAt(0)>= 48 &&valuesAndSymbols.charAt(0)<= 57 ) {
    				return true;
    			}
    		
    		return false;
    }
    
    private static boolean isVariable (String valuesAndSymbols, ArrayList<Variable> vars) {
    	  Variable a = new Variable(valuesAndSymbols);
    	if(vars.contains(a) == true) {
    		return true;
    	}else {
    		return false;
    	}
    	}
    
    private static boolean isOperator (String valuesAndSymbols) {
    	
    	boolean isOperator = false;
    		if(valuesAndSymbols.equals("+") || valuesAndSymbols.equals("-")|| valuesAndSymbols.equals("*") || valuesAndSymbols.equals("/")|| valuesAndSymbols.equals("(")) {
    			isOperator = true;
    		}
    		return isOperator;
    				
    }
    
    private static float operation(float num2, String operand, float num1) {
    	float answer = 0;
    	
    	if(operand.equals("+")) {
 			answer = num1 + num2;
 		} else if (operand.equals("-")) {
 			answer = num1 - num2;
 		} else if (operand.equals("*")) {
 			answer =  num1* num2;
 		} else if (operand.equals("/")) {
 			answer =  num1/num2;
 		}
    		return answer;
    }

    private static boolean currentOperatorHasPres(String valuesAndSymbols, Stack<String> symbols) {
    	boolean hasPres = false;
    	if((valuesAndSymbols.equals("+") || valuesAndSymbols.equals("-")) && (symbols.peek().equals("*")||symbols.peek().equals("/"))){
    		hasPres = false;
    	}else if((valuesAndSymbols.equals("*") || valuesAndSymbols.equals("/")) && (symbols.peek().equals("+")||symbols.peek().equals("-"))) {
    		hasPres = true;
    	} else if ((valuesAndSymbols.equals("+") || valuesAndSymbols.equals("-")) && (symbols.peek().equals("+")||symbols.peek().equals("-"))) {
    		hasPres = false;
    	} else if((valuesAndSymbols.equals("*") || valuesAndSymbols.equals("/")) && (symbols.peek().equals("*")||symbols.peek().equals("/"))) {
    		hasPres = false;
    	} else if ((valuesAndSymbols.equals("+")  || valuesAndSymbols.equals("-")||valuesAndSymbols.equals("*") ||valuesAndSymbols.equals("/") ) && symbols.peek().equals("(")) {
    		hasPres = true;
    	}else if ((valuesAndSymbols.equals("+")  || valuesAndSymbols.equals("-")||valuesAndSymbols.equals("*") ||valuesAndSymbols.equals("/") ) && symbols.peek().equals("[")) {
    		hasPres =true;
    }
    	return hasPres;
    }
   

    private static boolean isArray(String expr, ArrayList<Array> array) {
    	Array a = new Array(expr);
    	
    	if(array.contains(a) == true) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    /*System.out.println("Bracket Detected");
		 leftMostBracket = expr.indexOf(']',rightMostBracket(expr));
		String leftOfBracketInclusive = expr.substring(0,rightMostBracket(expr)+1);
		float insideBracket = evaluate(expr.substring(rightMostBracket(expr)+1, leftMostBracket),vars, arrays);
		String rightOfBracketInclusive = expr.substring(leftMostBracket,expr.length());
		
		expr = leftOfBracketInclusive + Float.toString(insideBracket) + rightOfBracketInclusive;
		System.out.println("This is after brackets " + expr);*/
    
    //substrings :( negative nums. 
    
    
    
    
    
    
    
    }
    






    
    
    
