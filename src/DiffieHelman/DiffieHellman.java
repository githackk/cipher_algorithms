package DiffieHelman;

import java.math.BigInteger;
import java.util.Scanner;

public class DiffieHellman {
    public static void main() {
        System.out.println("Диффи-Хеллман");
        DiffieHellman diffieHellman = new DiffieHellman(5, 29);
        System.out.println("Обмен ключами по Диффи-Хеллману");
        diffieHellman.exchange();
    }

    private int a;// общее число
    private int n;// общее число
    private int Ka;// секретный ключ стороны А
    private int Kb;// секретный ключ стороны B
    private int Ya;// публичный ключ стороны А
    private int Yb;// публичный ключ стороны В
    private int K;// общий ключ сторон

    DiffieHellman(int a, int n) {
        this.a = a;
        this.n = n;
    }

    public void exchange() {
        System.out.println("a = " + this.a + " n = " + this.n);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Сторона А! Введите свой секретный ключ (1<k<" + n + "):");
        this.Ka = scanner.nextInt();
        while (this.Ka < 2 || this.Ka > n - 1) {
            System.out.println("Секретный ключ не входит в интервал! Секретный ключ стороны А:");
            this.Ka = scanner.nextInt();
        }
        System.out.println("Сторона B! Введите свой секретный ключ (1<k<" + n + "):");
        this.Kb = scanner.nextInt();
        while (this.Kb < 2 || this.Kb > n - 1) {
            System.out.println("Секретный ключ не входит в интервал! Секретный ключ стороны B:");
            this.Ka = scanner.nextInt();
        }
        BigInteger b = BigInteger.valueOf(this.a).modPow(BigInteger.valueOf(this.Ka), BigInteger.valueOf(this.n));
        this.Ya = Integer.parseInt(String.valueOf(b));
        b = BigInteger.valueOf(this.a).modPow(BigInteger.valueOf(this.Kb), BigInteger.valueOf(this.n));
        this.Yb = Integer.parseInt(String.valueOf(b));
        System.out.println("Открытые ключи сторон: Ya = " + this.Ya + " Yb = " + this.Yb);

        b = BigInteger.valueOf(this.Yb).modPow(BigInteger.valueOf(this.Ka), BigInteger.valueOf(this.n));
        int Ka = Integer.parseInt(String.valueOf(b));
        System.out.println("Общий ключ у стороны А равен: " + Ka);
        b = BigInteger.valueOf(this.Ya).modPow(BigInteger.valueOf(this.Kb), BigInteger.valueOf(this.n));
        int Kb = Integer.parseInt(String.valueOf(b));
        System.out.println("Общий ключ у стороны В равен: " + Kb);
        if (Ka == Kb) {
            this.K = Ka;
            System.out.println("Общий ключ успешно найден! Общий ключ равен: " + this.K);
        }
        else{
            System.out.println("Общий ключ не найден!");
        }
    }
}
