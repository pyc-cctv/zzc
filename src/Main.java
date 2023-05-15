import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;
import java.util.Scanner;

public class Main {

    private static String[] op = { "+", "-", "*", "/" };// Operation set
    public static void main(String[] args) {
        try (Scanner n = new Scanner(System.in)) {
            System.out.println("请输入参数n,将生成n道练习题");
            int x=n.nextInt();
            System.out.println("生成的"+x+"道练习题如下");
            String[] ret=new String[x];
            for(int i=0;i<x;i++){
                String question = MakeFormula();
                System.out.println(question);
                ret[i] = Solve(question);
                System.out.println(ret[i]);
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter("subject.txt"));
                    out.write(Arrays.toString(ret));
                    out.close();
                    System.out.println("文件创建成功！");
                } catch (IOException e) {
                    System.out.println("...");
                }
            }
        }
    }

    public static String MakeFormula(){
        StringBuilder build = new StringBuilder();
        int count = (int) (Math.random() * 2) + 1; // generate random count
        int start = 0;
        int number1 = (int) (Math.random() * 99) + 1;
        build.append(number1);
        while (start <= count){
            int operation = (int) (Math.random() * 3); // generate operator
            int number2 = (int) (Math.random() * 99) + 1;
            build.append(op[operation]).append(number2);
            start ++;
        }
        return build.toString();
    }

    public static String Solve(String formula){
        Stack<String> tempStack = new Stack<>();//Store number or operator
        Stack<Character> operatorStack = new Stack<>();//Store operator
        int len = formula.length();
        int k = 0;
        for(int j = -1; j < len - 1; j++){
            char formulaChar = formula.charAt(j + 1);
            if(j == len - 2 || formulaChar == '+' || formulaChar == '-' || formulaChar == '/' || formulaChar == '*') {
                if (j == len - 2) {
                    tempStack.push(formula.substring(k));
                }
                else {
                    if(k < j){
                        tempStack.push(formula.substring(k, j + 1));
                    }
                    if(operatorStack.empty()){
                        operatorStack.push(formulaChar); //if operatorStack is empty, store it
                    }else{
                        char stackChar = operatorStack.peek();
                        if ((stackChar == '+' || stackChar == '-')
                                && (formulaChar == '*' || formulaChar == '/')){
                            operatorStack.push(formulaChar);
                        }else {
                            tempStack.push(operatorStack.pop().toString());
                            operatorStack.push(formulaChar);
                        }
                    }
                }
                k = j + 2;
            }
        }
        while (!operatorStack.empty()){ // Append remaining operators
            tempStack.push(operatorStack.pop().toString());
        }
        Stack<String> calcStack = new Stack<>();
        for(String peekChar : tempStack) { // Reverse traversing of stack
            if (!peekChar.equals("+") && !peekChar.equals("-") && !peekChar.equals("/") && !peekChar.equals("*")) {
                calcStack.push(peekChar); // Push number to stack
            } else {
                int a1 = 0;
                int b1 = 0;
                if (!calcStack.empty()) {
                    b1 = Integer.parseInt(calcStack.pop());
                }
                if (!calcStack.empty()) {
                    a1 = Integer.parseInt(calcStack.pop());
                }
                switch (peekChar) {
                    case "+":
                        calcStack.push(String.valueOf(a1 + b1));
                        break;
                    case "-":
                        calcStack.push(String.valueOf(a1 - b1));
                        break;
                    case "*":
                        calcStack.push(String.valueOf(a1 * b1));
                        break;
                    default:
                        calcStack.push(String.valueOf(a1 / b1));
                        break;

            }
        }
    }
        return formula + "=" + calcStack.pop();
}


}
