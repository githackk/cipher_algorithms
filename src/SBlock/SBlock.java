package SBlock;

import java.util.Scanner;

public class SBlock {
    public static void main() {
        System.out.println("S-Block");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        SBlock sBlock = new SBlock(text);
        System.out.println("Зашифрованный текст:" + sBlock.encode());
        System.out.println("Расшифрованный текст:" + sBlock.decode());
    }

    final String[][] SUBSTITUTE_BOX = {
            {"C", "4", "6", "2", "A", "5", "B", "9", "E", "8", "D", "7", "0", "3", "F", "1"},
            {"6", "8", "2", "3", "9", "A", "5", "C", "1", "E", "4", "7", "B", "D", "0", "F"},
            {"B", "3", "5", "8", "2", "F", "A", "D", "E", "1", "7", "4", "C", "9", "6", "0"},
            {"C", "8", "2", "1", "D", "4", "F", "6", "7", "0", "A", "5", "3", "E", "9", "B"},
            {"7", "F", "5", "A", "8", "1", "6", "D", "0", "9", "3", "E", "B", "4", "2", "C"},
            {"5", "D", "F", "6", "9", "2", "C", "A", "B", "7", "8", "1", "4", "3", "E", "0"},
            {"8", "E", "2", "5", "6", "9", "1", "C", "F", "4", "B", "0", "D", "A", "3", "7"},
            {"1", "7", "E", "D", "0", "5", "8", "3", "4", "F", "A", "6", "9", "C", "B", "2"}};

    char[] ptext;//открытый текст
    char[] ctext;//закрытый текст
    char[] dtext;//расшифрованный текст

    SBlock(String text) {
        this.ptext = text.toCharArray();
    }

    public String encode() {
        String btext = toBin(this.ptext);// открытый текст в виде двоичного кода
        String bincText = substitution(btext);// переменная, хранящая текст в двоичном представлении после подстановки
        return String.valueOf(this.ctext = toText(bincText).toCharArray());
    }

    public String decode() {
        String ctext = toBin(this.ctext);// закрытый текст в виде двоичного представления
        String bindText = invSubstitution(ctext);// дешифрованный текст в виде двоичного представления
        return String.valueOf(this.dtext = toText(bindText).toCharArray());
    }

    private String toBin(char[] text) {// функция перевода из алфавитного представления в двоичный код
        String btext = "";// открытый текст в виде двоичного кода
        String bin;// символ в двоичном коде
        for (int i = 0; i < text.length; i++) {// преобразование в двоичный код-строку
            bin = Integer.toBinaryString((int) text[i]);
            while (bin.length() < 11) {
                bin = '0' + bin;
            }
            btext += bin;
        }
        return btext;
    }

    private String substitution(String btext) {// функция подстановки
        String bincText = "";// переменная, хранящая текст в двоичном представлении после подстановки
        int point = 0;
        while (point < btext.length()) {
            String bits32;
            if (point + 32 > btext.length()) {
                bits32 = btext.substring(point, btext.length());
                while (bits32.length() < 32)
                    bits32 += '0';
                point += 32;
            } else {
                bits32 = btext.substring(point, point + 32);
                point += 32;
            }

            String[] bits4 = new String[8];
            for (int i = 0; i < 8; i++) {
                bits4[i] = bits32.substring(i * 4, i * 4 + 4);
                String bincTextcell = Integer.toBinaryString(Integer.parseInt(SUBSTITUTE_BOX[i][Integer.parseInt(bits4[i], 2)], 16));
                while (bincTextcell.length() < 4)
                    bincTextcell = '0' + bincTextcell;
                bincText += bincTextcell;
            }
        }
        return bincText;
    }

    private String invSubstitution(String btext){// функция обратной подстановки
        String bindText = "";// переменная, хранящая текст в двоичном представлении после подстановки
        int point = 0;
        while (point < btext.length()) {
            String bits32;
            if (point + 32 > btext.length()) {
                bits32 = btext.substring(point, btext.length());
                while (bits32.length() < 32)
                    bits32 += '0';
                point += 32;
            } else {
                bits32 = btext.substring(point, point + 32);
                point += 32;
            }

            String[] bits4 = new String[8];
            for (int i = 0; i < 8; i++) {
                bits4[i] = bits32.substring(i * 4, i * 4 + 4);
                String hex = Integer.toHexString(Integer.parseInt(bits4[i],2)).toUpperCase();
                for (int j = 0; j<16;j++){
                    if (hex.equals(SUBSTITUTE_BOX[i][j])){
                        String bindTextcell = Integer.toBinaryString(j);
                        while (bindTextcell.length()<4)
                            bindTextcell = '0' + bindTextcell;
                        bindText += bindTextcell;
                        break;
                    }
                }
            }
        }
        return bindText;
    }

    private String toText(String bincText) {// функция перевода текста из двоичного представления в алфавитный
        int point = 0;
        String text = "";
        while (point < bincText.length()) {
            if (point + 11 > bincText.length()) {
                text += (char) Integer.parseInt(bincText.substring(point, bincText.length()), 2);
                point+=11;
            } else {
                text += (char) Integer.parseInt(bincText.substring(point, point + 11), 2);
                point += 11;
            }
        }
        return text;
    }
}
