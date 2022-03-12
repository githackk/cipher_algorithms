package A52;

import java.util.Random;
import java.util.Scanner;

public class A52_cipher {
    public static void main() {
        System.out.println("A5/2");
        Scanner scanner = new Scanner(System.in);
        String text;
        System.out.println("Введите текст: ");
        text = scanner.nextLine();
        A52_cipher a52 = new A52_cipher(text);
        System.out.println("Зашифрованный текст: " + a52.encode());
        System.out.println("Расшифрованный текст: " + a52.decode());
    }

    char[] ctext;//шифр-текст
    char[] text;//открытый текст
    char[] dtext;//расшифрованный текст
    int[] keystream;//ключевой поток

    A52_cipher(String text) {
        this.text = text.toCharArray();
        this.ctext = new char[text.length()];
        this.dtext = new char[text.length()];
        this.keystream = new int[text.length() * 11];
    }

    public String encode() {//функция шифрования
        int[] R1 = new int[19];//регистр R1
        int[] R2 = new int[22];//регистр R2
        int[] R3 = new int[23];//регистр R3
        int[] R4 = new int[17];//регистр R4
        int t;

        initRG(R1);
        initRG(R2);
        initRG(R3);
        initRG(R4);

        int m1, m2, m3, m4;
        for (int i = 0; i < this.keystream.length; i++) {
            m4 = maj(R4[3], R4[7], R4[10]);
            if (R4[10] == m4) {
                t = R1[13] ^ R1[16] ^ R1[17] ^ R1[18];
                for (int j = 18; j > 0; j--) R1[j] = R1[j - 1];
                R1[0] = t;
            }

            if (R4[3] == m4) {
                t = R2[20] ^ R2[21];
                for (int j = 21; j > 0; j--) R2[j] = R2[j - 1];
                R2[0] = t;
            }

            if (R4[7] == m4) {
                t = R3[7] ^ R3[20] ^ R3[21] ^ R3[22];
                for (int j = 22; j > 0; j--) R3[j] = R3[j - 1];
                R3[0] = t;
            }
            m1 = maj(R1[12], R1[14], R1[15]);
            m2 = maj(R2[9], R2[13], R2[16]);
            m3 = maj(R3[13], R3[16], R3[18]);
            this.keystream[i] = R1[18] ^ R2[21] ^ R3[22] ^ m1 ^ m2 ^ m3;

            t = R4[11] ^ R4[16];
            for (int j = 16; j > 0; j--) R4[j] = R4[j - 1];
            R4[0] = t;
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
            int key_tail = i * 11;//хвост задействованного ключа
            for (int j = 0; j < 11; j++) {
                bin += Integer.toString(Integer.parseInt(String.valueOf(cbin.charAt(j))) ^ keystream[key_tail + j]);
            }
            this.dtext[i] = (char) Integer.parseInt(bin, 2);
        }

        return String.valueOf(this.dtext);
    }

    private void initRG(int q[]) {//инициализация регистров R1, R2, R3
        Random rd = new Random();
        for (int i = 0; i < q.length; i++) q[i] = rd.nextInt(2);
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
