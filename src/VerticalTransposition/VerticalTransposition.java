package VerticalTransposition;

import java.util.Scanner;

public class VerticalTransposition {
    public static void main() {
        System.out.println("Вертикальная перестановка");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ключ-слово:");
        String key = scanner.nextLine();
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        VerticalTransposition verticalTransposition = new VerticalTransposition(text, key);
        System.out.println("Зашифрованный текст:" + verticalTransposition.encode());
        System.out.println("Расшифрованный текст:" + verticalTransposition.decode());
    }

    public static char alphabet[] = new char[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З',
            'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р',
            'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш',
            'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    private String pText;// открытый текст
    private String cText;// закрытый текст
    private String dText;// расшифрованный текст
    private String Key;// ключ

    VerticalTransposition(String text, String key) {
        this.pText = format(text);
        this.Key = format(key);
        this.cText = "";
        this.dText = "";
    }

    public String encode() {// функция шифрования
        int[] digitalKey = todigitalKey();
        char[][] textTable = getTextTable(this.pText);
        for (int i = 0; i < digitalKey.length; i++) {
            int index = 0;
            for (int j = 0; i < digitalKey.length; j++) {
                if (i == digitalKey[j]) {
                    index = j;
                    break;
                }
            }
            for (int j = 0; j < textTable.length; j++) {
                if ((int) textTable[j][index] == 0)
                    continue;
                this.cText += textTable[j][index];
            }
        }
        return this.cText;
    }

    public String decode() {// функция расшифрования
        int rows;
        int mod = 0;
        int[] digitalKey = todigitalKey();
        if (this.cText.length() % this.Key.length() > 0) {
            rows = this.cText.length() / this.Key.length() + 1;
            mod = this.cText.length() % this.Key.length();
        } else
            rows = this.cText.length() / this.Key.length();

        char[][] textTable = new char[rows][this.Key.length()];

        int counter = 0;
        for (int i = 0; i < textTable[0].length; i++) {
            int index = 0;
            for (int j = 0; j < digitalKey.length; j++) {
                if (i == digitalKey[j]) {
                    index = j;
                    break;
                }
            }
            if (index >= mod) {
                for (int j = 0; j < textTable.length - 1; j++) {
                    textTable[j][index] = this.cText.charAt(counter);
                    counter++;
                }
            }
            else {
                for (int j = 0; j < textTable.length; j++) {
                    textTable[j][index] = this.cText.charAt(counter);
                    counter++;
                }
            }
        }
        for (int i = 0; i < textTable.length; i++) {
            for (int j = 0; j < textTable[0].length; j++) {
                if ((int) textTable[i][j] == 0)
                    break;
                this.dText += textTable[i][j];
            }
        }
        return this.dText;
    }

    private char[][] getTextTable(String text) {// перевод символов открытого текста в табличный вид
        int rows;
        if (text.length() % this.Key.length() > 0)
            rows = text.length() / this.Key.length() + 1;
        else
            rows = text.length() / this.Key.length();
        char[][] textTable = new char[rows][this.Key.length()];
        for (int i = 0; i < textTable.length; i++) {
            if (i == textTable.length - 1) {
                for (int j = 0; j < text.length() - i * textTable[0].length; j++) {
                    textTable[i][j] = text.charAt(i * textTable[0].length + j);
                }
                break;
            }
            for (int j = 0; j < textTable[0].length; j++) {
                textTable[i][j] = text.charAt(i * textTable[0].length + j);
            }
        }
        return textTable;
    }

    private int[] todigitalKey() {// приведение ключа в цифровой формат согласно порядку алфавита
        int[] digital = new int[this.Key.length()];//переменная с оцифрованным ключом согласно алфавиту
        int[] digitalKey = new int[this.Key.length()];
        for (int i = 0; i < this.Key.length(); i++) {
            digital[i] = new String(alphabet).indexOf(this.Key.charAt(i));
        }

        int index = -1;
        for (int i = 0; i < digital.length; i++) {
            int min = 32;
            for (int j = 0; j < digital.length; j++) {
                if (digital[j] != -1 && digital[j] < min) {
                    min = digital[j];
                    index = j;
                }
            }
            digital[index] = -1;
            digitalKey[index] = i;
        }
        return digitalKey;
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
