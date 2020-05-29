package simpleVM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 读入源代码文件，校验其正确性后，转换成字节码，写入字节码文件
 * 源代码采用四元式的形式，(指令，操作数，操作数，用于保存结果的变量)
 */
public class ByteCode {

    private String sourceFile = ""; // 源文件内容
    private static StringBuffer strBufBytecode = new StringBuffer(); // 保存转换的字节码
    private String bytecode = "";

    public String getSourceFile() {  //获取源文件内容
        return sourceFile;
    }

    public String getBytecode() {  //获取字节码
        bytecode = strBufBytecode.toString();
        return bytecode;
    }

    /** 读入源代码文件 **/
    public void readSourceFile() {
        try {
            File file = new File("src/simpleVM/input.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr); //缓冲输入

            String str = br.readLine(); //读取一个文本行
            while (str != null) {
                if (pan(str))
                    sourceFile += str;
                else { // 发现源码中的错误将scourFile置为error
                    System.out.print("源码出错!");
                    sourceFile = "error";
                    break;
                }
                str = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("文件读入失败");
        }
    }

    /** 将原文件转换成字节码 **/
    public void conver() {
        if (sourceFile.equals("error")) {
            strBufBytecode.append("error");
        } else {
            String strAry[] = sourceFile.split(";"); //以分号为间隔拆分字符串
            for (int i = 0; i < strAry.length; i++) {
                sourceToBytecode(strAry[i]); //将每个具体的四元式转化成字节码
            }
            strBufBytecode.append("POP\n"+"HALT\n");	//将最后的结果输出，并停机
        }
        //写入字节码文件
        try {
            FileWriter fw = new FileWriter("src/simpleVM/byte-code.txt");
            fw.write(strBufBytecode.toString());
            fw.close();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    /**
     * 将每个具体的四元式转换成字节码
     *
     * @param line
     */
    private static void sourceToBytecode(String line) {
        String strAry[] = line.split(",");
        switch (strAry[0]) {
            case "(ADD": {
                strBufBytecode.append("Ope," + strAry[1] + "\nOpe," + strAry[2] + "\nADD" +
                        "\nSTOR," + strAry[3].substring(0, strAry[3].length() - 1) + "\n");
                break;
            }
            case "(SUB": {
                strBufBytecode.append("Ope," + strAry[1] + "\nOpe," + strAry[2] + "\nSUB" +
                        "\nSTOR," + strAry[3].substring(0, strAry[3].length() - 1) + "\n");
                break;
            }
            case "(MUL": {
                strBufBytecode.append("Ope," + strAry[1] + "\nOpe," + strAry[2] + "\nMUL" +
                        "\nSTOR," + strAry[3].substring(0, strAry[3].length() - 1) + "\n");
                break;
            }
            case "(DIV": {
                strBufBytecode.append("Ope," + strAry[1] + "\nOpe," + strAry[2] + "\nDIV" +
                        "\nSTOR," + strAry[3].substring(0, strAry[3].length() - 1) + "\n");
                break;
            }
        }
    }

    /** 判断输入的源代码是否合法，规定格式为四元式,用逗号隔开 **/
    private static boolean pan(String line) {
        boolean bool = false;
        String strAry[] = line.split(",");
        if (strAry.length == 4) {
            if (strAry[0].equals("(ADD") || strAry[0].equals("(SUB") || strAry[0].equals("(MUL")
                    || strAry[0].equals("(DIV")) {
                bool = true;
            }
        }
        return bool;
    }

    public static boolean isNum(String str){
        for(int i=0;i<str.length();i++){
            char ch = str.charAt(i);
            if(ch < '0' || ch >'9'){
                return false;
            }
        }
        return true;
    }

}
