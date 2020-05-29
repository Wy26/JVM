package simpleVM;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 装载器和执行引擎
 */
public class LoadBytecode {
    private int len; // 用于动态定义方法区的大小
    private Stack<Integer> valueStack = new Stack<Integer>(); // 值栈
    private int pc = 0; // 程序计数器
    public String methodArea[] = new String[len]; // 方法区保存指令
    private Map<String, Integer> mapRegister = new HashMap<String, Integer>(); // 用键值对保存变量值
    private String bytecode; // 字节码

    public LoadBytecode(String bytecode) { //初始化方法区
        super();
        len = bytecode.length();
        methodArea = new String[len/3]; // 初始化方法区的长度
        for (int i = 0; i < methodArea.length; i++) {// 初始化方法区
            methodArea[i] = " ";
        }
        this.bytecode = bytecode;
    }

    /**
     * 将字节码装载到方法区
     *
     * 遍历字节码长度中每个位置的字符，然后将字节码特定位置的字符赋给变量ch，如果是"，"或者"/n"，将str添加到方法区数组中此时str为空，
     * 如果不是"，"，或者换行符将字节码字符赋给str将str添加到方法区数组中
     */
    public void loadMethod() {
        char ch;
        String str = "";
        int j = 0;
        for (int i = 0; i < bytecode.length(); i++) {
            ch = bytecode.charAt(i);
            if (ch == ',' || ch == '\n') {
                methodArea[j] = str;
                j++;
                str = "";
            } else {
                str += ch;
            }
        }
    }

    /**
     * 执行方法区的指令
     */
    public void run() {
        String str;
        for (pc = 0; pc < methodArea.length; pc++) {
            str = methodArea[pc];
            switch (str) {
                case "Ope": {
                    int num;
                    pc++;
                    if (isNum(methodArea[pc])) { // 如果是数字直接转换类型
                        num = Integer.parseInt(methodArea[pc]);
                        valueStack.add(num); //压入值栈
                        System.out.println("push：" + num + ".");
                    } else { // 是变量名从寄存器中读取对应的值
                        try {
                            num = readRegister(methodArea[pc]); //将变量的值从寄存器中读入
                            valueStack.add(num);
                            System.out.println("push：" + num + ".");
                        } catch (NullPointerException e) {
                            methodArea[pc + 1] = "HALT";
                        }

                    }
                    break;
                }
                case "ADD": {
                    int num1, num2;
                    num1 = valueStack.pop();
                    num2 = valueStack.pop();
                    valueStack.push(num2 + num1);
                    System.out.println(num2 + "与" + num1 + "相加");
                    break;
                }
                case "SUB": {
                    int num1, num2;
                    num1 = valueStack.pop();
                    num2 = valueStack.pop();
                    valueStack.push(num2 - num1);
                    System.out.println(num2 + "与" + num1 + "相减");
                    break;
                }
                case "MUL": {
                    int num1, num2;
                    num1 = valueStack.pop();
                    num2 = valueStack.pop();
                    valueStack.push(num2 * num1);
                    System.out.println(num1 + "与" + num2 + "相乘");
                    break;
                }
                case "DIV": {
                    int num1, num2;
                    num1 = valueStack.pop();
                    num2 = valueStack.pop();
                    if (num1 == 0) {
                        System.out.println("除数不能为0！");
                        methodArea[pc + 1] = "HALT";
                    } else {
                        valueStack.push(num2 / num1);
                        System.out.println(num2 + "与" + num1 + "相除");
                    }
                    break;
                }
                case "HALT": {
                    pc = methodArea.length;
                    System.out.println("FINISH");
                    break;
                }
                case "POP": {
                    System.out.println("结果为: "+valueStack.pop());
                    break;
                }
                case "STOR": {
                    pc++;
                    int num = valueStack.peek(); //得到栈顶元素
                    saveRegister(methodArea[pc], num);
                    System.out.println("将变量" + methodArea[pc] + "=" + num + "存入寄存器");
                    break;
                }
            }
        }
    }

    /**
     * 判断是否为整形数字
     */
    private boolean isNum(String num) {
        boolean flag = true;
        for (int i = 0; i < num.length(); i++) {
            if ((i == 0 && num.charAt(i) == '-') || (i == 0 && num.charAt(i) == '+'))
                flag = true;
            else if (num.charAt(i) < '0' || num.charAt(i) > '9')
                flag = false;
        }
        return flag;
    }

    /**
     * 将变量名和值存入寄存器
     */
    private void saveRegister(String key, int num) {
        mapRegister.put(key, num);
    }

    /**
     * 读取寄存器的值
     */
    private int readRegister(String key) {
        if (mapRegister.get(key) == null){
            System.out.println("寄存器中没有该变量！");
            System.exit(1);
        }
        return mapRegister.get(key);
    }
}

