package Kuznechik;

import java.util.Random;
import java.util.Scanner;

public class Kuznechik {
    public static void main() {
        System.out.println("Кузнечик");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        Kuznechik kuznechik = new Kuznechik(text);
        System.out.println("Зашифрованный текст:" + kuznechik.encode());
        System.out.println("Расшифрованный текст:" + kuznechik.decode());
    }

    final String[] R = {"1", "148", "32", "133", "16", "194", "192", "1", "251", "1", "192", "194", "16", "133", "32", "148"};
    final String[][] S = {
            {"252", "238", "221", "17", "207", "110", "49", "22", "251", "196", "250", "218", "35", "197", "4", "77"},
            {"233", "119", "240", "219", "147", "46", "153", "186", "23", "54", "241", "187", "20", "205", "95", "193"},
            {"249", "24", "101", "90", "226", "92", "239", "33", "129", "28", "60", "66", "139", "1", "142", "79"},
            {"5", "132", "2", "174", "227", "106", "143", "160", "6", "11", "237", "152", "127", "212", "211", "31"},
            {"235", "52", "44", "81", "234", "200", "72", "171", "242", "42", "104", "162", "253", "58", "206", "204"},
            {"181", "112", "14", "86", "8", "12", "118", "18", "191", "114", "19", "71", "156", "183", "93", "135"},
            {"21", "161", "150", "41", "16", "123", "154", "199", "243", "145", "120", "111", "157", "158", "178", "177"},
            {"50", "117", "25", "61", "255", "53", "138", "126", "109", "84", "198", "128", "195", "189", "13", "87"},
            {"223", "245", "36", "169", "62", "168", "67", "201", "215", "121", "214", "246", "124", "34", "185", "3"},
            {"224", "15", "236", "222", "122", "148", "176", "188", "220", "232", "40", "80", "78", "51", "10", "74"},
            {"167", "151", "96", "115", "30", "0", "98", "68", "26", "184", "56", "130", "100", "159", "38", "65"},
            {"173", "69", "70", "146", "39", "94", "85", "47", "140", "163", "165", "125", "105", "213", "149", "59"},
            {"7", "88", "179", "64", "134", "172", "29", "247", "48", "55", "107", "228", "136", "217", "231", "137"},
            {"225", "27", "131", "73", "76", "63", "248", "254", "141", "83", "170", "144", "202", "216", "133", "97"},
            {"32", "113", "103", "164", "45", "43", "9", "91", "203", "155", "37", "208", "190", "229", "108", "82"},
            {"89", "166", "116", "210", "230", "244", "180", "192", "209", "102", "175", "194", "57", "75", "99", "182"}};

    final String[][] invS = {
            {"165", "45", "50", "143", "14", "48", "56", "192", "84", "230", "158", "57", "85", "126", "82", "145"},
            {"100", "3", "87", "90", "28", "96", "7", "24", "33", "114", "168", "209", "41", "198", "164", "63"},
            {"224", "39", "141", "12", "130", "234", "174", "180", "154", "99", "73", "229", "66", "228", "21", "183"},
            {"200", "6", "112", "157", "65", "117", "25", "201", "170", "252", "77", "191", "42", "115", "132", "213"},
            {"195", "175", "43", "134", "167", "177", "178", "91", "70", "211", "159", "253", "212", "15", "156", "47"},
            {"155", "67", "239", "217", "121", "182", "83", "127", "193", "240", "35", "231", "37", "94", "181", "30"},
            {"162", "223", "166", "254", "172", "34", "249", "226", "74", "188", "53", "202", "238", "120", "5", "107"},
            {"81", "225", "89", "163", "242", "113", "86", "17", "106", "137", "148", "101", "140", "187", "119", "60"},
            {"123", "40", "171", "210", "49", "222", "196", "95", "204", "207", "118", "44", "184", "216", "46", "54"},
            {"219", "105", "179", "20", "149", "190", "98", "161", "59", "22", "102", "233", "92", "108", "109", "173"},
            {"55", "97", "75", "185", "227", "186", "241", "160", "133", "131", "218", "71", "197", "176", "51", "250"},
            {"150", "111", "110", "194", "246", "80", "255", "93", "169", "142", "23", "27", "151", "125", "236", "88"},
            {"247", "31", "251", "124", "9", "13", "122", "103", "69", "135", "220", "232", "79", "29", "78", "4"},
            {"235", "248", "243", "62", "61", "189", "138", "136", "221", "205", "11", "19", "152", "2", "147", "128"},
            {"144", "208", "36", "52", "203", "237", "244", "206", "153", "16", "68", "64", "146", "58", "1", "38"},
            {"18", "26", "72", "104", "245", "129", "139", "199", "214", "32", "10", "8", "0", "76", "215", "116"}};

    final String[][] GF28 = {
            {"1", "2", "4", "8", "16", "32", "64", "128", "195", "69", "138", "215", "109", "218", "119", "238"},
            {"31", "62", "124", "248", "51", "102", "204", "91", "182", "175", "157", "249", "49", "98", "196", "75"},
            {"150", "239", "29", "58", "116", "232", "19", "38", "76", "152", "243", "37", "74", "148", "235", "21"},
            {"42", "84", "168", "147", "229", "9", "18", "36", "72", "144", "227", "5", "10", "20", "40", "80"},
            {"160", "131", "197", "73", "146", "231", "13", "26", "52", "104", "208", "99", "198", "79", "158", "255"},
            {"61", "122", "244", "43", "86", "172", "155", "245", "41", "82", "164", "139", "213", "105", "210", "103"},
            {"206", "95", "190", "191", "189", "185", "177", "161", "129", "193", "65", "130", "199", "77", "154", "247"},
            {"45", "90", "180", "171", "149", "233", "17", "34", "68", "136", "211", "101", "202", "87", "174", "159"},
            {"253", "57", "114", "228", "11", "22", "44", "88", "176", "163", "133", "201", "81", "162", "135", "205"},
            {"89", "178", "167", "141", "217", "113", "226", "7", "14", "28", "56", "112", "224", "3", "6", "12"},
            {"24", "48", "96", "192", "67", "134", "207", "93", "186", "183", "173", "153", "241", "33", "66", "132"},
            {"203", "85", "170", "151", "237", "25", "50", "100", "200", "83", "166", "143", "221", "121", "242", "39"},
            {"78", "156", "251", "53", "106", "212", "107", "214", "111", "222", "127", "254", "63", "126", "252", "59"},
            {"118", "236", "27", "54", "108", "216", "115", "230", "15", "30", "60", "120", "240", "35", "70", "140"},
            {"219", "117", "234", "23", "46", "92", "184", "179", "165", "137", "209", "97", "194", "71", "142", "223"},
            {"125", "250", "55", "110", "220", "123", "246", "47", "94", "188", "187", "181", "169", "145", "225", "1"}};

    private String pText;// открытый текст
    private String cText;// закрытый текст
    private String dText;// расшифрованный текст
    private String Key;// ключ 256 бит

    Kuznechik(String text) {
        this.pText = text;
        this.Key = toHex(genKey());
        this.cText = "";
        this.dText = "";
    }

    public String encode() {
        //генерация констант и расширение 256-битного ключа на 10 128-битных
        String[] C = generateC();
        String[] Keys = KeyExtend(this.Key, C);

        //перевод текста в двоичный код и распределение его в 128-битные блоки
        String bText = toBits(this.pText);
        String[] block128Table = new String[bText.length() / 128];
        for (int i = 0; i < block128Table.length; i++) {
            block128Table[i] = bText.substring(i * 128, i * 128 + 128);
        }

        //шифрование SP сетью
        String result = "";
        for (int i = 0; i < block128Table.length; i++) {
            String subResult = block128Table[i];
            for (int j = 0; j < 9; j++) {
                subResult = X(subResult, toBin(Keys[j]));
                subResult = S(subResult);
                subResult = L(subResult);
                subResult = toBin(subResult);
            }
            result += toBin(X(subResult, toBin(Keys[9])));
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

    public String decode() {
        String[] C = generateC();
        String[] Keys = KeyExtend(this.Key, C);

        String bText = toBitsCipher(this.cText);
        String[] block128Table = new String[bText.length() / 128];
        for (int i = 0; i < block128Table.length; i++) {
            block128Table[i] = bText.substring(i * 128, i * 128 + 128);
        }

        String result = "";
        for (int i = 0; i < block128Table.length; i++) {
            String subResult = X(block128Table[i], toBin(Keys[9]));
            for (int j = 8; j >=0; j--) {
                subResult = invL(subResult);
                subResult = invS(subResult);
                subResult = X(toBin(subResult), toBin(Keys[j]));
            }
            result += toBin(subResult);
        }

        //перевод двоичного кода в алфавитное представление
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

    private String toBits(String text) {// функция перевода текста в биты, количество бит кратно 128
        String bits = "";
        for (int i = 0; i < text.length(); i++) {
            String symbol2 = Integer.toBinaryString(text.charAt(i));
            while (symbol2.length() < 11)
                symbol2 = '0' + symbol2;
            bits += symbol2;
        }
        while (bits.length() % 128 != 0)
            bits += '0';
        return bits;
    }

    private String toBitsCipher(String text) {// функция перевода текста в биты, количество бит кратно 128
        String bits = "";
        for (int i = 0; i < text.length(); i++) {
            String symbol2 = Integer.toBinaryString(text.charAt(i));
            if (i == text.length() - 1) {
                while ((128 - bits.length() % 128) - symbol2.length() != 0)
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

    private String[] generateC() {//генерация констант для расчета итерационных ключей
        String[] C = new String[32];
        for (int i = 0; i < C.length; i++) {
            C[i] = Integer.toHexString(i + 1);
            while (C[i].length() < 32)
                C[i] = '0' + C[i];
            int sum = 0;
            for (int j = 0; j < 15; j++) {
                sum = 0;
                for (int k = 0; k < 16; k++) {
                    int a = Integer.parseInt(R[k]);
                    int b = Integer.parseInt(C[i].substring(k * 2, k * 2 + 2), 16);
                    if (a * b > 255) {
                        sum ^= findVal((findExp(a) + findExp(b)) % 255);
                        continue;
                    }
                    sum ^= a * b;
                }
                String Sum = Integer.toHexString(sum);
                while (Sum.length() < 2)
                    Sum = '0' + Sum;
                C[i] += Sum;
                C[i] = C[i].substring(2);
            }
            String newC = "";
            for (int j = 0; j < 16; j++) {
                String sub = C[i].substring(j * 2, j * 2 + 2);
                newC = sub + newC;
            }
            C[i] = newC;
        }
        return C;
    }

    private String L(String bytes) {//линейное преобразование L
        String newC = "";
        for (int j = 0; j < 16; j++) {
            String sub = bytes.substring(j * 2, j * 2 + 2);
            newC = sub + newC;
        }
        bytes = newC;

        int sum = 0;
        for (int j = 0; j < 16; j++) {
            sum = 0;
            for (int k = 0; k < 16; k++) {
                int a = Integer.parseInt(R[k]);
                int b = Integer.parseInt(bytes.substring(k * 2, k * 2 + 2), 16);
                if (a * b > 255) {
                    sum ^= findVal((findExp(a) + findExp(b)) % 255);
                    continue;
                }
                sum ^= a * b;
            }
            String Sum = Integer.toHexString(sum);
            while (Sum.length() < 2)
                Sum = '0' + Sum;
            bytes += Sum;
            bytes = bytes.substring(2);
        }
        newC = "";
        for (int j = 0; j < 16; j++) {
            String sub = bytes.substring(j * 2, j * 2 + 2);
            newC = sub + newC;
        }
        bytes = newC;
        return bytes;
    }

    private String S(String bytes) {//нелинейное преобразование S
        String result = "";
        for (int i = 0; i < bytes.length() / 2; i++) {
            int sub = Integer.parseInt(bytes.substring(i * 2, i * 2 + 2), 16);
            int div = sub / 16;
            int mod = sub % 16;
            sub = Integer.parseInt(S[div][mod]);
            String str = Integer.toHexString(sub);
            while (str.length() < 2)
                str = '0' + str;
            result += str;
        }
        return result;
    }

    private String invL(String bytes) {//инвертированное линейное преобразование L^-1
        int sum = 0;
        for (int j = 0; j < 16; j++) {
            sum = 0;
            for (int k = 0; k < 16; k++) {
                int a = Integer.parseInt(R[k]);
                int b = Integer.parseInt(bytes.substring(k * 2, k * 2 + 2), 16);
                if (a * b > 255) {
                    sum ^= findVal((findExp(a) + findExp(b)) % 255);
                    continue;
                }
                sum ^= a * b;
            }
            String Sum = Integer.toHexString(sum);
            while (Sum.length() < 2)
                Sum = '0' + Sum;
            bytes += Sum;
            bytes = bytes.substring(2);
        }
        return bytes;
    }

    private String invS(String bytes) {//инвертированное нелинейное преобразование (подстановка) S^-1
        String result = "";
        for (int i = 0; i < bytes.length() / 2; i++) {
            int sub = Integer.parseInt(bytes.substring(i * 2, i * 2 + 2), 16);
            int div = sub / 16;
            int mod = sub % 16;
            sub = Integer.parseInt(invS[div][mod]);
            String str = Integer.toHexString(sub);
            while (str.length() < 2)
                str = '0' + str;
            result += str;
        }
        return result;
    }

    private int findExp(int val) {//поиск степени двойки в поле Галуа
        for (int i = 0; i < GF28.length; i++) {
            for (int j = 0; j < GF28[0].length; j++) {
                if (GF28[i][j].equals(String.valueOf(val)))
                    return i * 16 + j;
            }
        }
        return 0;
    }

    private int findVal(int exp) {//поиск элемента в поле Галуа по степени двойки
        int div = exp / 16;
        int mod = exp % 16;
        return Integer.parseInt(GF28[div][mod]);
    }

    private String[] KeyExtend(String K, String[] C) {//расчет итерационных ключей
        String[] Keys = new String[10];
        String L = K.substring(32);
        String R = K.substring(0, 32);
        Keys[0] = R;
        Keys[1] = L;
        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                String temp = X(toBin(F(R, C[i * 8 - 8 + j])), toBin(L));
                L = R;
                R = temp;
            }
            Keys[i * 2] = R;
            Keys[i * 2 + 1] = L;
        }
        return Keys;
    }

    private String F(String text, String K) {//функция в сети Фейстеля для расчета итерационных ключей
        String Kbits = "";
        String Textbits = "";
        for (int i = 0; i < K.length() / 2; i++) {
            String subK = Integer.toBinaryString(Integer.parseInt(K.substring(i * 2, i * 2 + 2), 16));
            String subText = Integer.toBinaryString(Integer.parseInt(text.substring(i * 2, i * 2 + 2), 16));
            while (subK.length() < 8)
                subK = '0' + subK;
            while (subText.length() < 8)
                subText = '0' + subText;
            Kbits += subK;
            Textbits += subText;
        }

        Textbits = X(Textbits, Kbits);
        Textbits = S(Textbits);
        Textbits = L(Textbits);
        return Textbits;
    }

    private String X(String a, String b) {//операция xor двух переменных в двоичном предстевлении, возврат в hex
        String result = "";
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(i))
                result += '0';
            else
                result += '1';
        }
        return toHex(result);
    }

    private String toHex(String bin) {//перевод двоичного числа в шестнадцатеричное
        String result = "";
        for (int i = 0; i < bin.length() / 8; i++) {
            String sub = Integer.toHexString(Integer.parseInt(bin.substring(i * 8, i * 8 + 8), 2));
            while (sub.length() < 2)
                sub = '0' + sub;
            result += sub;
        }
        return result;
    }

    private String toBin(String hex) {//перевод шестнадцатеричного числа в двоичное
        String result = "";
        for (int i = 0; i < hex.length() / 2; i++) {
            String sub = Integer.toBinaryString(Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16));
            while (sub.length() < 8)
                sub = '0' + sub;
            result += sub;
        }
        return result;
    }

    private String genKey() {// генерация первичного ключа в двоичной СС
        Random rd = new Random();
        String key = "";
        for (int i = 0; i < 256; i++) key += rd.nextInt(2);
        return key;
    }
}
