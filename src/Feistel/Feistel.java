package Feistel;


import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class Feistel {
    public static void main() {
        System.out.println("Сеть Фейстеля");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        Feistel feistel = new Feistel(text);
        System.out.println("Зашифрованный текст:" + feistel.encode());
        System.out.println("Расшифрованный текст:" + feistel.decode());
    }

    final String[][] S = {
            {"12", "4", "6", "2", "10", "5", "11", "9", "14", "8", "13", "7", "0", "3", "15", "1"},
            {"6", "8", "2", "3", "9", "10", "5", "12", "1", "14", "4", "7", "11", "13", "0", "15"},
            {"11", "3", "5", "8", "2", "15", "10", "13", "14", "1", "7", "4", "12", "9", "6", "0"},
            {"12", "8", "2", "1", "13", "4", "15", "6", "7", "0", "10", "5", "3", "14", "9", "11"},
            {"7", "15", "5", "10", "8", "1", "6", "13", "0", "9", "3", "14", "11", "4", "2", "12"},
            {"5", "13", "15", "6", "9", "2", "12", "10", "11", "7", "8", "1", "4", "3", "14", "0"},
            {"8", "14", "2", "5", "6", "9", "1", "12", "15", "4", "11", "0", "13", "10", "3", "7"},
            {"1", "7", "14", "13", "0", "5", "8", "3", "4", "15", "10", "6", "9", "12", "11", "2"}};

    private String pText;// открытый текст
    private String cText;// закрытый текст
    private String dText;// расшифрованный текст
    private String Key;// ключ 256 бит

    Feistel(String text) {
        this.pText = text;
        this.Key = genKey();
        this.cText = "";
        this.dText = "";
    }

    public String encode() {// функция зашифрования
        //разбиение 256 бит основного ключа на 8 32-битных подключей
        String[] K = new String[8];
        for (int i = 0; i < 8; i++) {
            K[i] = this.Key.substring(i * 32, i * 32 + 32);
        }
        //перевод текста в двоичный код и распределение его в 64-битные блоки
        String bText = toBits(this.pText);
        String[] block64Table = new String[bText.length() / 64];
        for (int i = 0; i < block64Table.length; i++) {
            block64Table[i] = bText.substring(i * 64, i * 64 + 64);
        }

        //сеть Фейстеля
        String result = "";
        for (int i = 0; i < block64Table.length; i++) {
            String L = block64Table[i].substring(0, 32);
            String R = block64Table[i].substring(32);
            for (int j = 0; j < 8; j++) {
                String temp = R;
                R = xorBits(F(R, K[j]), L);
                L = temp;
            }
            result += R + L;
        }

        //перевод двоичного кода в алфавитное представление
        int point = 0;
        while (point < result.length()) {
            if (point + 11 > result.length())
                this.cText += (char) Integer.parseInt(result.substring(point, result.length()), 2);
            else
                this.cText += (char) Integer.parseInt(result.substring(point, point + 11), 2);
            point += 11;
        }

        return this.cText;
    }

    public String decode() {// функция расшифрования
        String[] K = new String[8];
        for (int i = 0; i < 8; i++) {
            K[i] = this.Key.substring(i * 32, i * 32 + 32);
        }

        String bText = toBitsCipher(this.cText);
        String[] block64Table = new String[bText.length() / 64];
        for (int i = 0; i < block64Table.length; i++) {
            block64Table[i] = bText.substring(i * 64, i * 64 + 64);
        }

        String result = "";
        for (int i = 0; i < block64Table.length; i++) {
            String L = block64Table[i].substring(0, 32);
            String R = block64Table[i].substring(32);
            String R_1 = "";
            L = xorBits(L, F(R, K[7]));
            for (int j = 6; j >= 0; j--) {
                R_1 = L;
                L = xorBits(R, F(R_1, K[j]));
                R = R_1;
            }
            result += L + R_1;
        }

        int point = 0;
        while (point < result.length()) {
            if (point + 11 > result.length())
                this.dText += (char) Integer.parseInt(result.substring(point, result.length()), 2);
            else
                this.dText += (char) Integer.parseInt(result.substring(point, point + 11), 2);
            point += 11;
        }


        return this.dText;
    }

    private String F(String L, String K) {//функция F в сети Фейстеля
        BigInteger l = new BigInteger(L, 2);//левая часть
        BigInteger k = new BigInteger(K, 2);//ключ
        BigInteger mod = BigInteger.valueOf(2).pow(32);//модуль 2^32
        BigInteger sum = l.add(k).mod(mod);//суммирование l с ключом k по модулю 2^32
        String result = sum.toString(2);//приведение полученного числа в двоичный формат
        while (result.length() != 32)//приведение к 32-битному представлению
            result = '0' + result;

        //----------------S-подстановки-------------------------
        String[] s8 = new String[8];
        for (int i = 0; i < 8; i++)
            s8[i] = result.substring(i * 4, i * 4 + 4);
        result = "";
        int index = 7;
        for (int i = 0; i < 8; i++) {
            String str = S[index][Integer.parseInt(s8[i], 2)];
            String b = Integer.toBinaryString(Integer.valueOf(str));
            while (b.length() < 4)
                b = '0' + b;
            result += b;
            index--;
        }

        //-------циклический сдвиг влево на 11 битов------
        String result1 = "";
        for (int i = 11; i < result.length(); i++) {
            result1 += result.charAt(i);
        }
        for (int i = 0; i < 11; i++) {
            result1 += result.charAt(i);
        }

        return result1;
    }

    private String xorBits(String a, String b) {//операция xor двух переменных string
        String result = "";
        for (int i = 0; i < 32; i++) {
            if (a.charAt(i) == b.charAt(i))
                result += '0';
            else
                result += '1';
        }
        return result;
    }

    private String toBits(String text) {// функция перевода текста в биты, количество бит кратно 64
        String bits = "";
        for (int i = 0; i < text.length(); i++) {
            String symbol2 = Integer.toBinaryString(text.charAt(i));
            while (symbol2.length() < 11)
                symbol2 = '0' + symbol2;
            bits += symbol2;
        }
        while (bits.length() % 64 != 0)
            bits += '0';
        return bits;
    }

    private String toBitsCipher(String text) {// функция перевода текста в биты, количество бит кратно 64
        String bits = "";
        for (int i = 0; i < text.length(); i++) {
            String symbol2 = Integer.toBinaryString(text.charAt(i));
            if (i == text.length() - 1) {
                while ((64 - bits.length() % 64) - symbol2.length() != 0)
                    symbol2 = '0' + symbol2;
                bits += symbol2;
                break;
            }
            while (symbol2.length() < 11)
                symbol2 = '0' + symbol2;
            bits += symbol2;
        }

        return bits;
    }

    private String genKey() {// генерация первичного ключа в двоичной СС
        Random rd = new Random();
        String key = "";
        for (int i = 0; i < 256; i++) key += rd.nextInt(2);
        return key;
    }
}
