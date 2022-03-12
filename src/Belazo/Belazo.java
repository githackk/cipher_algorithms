package Belazo;

import java.util.Scanner;

public class Belazo {
    public static void main() {
        System.out.println("Белазо");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ключ:");
        String key = scanner.nextLine();
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        Belazo belazo = new Belazo(text, key);
        System.out.println("Зашифрованный текст:" + belazo.encode());
        System.out.println("Расшифрованный текст:" + belazo.decode());
    }

    public static char alphabet[] = new char[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З',
            'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р',
            'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш',
            'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    private String pText;// открытый текст
    private String cText;// закрытый текст
    private String dText;// расшифрованный текст
    private String Key;// ключ

    Belazo(String text) {// конструктор при статически заданном ключе
        this.pText = text;
        this.cText = "";
        this.dText = "";
    }

    Belazo(String text, String key){// конструктор при получении ключа от пользователя
        this.pText = text;
        this.cText = "";
        this.dText = "";
        this.Key = format(key);
    }

    public String encode() {// функция шифрования
        String fText = format(pText);
        char[][] keyTable = genKeyTable();
        for (int k = 0; k < fText.length(); k++) {
            int i = k % Key.length()+1;
            int j = new String(keyTable[0]).indexOf(fText.charAt(k));
            this.cText += keyTable[i][j];
        }
        return this.cText;
    }

    public String decode() {// функция расшифрования
        char[][] keyTable = genKeyTable();
        for (int k = 0; k < this.cText.length(); k++) {
            int i = k % Key.length()+1;
            int j = new String(keyTable[i]).indexOf(this.cText.charAt(k));
            this.dText += keyTable[0][j];
        }
        return this.dText;
    }

    private char[][] genKeyTable(){// генерация ключа-таблицы
        char[][] keyTable = new char[Key.length() + 1][32];
        keyTable[0] = alphabet;
        for (int i = 1; i < keyTable.length; i++) {
            int index = new String(alphabet).indexOf(Key.charAt(i - 1));
            int count = 0;
            for (int j = index; j < keyTable[0].length; j++) {
                keyTable[i][count] = alphabet[j];
                count++;
            }
            for (int j = 0; count != 32; j++) {
                keyTable[i][count] = alphabet[j];
                count++;
            }
        }
        return keyTable;
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
