import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        int[] a = {1, 2};
        out: for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(i + " " + j);
                if (j == 1) {
                    continue out;
                }
                System.out.println("continue");
            }
        }
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            boolean a = true, b = false;
            int i = 0;
            while (!isInterrupted() && i < Integer.MAX_VALUE) {
                System.out.println(i + " while循环");
                i++;
            }
        }
    }
}