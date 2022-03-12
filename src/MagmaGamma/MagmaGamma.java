package MagmaGamma;

import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class MagmaGamma {
    public static void main() {
        System.out.println("Магма в режиме гаммирования");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        MagmaGamma magmaGamma = new MagmaGamma(text);
        System.out.println("Зашифрованный текст:" + magmaGamma.encode());
        System.out.println("Расшифрованный текст:" + magmaGamma.decode());
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
    private String[] K;//разбиение ключа на 32 подключа
    private String IV;
    private int counter;

    MagmaGamma(String text) {
        this.pText = text;
        this.Key = genKey();
        this.K = SplittingKey();
        this.cText = "";
        this.dText = "";
        this.IV = "";
        this.counter = 0;
    }

    public String encode() {// функция зашифрования
        //перевод текста в двоичный код и распределение его в 64-битные блоки
        String bText = toBits(this.pText);
        String[] block64Table;
        if (bText.length() % 64 != 0)
            block64Table = new String[bText.length() / 64 + 1];
        else
            block64Table = new String[bText.length() / 64];
        for (int i = 0; i < block64Table.length; i++) {
            if (i == block64Table.length - 1) {
                block64Table[i] = bText.substring(i * 64, i * 64 + bText.length() % 64);
                break;
            }
            block64Table[i] = bText.substring(i * 64, i * 64 + 64);
        }

        String gamma;
        //сложение гаммы с открытым текстом
        String result = "";
        for (int i = 0; i < block64Table.length; i++) {
            String IV = InitIV();
            String IVBin = "";
            for (int j = 0; j < IV.length(); j++) {
                String sub = Integer.toBinaryString(Integer.parseInt(String.valueOf(IV.charAt(j)), 16));
                while (sub.length() < 4)
                    sub = '0' + sub;
                IVBin += sub;
            }
            gamma = encodeBlock64(IVBin);
            result += xorBits(block64Table[i], gamma);
        }

        //перевод двоичного кода в алфавитное представление
        int point = 0;
        while (point < result.length()) {
            this.cText += (char) Integer.parseInt(result.substring(point, point + 11), 2);
            point += 11;
        }
        this.counter = 0;
        return this.cText;
    }

    private String encodeBlock64(String block64) {//функция зашифрования 64-битного блока сетью Фейстеля
        String L = block64.substring(0, 32);
        String R = block64.substring(32);
        for (int j = 0; j < 32; j++) {
            String temp = R;
            R = xorBits(F(R, this.K[j]), L);
            L = temp;
        }
        return R + L;
    }

    public String decode() {// функция расшифрования
        //перевод текста в блоки по 64 бита
        String bText = toBitsCipher(this.cText);
        String[] block64Table;
        if (bText.length() % 64 != 0)
            block64Table = new String[bText.length() / 64 + 1];
        else
            block64Table = new String[bText.length() / 64];
        for (int i = 0; i < block64Table.length; i++) {
            if (i == block64Table.length - 1) {
                block64Table[i] = bText.substring(i * 64, i * 64 + bText.length() % 64);
                break;
            }
            block64Table[i] = bText.substring(i * 64, i * 64 + 64);
        }

        String gamma;
        //сложение гаммы с закрытым текстом
        String result = "";
        for (int i = 0; i < block64Table.length; i++) {
            String IV = InitIV();
            String IVBin = "";
            for (int j = 0; j < IV.length(); j++) {
                String sub = Integer.toBinaryString(Integer.parseInt(String.valueOf(IV.charAt(j)), 16));
                while (sub.length() < 4)
                    sub = '0' + sub;
                IVBin += sub;
            }
            gamma = encodeBlock64(IVBin);
            result += xorBits(block64Table[i], gamma);
        }

        //перевод двоичного кода в алфавитное представление
        int point = 0;
        while (point < result.length()) {
            this.dText += (char) Integer.parseInt(result.substring(point, point + 11), 2);
            point += 11;
        }
        this.counter = 0;
        return this.dText;
    }

    private String[] SplittingKey() {//получение 32-х ключей
        String[] splittingKey = new String[8];
        String[] K = new String[32];
        for (int i = 0; i < 8; i++) {
            splittingKey[i] = this.Key.substring(i * 32, i * 32 + 32);
        }
        for (int i = 0; i < 24; i++) {
            K[i] = splittingKey[i % 8];
        }
        int index = 24;
        for (int i = 7; i >= 0; i--) {
            K[index] = splittingKey[i];
            index++;
        }
        return K;
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
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(i))
                result += '0';
            else
                result += '1';
        }
        return result;
    }

    private String InitIV() {
        Random rd = new Random();
        String iv="";
        if (this.IV=="") {
            for (int i = 0; i < 32; i++)
                iv += rd.nextInt(2);
            this.IV = new BigInteger(iv, 2).toString(16);
        }
        iv = this.IV + "00000000";
        BigInteger IVbgint = new BigInteger(iv, 16).add(BigInteger.valueOf(this.counter));
        iv = IVbgint.toString(16);
        this.counter++;
        return iv;
    }

    private String toBits(String text) {// функция перевода текста в биты, количество бит кратно 64
        String bits = "";
        for (int i = 0; i < text.length(); i++) {
            String symbol2 = Integer.toBinaryString(text.charAt(i));
            while (symbol2.length() < 11)
                symbol2 = '0' + symbol2;
            bits += symbol2;
        }
        return bits;
    }

    private String toBitsCipher(String text) {// функция перевода текста в биты, количество бит кратно 64
        String bits = "";
        for (int i = 0; i < text.length(); i++) {
            String symbol2 = Integer.toBinaryString(text.charAt(i));
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
