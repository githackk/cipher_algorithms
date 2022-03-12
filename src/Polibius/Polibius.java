package Polibius;

import Cesar.Cesar;

import java.util.Arrays;
import java.util.Scanner;

public class Polibius {
    public static void main() {
        System.out.println("Полибий");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        Polibius polibius = new Polibius(text);
        System.out.println("Зашифрованный текст:" + polibius.encode());
        System.out.println("Расшифрованный текст:" + polibius.decode());
    }

    public static char[][] alphabet = new char[][]{
            {'А', 'Б', 'В', 'Г', 'Д', 'Е'},
            {'Ж', 'З', 'И', 'Й', 'К', 'Л'},
            {'М', 'Н', 'О', 'П', 'Р', 'С'},
            {'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч'},
            {'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э'},
            {'Ю', 'Я'}};

    private String pText;// открытый текст
    private String[] cText;// закрытый текст
    private String dText;// расшифрованный текст

    Polibius(String text) {
        this.pText = text;
        this.dText = "";
    }

    public String encode() {
        String fText = format(pText);
        this.cText = new String[fText.length()];
        for (int i = 0; i < fText.length(); i++) {
            this.cText[i] = findIndex(fText.charAt(i));
        }
        return Arrays.deepToString(cText);
    }

    public String decode() {
        for (int i = 0; i < cText.length; i++) {
            int a = Integer.valueOf(cText[i].substring(0, 1));
            int b = Integer.valueOf(cText[i].substring(1));
            this.dText += alphabet[a][b];
        }
        return this.dText;
    }

    private String findIndex(char el) {
        for (int i = 0; i < alphabet.length; i++) {
            for (int j = 0; j < alphabet[0].length; j++) {
                if (el == alphabet[i][j])
                    return (String.valueOf(i) + String.valueOf(j));
            }
        }
        return "";
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
