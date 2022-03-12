package Trithemius;

import java.util.Scanner;

public class Trithemius {
    public static void main() {
        System.out.println("Тритемий");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        Trithemius trithemius = new Trithemius(text);
        System.out.println("Зашифрованный текст:" + trithemius.encode());
        System.out.println("Расшифрованный текст:" + trithemius.decode());
    }

    public static char alphabet[] = new char[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З',
            'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р',
            'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш',
            'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    private String pText;// открытый текст
    private String cText;// закрытый текст
    private String dText;// расшифрованный текст

    Trithemius(String text) {
        this.pText = text;
        this.cText = "";
        this.dText = "";
    }

    public String encode() {// функция шифрования
        String fText = format(this.pText);
        for (int j = 0; j < fText.length(); j++) {
            int i = new String(alphabet).indexOf(fText.charAt(j)) + 1;
            this.cText += alphabet[(i + j - 1) % 32];
        }
        return this.cText;
    }

    public String decode() {//функция расшифрования
        for (int j = 0; j < cText.length(); j++) {
            int i = new String(alphabet).indexOf(cText.charAt(j));
            int index = i-j;
            while (index<0)
                index+=32;
            this.dText += alphabet[index];
        }
        return this.dText;
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
