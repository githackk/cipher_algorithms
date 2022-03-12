package ClaudeShannon;

import java.util.Random;
import java.util.Scanner;

public class ClaudeShannon {
    public static void main(){
        System.out.println("Одноразовый блокнот К.Шеннона");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        ClaudeShannon claudeShannon = new ClaudeShannon(text);
        System.out.println("Зашифрованный текст:" + claudeShannon.cipher());
        System.out.println("Расшифрованный текст:" + claudeShannon.decipher());
    }

    public static char alphabet[] = new char[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З',
            'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р',
            'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш',
            'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    private String text;//текст
    private String cipher_text;//шифр-текст
    private String decipher_text;//дешифрованный текста
    private String key;//ключ

    public String getKey(){//гетер ключа
        return this.key;
    }

    public ClaudeShannon(String text) {//конструктор для шифрования
        this.text = format(text);
        this.keyGeneration();
    }

    public ClaudeShannon(String text, String key){//конструктор для дешифрования
        this.cipher_text = format(text);
        this.key = format(key);
    }

    private String format(String text){//функция приведения текста к специальному формату
        text = text.toUpperCase();
        text = text.replaceAll(" ", "");
        text = text.replaceAll(",", "ЗПТ");
        text = text.replaceAll("-", "ТИРЕ");
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

    private void keyGeneration() {//генерация ключа
        this.key = new String();
        Random random = new Random();
        while (this.key.length() != text.length()) {
            char symbol = alphabet[random.nextInt(31)];
            this.key += symbol;
        }
    }

    private int[] toDigital(String text) {//перевод текста из букв в цифры
        int[] digital = new int[text.length()];
        for (int i = 0; i < digital.length; i++) {

            digital[i] = (new String(alphabet).indexOf(text.charAt(i)));
        }
        return digital;
    }

    private String toText(int[] digitals) {//перевод текста из цифр в буквы
        String text = new String();
        for (int i = 0; i < digitals.length; i++) {
            text += alphabet[digitals[i]];
        }
        return text;
    }

    public String cipher() {//функция шифрования
        int digital_key[] = toDigital(this.key);
        int digital_text[] = toDigital(this.text);
        int digital_cipher[] = new int[digital_text.length];

        for (int i = 0; i < digital_key.length; i++) {
            digital_cipher[i] = (digital_key[i] + digital_text[i]) % 32;
        }
        return this.cipher_text = toText(digital_cipher);
    }

    public String decipher() {//функция дешифрования
        int digital_key[] = toDigital(this.key);
        int digital_cipher[] = toDigital(this.cipher_text);
        int digital_text[] = new int[digital_cipher.length];

        for (int i = 0; i < digital_key.length; i++) {
            digital_text[i] = digital_cipher[i] - digital_key[i];
            if (digital_text[i] < 0)
                digital_text[i] = digital_text[i] + 32;
        }

        return this.decipher_text = toText(digital_text);
    }


}
