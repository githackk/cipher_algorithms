package CardanoGrille;


import java.util.Random;
import java.util.Scanner;

public class CardanoGrille {
    public static void main() {
        System.out.println("Решетка Кардано");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        CardanoGrille cardanoGrille = new CardanoGrille(text);
        System.out.println("Зашифрованный текст:" + cardanoGrille.encode());
        System.out.println("Расшифрованный текст:" + cardanoGrille.decode());
    }
    private String pText;
    private String cText;
    private String dText;
    private int[][] Key;

    CardanoGrille(String text) {
        this.pText = text;
        this.Key = keyGrille();
        this.cText = "";
        this.dText = "";
    }

    public String encode() {// функция зашифрования
        Random r = new Random();
        while (this.pText.length() % 64 != 0)
            this.pText += (char) (r.nextInt((1100 - 1040) + 1) + 1040);
        int n;
        if (this.pText.length() % 64 > 0)
            n = this.pText.length() / 64 + 1;
        else
            n = this.pText.length() / 64;
        char[][][] table = new char[n][8][8];

        int count = 0;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                for (int k = 0; k < table[0][0].length; k++) {
                    if (this.Key[j][k] == 0) {
                        table[i][j][k] = this.pText.charAt(count);
                        count++;
                    }
                }
            }
            this.Key = rotateRight90(this.Key);
            for (int j = 0; j < table[0].length; j++) {
                for (int k = 0; k < table[0][0].length; k++) {
                    if (this.Key[j][k] == 0) {
                        table[i][j][k] = this.pText.charAt(count);
                        count++;
                    }
                }
            }
            this.Key = rotateRight90(this.Key);
            for (int j = 0; j < table[0].length; j++) {
                for (int k = 0; k < table[0][0].length; k++) {
                    if (this.Key[j][k] == 0) {
                        table[i][j][k] = this.pText.charAt(count);
                        count++;
                    }
                }
            }
            this.Key = rotateRight90(this.Key);
            for (int j = 0; j < table[0].length; j++) {
                for (int k = 0; k < table[0][0].length; k++) {
                    if (this.Key[j][k] == 0) {
                        table[i][j][k] = this.pText.charAt(count);
                        count++;
                    }
                }
            }
            this.Key = rotateRight90(this.Key);
        }
        for (int i = 0; i<table.length;i++){
            for (int j = 0; j<table[0].length;j++){
                for (int k = 0; k<table[0][0].length;k++){
                    this.cText += table[i][j][k];
                }
            }
        }
        return this.cText;
    }

    public String decode() {// функция расшифрования
        Random r = new Random();
        int n = this.cText.length() / 64;
        char[][][] table = new char[n][8][8];
        int count = 0;
        for (int i = 0;i<table.length;i++){
            for (int j = 0; j<table[0].length;j++){
                for (int k = 0; k<table[0][0].length;k++){
                    table[i][j][k] = this.cText.charAt(count);
                    count++;
                }
            }
        }

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                for (int k = 0; k < table[0][0].length; k++) {
                    if (this.Key[j][k] == 0) {
                        this.dText += table[i][j][k];
                    }
                }
            }
            this.Key = rotateRight90(this.Key);
            for (int j = 0; j < table[0].length; j++) {
                for (int k = 0; k < table[0][0].length; k++) {
                    if (this.Key[j][k] == 0) {
                        this.dText += table[i][j][k];
                    }
                }
            }
            this.Key = rotateRight90(this.Key);
            for (int j = 0; j < table[0].length; j++) {
                for (int k = 0; k < table[0][0].length; k++) {
                    if (this.Key[j][k] == 0) {
                        this.dText += table[i][j][k];
                    }
                }
            }
            this.Key = rotateRight90(this.Key);
            for (int j = 0; j < table[0].length; j++) {
                for (int k = 0; k < table[0][0].length; k++) {
                    if (this.Key[j][k] == 0) {
                        this.dText += table[i][j][k];
                    }
                }
            }
            this.Key = rotateRight90(this.Key);
        }
        return this.dText;
    }

    private int[][] keyGrille() {// генерация решетки 8х8
        int[][][] key = new int[4][4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                key[0][i][j] = i * 4 + j + 1;
            }
        }
        for (int i = 1; i < 4; i++) {
            key[i] = rotateRight90(key[i - 1]);
        }
        for (int i = 0; i < 16; i++) {
            Random r = new Random();
            int number = r.nextInt(4);
            key[number] = findEl(key[number], i + 1);
        }
        int[][] Key = new int[8][8];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Key[i][j] = key[0][i][j];
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 4; j < 8; j++) {
                Key[i][j] = key[1][i][j - 4];
            }
        }
        for (int i = 4; i < 8; i++) {
            for (int j = 4; j < 8; j++) {
                Key[i][j] = key[2][i - 4][j - 4];
            }
        }
        for (int i = 4; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                Key[i][j] = key[3][i - 4][j];
            }
        }
        return Key;
    }

    private int[][] rotateRight90(int[][] array) {// поворот матрицы на 90 градусов по часовой стрелке
        int[][] resultArray = new int[array[0].length][array.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                resultArray[j][array.length - i - 1] = array[i][j];
            }
        }
        return resultArray;
    }

    private int[][] findEl(int[][] array, int el) {// пустым ячейкам решетки присваивается 0, как отличительный признак
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] == el) {
                    array[i][j] = 0;
                    return array;
                }
            }
        }
        return array;
    }
}
