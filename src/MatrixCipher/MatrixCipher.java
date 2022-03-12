package MatrixCipher;

import java.util.Random;
import java.util.Scanner;

public class MatrixCipher {
    public static void main() {
        System.out.println("Матричный шифр");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        MatrixCipher matrixCipher = new MatrixCipher(text);
        System.out.println("Зашифрованный текст:" + matrixCipher.encode());
        System.out.println("Расшифрованный текст:" + matrixCipher.decode());
    }

    public static char alphabet[] = new char[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З',
            'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р',
            'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш',
            'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    int n = 3;// размерность матрицы ключа
    private String pText;// открытый текст
    private String cText;// закрытый текст
    private String dText;// расшифрованный текст
    private int[][] key;// ключ-матрица

    MatrixCipher(String text) {
        this.pText = text;
        this.cText = "";
        this.dText = "";
        this.key = genNewKey();
    }

    public String encode() {// функция зашифрования
        String fText = format(this.pText);
        int[] digitalText = toDigital(fText);
        int[][] tableText = formingTable(digitalText);
        int[][] cTableText = new int[tableText.length][tableText[0].length];
        for (int i = 0; i < tableText.length; i++) {
            int[] column = tableText[i];
            int[] res = new int[this.n];
            for (int j = 0; j < tableText[0].length; j++) {
                for (int k = 0; k < tableText[0].length; k++) {
                    res[j] += column[k] * this.key[j][k];
                }
                res[j] = res[j] % 31;
            }
            cTableText[i] = res;
        }
        return this.cText = toText(cTableText);
    }

    public String decode() {// функция расшифрования
        int[][] invKey = getInverseKey();
        int[] digitalText = toDigital(this.cText);
        int[][] tableText = formingTable(digitalText);
        int[][] dTableText = new int[tableText.length][tableText[0].length];
        for (int i = 0; i < tableText.length; i++) {
            int[] column = tableText[i];
            int[] res = new int[this.n];
            for (int j = 0; j < tableText[0].length; j++) {
                for (int k = 0; k < tableText[0].length; k++) {
                    res[j] += column[k] * invKey[j][k];
                }
                res[j] = res[j] % 31;
                res[j] += 31;
                res[j] %= 31;
            }
            dTableText[i] = res;
        }

        System.out.println("Ключ:");
        for (int i = 0; i<3;i++){
            System.out.println(this.key[i][0] + "\t " + this.key[i][1] + "\t " + this.key[i][2]);
        }
        return this.dText = toText(dTableText);
    }

    int getReduction3x3(int[][] matrix) {// определитель матрицы 3х3
        int d = (matrix[0][0] * matrix[1][1] * matrix[2][2] + matrix[0][1] *
                matrix[1][2] * matrix[2][0] + matrix[0][2] * matrix[1][0] *
                matrix[2][1] - matrix[2][0] * matrix[1][1] * matrix[0][2] -
                matrix[2][1] * matrix[1][2] * matrix[0][0] - matrix[2][2] *
                matrix[1][0] * matrix[0][1]) % 31;
        if (d < 0)//детерминант никогда не будет отрицательным
            return d + 31;
        return d;
    }

    int[] gcdex(int a, int b) {// расширенный алгоритм Евклида
        int[] res;
        if (a == 0)
            return new int[]{b, 0, 1};
        res = gcdex(b % a, a);
        return new int[]{res[0], res[2] - (b / a) * res[1], res[1]};
    }

    int invmod(int a, int m) {// вычисление обратного элемента в кольце по модулю
        int[] res = gcdex(a, m);
        if (res[0] > 1)
            return 0;
        else
            return (res[1] % m + m) % m;
    }

    private int[][] getTransposeMatrix(int[][] arr) {// функция транспортирования матрицы
        int[][] result = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                result[j][i] = arr[i][j];
            }
        }
        return result;
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

    private int[][] formingTable(int[] elemets) {// приведение цифрового эквивалента открытого текста в табличный формат
        int length;
        if (elemets.length % 3 == 0)
            length = elemets.length / 3;
        else
            length = elemets.length / 3 + 1;

        int[][] table = new int[length][3];
        for (int i = 0; i < table.length; i++) {
            if (i * 3 + 3 > elemets.length) {
                for (int j = 0; j < elemets.length % 3; j++) {
                    table[i][j] = elemets[i * 3 + j];
                }
                for (int j = elemets.length % 3; j < 3; j++) {
                    table[i][j] = 0;
                }
                break;
            }
            for (int j = 0; j < table[0].length; j++) {
                table[i][j] = elemets[i * 3 + j];
            }
        }
        return table;
    }

    private String toText(int[][] digitals) {// перевод текста из цифрового эквивалента в алфавитный
        String text = "";
        for (int i = 0; i < digitals.length; i++) {
            for (int j = 0; j < digitals[0].length; j++) {
                text += alphabet[digitals[i][j]];
            }
        }
        return text;
    }

    private int[] toDigital(String text) {// перевод текста из алфавитного эквивалента в цифровой
        int[] digitals = new int[text.length()];
        for (int i = 0; i < digitals.length; i++) {
            digitals[i] = new String(alphabet).indexOf(text.charAt(i));
        }
        return digitals;
    }

    private int[][] genKey() {// генерирование ключа
        Random r = new Random();
        int key[][] = new int[this.n][this.n];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                key[i][j] = r.nextInt(31);
            }
        }
        if (getReduction3x3(key) == 0 || invmod(getReduction3x3(key), 31) == 0)
            genKey();
        return key;
    }

    private int[][] genNewKey() {
        Scanner scanner = new Scanner(System.in);
        int[][] key = new int[this.n][this.n];
        String word = "";
        while (word.length()!=9){
            System.out.println("Введите ключ-слово размерностью 9 букв: ");
            word = scanner.nextLine();
        }
        word = word.toUpperCase();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                key[i][j] = (new String(alphabet).indexOf(word.charAt(i * 3 + j)));
            }
        }
        if(getReduction3x3(key)==0||invmod(getReduction3x3(key),31)==0){
            System.out.println("Введите другое слово!");
            genNewKey();
        }
        return key;
    }

    private int[][] getInverseKey() {// инверсия ключа
        int[][] sub = new int[this.key.length][this.key[0].length];
        int d = getReduction3x3(this.key);
        int invD = invmod(d, 31);
        for (int i = 0; i < this.key.length; i++) {
            for (int j = 0; j < this.key.length; j++) {
                sub[i][j] = (int) Math.pow(-1, i + j) * getReductionMinore2x2(this.key, i, j);
            }
        }

        sub = getTransposeMatrix(sub);

        int[][] invKey = new int[sub.length][sub[0].length];
        for (int i = 0; i < invKey.length; i++) {
            for (int j = 0; j < invKey[0].length; j++) {
                invKey[i][j] = invD * sub[i][j] % 31;
            }
        }
        return invKey;
    }

    private int getReductionMinore2x2(int[][] matrix, int a, int b) {// функция-помогатор для вычисления детерминанта миноров
        int d = 0;
        int[][] minore = new int[2][2];
        int c1 = 0;
        int c2 = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (i == a)
                continue;
            for (int j = 0; j < matrix[0].length; j++) {
                if (j == b)
                    continue;
                minore[c1][c2] = matrix[i][j];
                c2++;
            }
            c1++;
            c2 = 0;
        }
        return d = (minore[0][0] * minore[1][1] - minore[0][1] * minore[1][0]) % 31;
    }
}
