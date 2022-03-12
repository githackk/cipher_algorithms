package A51;

import java.util.Random;
import java.util.Scanner;

public class A51_cipher {
    public static void main() {
        System.out.println("A5/1");
        Scanner scanner = new Scanner(System.in);
        String text;
        System.out.println("Введите текст: ");
        text = scanner.nextLine();
        A51_cipher a51 = new A51_cipher(text);
        System.out.println("Зашифрованный текст: " + a51.encode());
        System.out.println("Расшифрованный текст: " + a51.decode());
    }

    char[] ctext;//шифр-текст
    char[] text;//открытый текст
    char[] dtext;//расшифрованный текст
    int[] keystream;//ключевой поток

    A51_cipher(String text) {
        this.text = text.toCharArray();
        this.ctext = new char[text.length()];
        this.dtext = new char[text.length()];
        this.keystream = new int[text.length() * 11];
    }

    public String encode() {//функция шифрования
        int[] x = new int[19];//регистр R1
        int[] y = new int[22];//регистр R2
        int[] z = new int[23];//регистр R3
        int m, t;

        initRG(x, y, z);

        for (int i = 0; i < this.keystream.length; i++) {
            m = maj(x[8], y[10], z[10]);
            if (x[8] == m) {
                t = x[13] ^ x[16] ^ x[17] ^ x[18];
                for (int j = 18; j > 0; j--) x[j] = x[j - 1];
                x[0] = t;
            }

            if (y[10] == m) {
                t = y[20] ^ y[21];
                for (int j = 21; j > 0; j--) y[j] = y[j - 1];
                y[0] = t;
            }

            if (z[10] == m) {
                t = z[7] ^ z[20] ^ z[21] ^ z[22];
                for (int j = 22; j > 0; j--) z[j] = z[j - 1];
                z[0] = t;
            }
            this.keystream[i] = x[18] ^ y[21] ^ z[22];
        }

        int tmp = 0;
        for (int i = 0; i < this.text.length; i++) {
            tmp = (int) (this.text[i]);//оцифровка ascii
            String bin = Integer.toBinaryString(tmp);//перевод в двоичное число

            if (bin.length() < 11) {//если двоичное представление числа меньше 11 бит, то добавляем в начало нули
                while (bin.length() < 11) {
                    bin = "0" + bin;
                }
            }

            String cbin = "";//двоичный шифр
            int key_tail = i * 11;
            for (int j = 0; j < 11; j++) {
                cbin += Integer.toString(Integer.parseInt(String.valueOf(bin.charAt(j))) ^ keystream[key_tail + j]);
            }
            this.ctext[i] = (char) Integer.parseInt(cbin, 2);
        }

        System.out.print("Ключевой поток: ");
        for (int i = 0; i < this.text.length * 11; i++) System.out.print(this.keystream[i]);
        System.out.println();

        return String.valueOf(this.ctext);
    }

    public String decode() {//функция расшифрования
        int tmp = 0;
        for (int i = 0; i < this.text.length; i++) {
            tmp = (int) (this.ctext[i]);//оцифровка ascii
            String cbin = Integer.toBinaryString(tmp);//перевод в двоичное число

            if (cbin.length() < 11) {//если двоичное представление числа меньше 11 бит, то добавляем в начало нули
                while (cbin.length() < 11) {
                    cbin = "0" + cbin;
                }
            }

            String bin = "";//двоичный текст
            int key_tail = i * 11;
            for (int j = 0; j < 11; j++) {
                bin += Integer.toString(Integer.parseInt(String.valueOf(cbin.charAt(j))) ^ keystream[key_tail + j]);
            }
            this.dtext[i] = (char) Integer.parseInt(bin, 2);
        }

        return String.valueOf(this.dtext);
    }

    private void initRG(int x[], int y[], int z[]) {//инициализация регистров R1, R2, R3
        Random rd = new Random();
        for (int i = 0; i < 19; i++) x[i] = rd.nextInt(2);
        for (int i = 0; i < 22; i++) y[i] = rd.nextInt(2);
        for (int i = 0; i < 23; i++) z[i] = rd.nextInt(2);
        System.out.print("x :");
        for (int i = 0; i < 19; i++) System.out.print(x[i]);
        System.out.println("");
        System.out.print("y :");
        for (int i = 0; i < 22; i++) System.out.print(y[i]);
        System.out.println("");
        System.out.print("z :");
        for (int i = 0; i < 23; i++) System.out.print(z[i]);
        System.out.println("");
    }

    private int maj(int x, int y, int z) {//функция majority
        int cnt0 = 0, cnt1 = 0;

        if (x == 0) cnt0 += 1;
        else cnt1 += 1;
        if (y == 0) cnt0 += 1;
        else cnt1 += 1;
        if (z == 0) cnt0 += 1;
        else cnt1 += 1;

        if (cnt0 > cnt1) return 0;
        else return 1;
    }
}



