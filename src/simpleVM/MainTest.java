package simpleVM;

import java.io.IOException;

public class MainTest {

    String str;

    public static void main(String[] args) throws IOException {
        ByteCode code = new ByteCode();
        code.readSourceFile();
        System.out.println(code.getSourceFile());
        code.conver();
        System.out.println("转换成字节码:\n" + code.getBytecode());
        LoadBytecode load = new LoadBytecode(code.getBytecode());
        if (code.getBytecode().equals("error")) {
            System.out.println("请检查源代码！");
        } else {
            load.loadMethod();
            System.out.println("方法区：");
            for (int i = 0; i < load.methodArea.length; i++) {
                if (i % 5 == 0 && i != 0) {
                    System.out.print("\n");
                }
                System.out.print(load.methodArea[i] + "\t");
            }
            System.out.println("\n执行方法区:");
            load.run();
        }
    }
}
