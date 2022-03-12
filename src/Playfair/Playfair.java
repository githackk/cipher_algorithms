package Playfair;

import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;

public class Playfair {
    public static void main() {
        System.out.println("Плэйфер");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        Playfair playfair = new Playfair(text);
        System.out.println("Зашифрованный текст:" + playfair.encode());
        System.out.println("Расшифрованный текст:" + playfair.decode());
    }

    public static char alphabet[] = new char[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З',
            'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р',
            'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш',
            'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    private String keyWord;//ключ-слово
    private char[][] keyTable = new char[8][4];//ключ-таблица на основе ключ-слова
    private String pText;//открытый текст
    private String cText;//закрытый текст
    private String dText;//расшифрованный текст

    Playfair(String text) {
        this.pText = format(text);
        this.cText = "";
        this.dText = "";
        this.keyWord = getKey();
        int count = 0;
        while (count < keyWord.length()) {
            this.keyTable[count / 4][count % 4] = this.keyWord.charAt(count);
            count++;
        }
        for (int i = 0; i < alphabet.length; i++) {
            if (this.keyWord.indexOf(alphabet[i]) != -1)
                continue;
            this.keyTable[count / 4][count % 4] = alphabet[i];
            count++;
        }
    }

    public String encode() {//зашифрование текста
        String[] bigrams = Bigramming(this.pText);
        for (int i = 0; i < bigrams.length; i++) {
            Point a = findEl(bigrams[i].substring(0, 1).charAt(0));
            Point b = findEl(bigrams[i].substring(1).charAt(0));
            Point c = new Point();
            Point d = new Point();
            if (a.x == b.x) {
                if (a.y + 1 == 4) {
                    c.x = a.x;
                    c.y = 0;
                } else {
                    c.x = a.x;
                    c.y = a.y + 1;
                }
                if (b.y + 1 == 4) {
                    d.x = b.x;
                    d.y = 0;
                } else {
                    d.x = b.x;
                    d.y = b.y + 1;
                }
            } else if (a.y == b.y) {
                if (a.x + 1 == 8) {
                    c.y = a.y;
                    c.x = 0;
                } else {
                    c.y = a.y;
                    c.x = a.x + 1;
                }
                if (b.x + 1 == 8) {
                    d.y = b.y;
                    d.x = 0;
                } else {
                    d.y = b.y;
                    d.x = b.x + 1;
                }
            } else {
                d.y = a.y;
                d.x = b.x;
                c.x = a.x;
                c.y = b.y;
            }
            this.cText += this.keyTable[c.x][c.y];
            this.cText += this.keyTable[d.x][d.y];
        }
        return this.cText;
    }

    public String decode() {//расшифрование текста
        String[] bigrams = Bigramming(this.cText);
        for (int i = 0; i < bigrams.length; i++) {
            Point a = findEl(bigrams[i].substring(0, 1).charAt(0));
            Point b = findEl(bigrams[i].substring(1).charAt(0));
            Point c = new Point();
            Point d = new Point();
            if (a.x == b.x) {
                if (a.y == 0) {
                    c.x = a.x;
                    c.y = 3;
                } else {
                    c.x = a.x;
                    c.y = a.y - 1;
                }
                if (b.y == 0) {
                    d.x = b.x;
                    d.y = 3;
                } else {
                    d.x = b.x;
                    d.y = b.y - 1;
                }
            } else if (a.y == b.y) {
                if (a.x == 0) {
                    c.y = a.y;
                    c.x = 7;
                } else {
                    c.y = a.y;
                    c.x = a.x - 1;
                }
                if (b.x == 0) {
                    d.y = b.y;
                    d.x = 7;
                } else {
                    d.y = b.y;
                    d.x = b.x - 1;
                }
            } else {
                d.y = a.y;
                d.x = b.x;
                c.x = a.x;
                c.y = b.y;
            }
            this.dText += this.keyTable[c.x][c.y];
            this.dText += this.keyTable[d.x][d.y];
        }
        return this.dText;
    }

    private Point findEl(char el) {//поиск индексов элемента в ключ-таблице
        Point point = new Point();
        for (int i = 0; i < this.keyTable.length; i++) {
            for (int j = 0; j < this.keyTable[0].length; j++) {
                if (keyTable[i][j] == el) {
                    point.x = i;
                    point.y = j;
                    return point;
                }
            }
        }
        return point;
    }

    private String[] Bigramming(String text) {//разделение текста на биграммы
        int length_text = text.length();
        int count_bigrams = 0;
        String[] result = new String[length_text / 2 + length_text % 2];
        for (int i = 0; i < text.length(); i += 2) {
            if (i + 1 == text.length()) {
                result[count_bigrams] = String.valueOf(text.charAt(i));
                result[count_bigrams] += "Ъ";
                count_bigrams++;
            } else if (text.charAt(i) == text.charAt(i + 1)) {
                result[count_bigrams] = String.valueOf(text.charAt(i));
                result[count_bigrams] += "Ъ";

                length_text++;
                count_bigrams++;
                result = Arrays.copyOf(result, length_text / 2 + length_text % 2);
                i--;
            } else {
                result[count_bigrams] = String.valueOf(text.charAt(i));
                result[count_bigrams] += String.valueOf(text.charAt(i + 1));
                count_bigrams++;
            }
        }
        return result;
    }

    private String getKey() {// запрос ключ-слова и проверка на совпадающие буквы в нем
        String key;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ключ-слово (буквы в слове не должны повторяться): ");
        key = format(scanner.nextLine());
        for (int i = 0; i < key.length(); i++) {
            int counter = 0;
            for (int j = 0; j < key.length(); j++) {
                if (key.charAt(i) == key.charAt(j))
                    counter++;
            }
            if (counter > 1) {
                System.out.println("В слове не должно быть повторяющихся букв!");
                return getKey();
            }
        }
        return key;
    }

    private String format(String text) {//функция приведения текста к специальному формату
        text = text.toUpperCase();
        text = text.replaceAll(" ", "");
        text = text.replaceAll(",", "ЗПТ");
        text = text.replaceAll("-", "ТИРЕ");
        text = text.replaceAll("«", "ОКАВЫЧКИ");
        text = text.replaceAll("»", "ЗКАВЫЧКИ");
        text = text.replaceAll("\\?", "ВОПРОС");
        text = text.replaceAll("\\.", "ТЧК");
        text = text.replaceAll("Ё", "Е");
        text = text.replaceAll("0", "НОЛЬ");
        text = text.replaceAll("1", "ОДИН");
        text = text.replaceAll("2", "ДВА");
        text = text.replaceAll("3", "ТРИ");
        text = text.replaceAll("4", "ЧЕТЫРЕ");
        text = text.replaceAll("5", "ПЯТЬ");
        text = text.replaceAll("6", "ШЕСТЬ");
        text = text.replaceAll("7", "СЕМЬ");
        text = text.replaceAll("8", "ВОСЕМЬ");
        text = text.replaceAll("9", "ДЕВЯТЬ");

        return text;
    }
}
