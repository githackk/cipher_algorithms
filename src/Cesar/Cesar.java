package Cesar;

import Atbasch.Atbasch;

import java.util.Random;
import java.util.Scanner;

public class Cesar {
    public static void main(){
        System.out.println("Цезарь");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        Cesar cesar = new Cesar(text);
        System.out.println("Зашифрованный текст:" + cesar.encode());
        System.out.println("Расшифрованный текст:" + cesar.decode());
    }


    public static char alphabet[] = new char[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З',
            'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р',
            'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш',
            'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    private String pText;// открытый текст
    private String cText;// закрытый текст
    private String dText;// расшифрованный текст
    private int Key;

    Cesar(String text) {
        this.pText = text;
        this.cText = "";
        this.dText = "";
        this.Key = (int)(Math.random()*((31-1)+1))+1;
    }

    String encode() {
        String fText = format(pText);
        for (int i = 0; i < fText.length(); i++) {
            cText += alphabet[(new String(alphabet).indexOf(fText.charAt(i)) + Key) % 32];
        }
        return cText;
    }

    String decode() {
        for (int i = 0; i < cText.length(); i++) {
            dText += alphabet[(new String(alphabet).indexOf(cText.charAt(i)) - Key + 32) % 32];
        }
        System.out.println("Ключ:" + Key);
        return dText;
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
