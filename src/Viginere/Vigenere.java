package Viginere;

import java.util.Scanner;

public class Vigenere {
    public static void main() {
        System.out.println("Вижинер");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ключ-букву:");
        String key = scanner.nextLine();
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        Vigenere vigenere = new Vigenere(text, key);
        System.out.println("Зашифрованный текст:" + vigenere.encode());
        System.out.println("Расшифрованный текст:" + vigenere.decode());
    }

    public static char alphabet[] = new char[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З',
            'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р',
            'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш',
            'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    private String pText;// открытый текст
    private String cText;// закрытый текст
    private String dText;// расшифрованный текст
    private String Key;// ключ

    Vigenere(String text) {// конструктор при статически заданном ключе
        this.pText = text;
        this.cText = "";
        this.dText = "";
    }

    Vigenere(String text, String key) {// конструктор при получении ключа от пользователя
        this.pText = text;
        this.cText = "";
        this.dText = "";
        this.Key = key.toUpperCase();
    }

    public String encode() {// функция шифрования
        String fText = format(this.pText);
        String keyString = this.Key + fText;
        for (int i = 0; i < fText.length(); i++) {
            int a = new String(alphabet).indexOf(keyString.charAt(i));
            int b = new String(alphabet).indexOf(fText.charAt(i));
            this.cText += alphabet[(a + b) % 32];
        }
        return this.cText;
    }

    public String decode() {// функция расшифрования
        int key = new String(alphabet).indexOf(Key);
        for (int i = 0; i < this.cText.length(); i++) {
            int a = new String(alphabet).indexOf(this.cText.charAt(i));
            key = a - key;
            if (key < 0)
                key += 32;
            this.dText += alphabet[key];
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
