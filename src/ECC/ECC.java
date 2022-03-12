package ECC;


import java.math.BigInteger;
import java.util.Scanner;

public class ECC {
    public static void main() {
        System.out.println("ECC");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст: ");
        String text = scanner.nextLine();
        ECC ecc = new ECC(text);
        System.out.println("Зашифрованный текст:" + ecc.encode());
        System.out.println("Расшифрованный текст:" + ecc.decode());
    }

    public static char alphabet[] = new char[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З',
            'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р',
            'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш',
            'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    private BigInteger[] pKeyA;// открытый ключ стороны A
    private BigInteger[] pKeyB;// открытый ключ стороны B
    private BigInteger kA;// закрытый ключ k стороны А
    private BigInteger kB;// закрытый ключ k стороны B
    private String pText;// открытый текст
    private BigInteger[] cText;// закрытый текст
    private String dText;// расшифрованный текст
    private BigInteger[] Ep;//эллиптическая кривая
    private BigInteger mod;//модуль p
    private BigInteger[] G = new BigInteger[2];//базовая точка
    private BigInteger n;//порядок кривой

    ECC(String text) {
        this.pText = format(text);
        this.cText = new BigInteger[this.pText.length()];
        this.Ep = new BigInteger[]{BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(1), BigInteger.valueOf(1)};
        this.mod = BigInteger.valueOf(37);
        this.G[0] = BigInteger.valueOf(3);
        this.G[1] = BigInteger.valueOf(6);
        this.n = BigInteger.valueOf(getN());
        this.kA = BigInteger.valueOf(1);
        this.kB = BigInteger.valueOf(4);
    }

    public String encode() {//действия Алисы А, которая отправляет шифр-текст
        BigInteger q = getN1();
        Scanner scanner = new Scanner(System.in);
        System.out.println("p = " + this.mod);
        System.out.println("Ep = y^2 = x^3 + " + this.Ep[2] + "x" + " + " + this.Ep[3]);
        System.out.println("G = [" + this.G[0] + ", " + this.G[1] + "]");
        System.out.println("n = " + this.n);

        System.out.println("Сторона А, сгенерируйте k (0<k<" + this.n + "):");
        this.kA = scanner.nextBigInteger();
        while (this.kA.compareTo(this.n) >= 0 || this.kA.compareTo(BigInteger.ZERO) <= 0) {
            System.out.println("Выбрано недопустимое k! k = ");
            this.kA = scanner.nextBigInteger();
        }
        this.pKeyA = kPoint(this.kA, G);

        System.out.println("Сторона B, сгенерируйте k (0<k<" + this.n + "):");
        this.kB = scanner.nextBigInteger();
        while (this.kA.compareTo(this.n) >= 0) {
            System.out.println("Выбрано недопустимое k! k = ");
            this.kB = scanner.nextBigInteger();
        }
        this.pKeyB = kPoint(this.kB, G);

        BigInteger[] P = kPoint(this.kA, this.pKeyB);
        int[] digitalText = toDigital(this.pText);
        for (int i = 0; i < digitalText.length; i++) {
            this.cText[i] = P[0].multiply(BigInteger.valueOf(digitalText[i])).mod(mod);
        }
        String str = "";
        for (int i = 0; i < this.cText.length; i++) {
            str += this.cText[i] + "\n";
        }
        return str;
    }

    public String decode() {//действия Боба В, который расшифровывает полученный текст
        BigInteger[] Q = kPoint(kB, this.pKeyA);
        int[] digitalText = new int[this.cText.length];
        for (int i = 0; i < this.cText.length; i++) {
            digitalText[i] = Integer.parseInt(this.cText[i].multiply(Q[0].modInverse(mod)).mod(mod).toString());
        }
        this.dText = toText(digitalText);
        return this.dText;
    }

    private int getN() {
        int mod = Integer.parseInt(this.mod.toString());
        int counter = 0;
        BigInteger[] y = new BigInteger[mod];
        for (int i = 0; i < mod; i++) {
            y[i] = BigInteger.valueOf(i).multiply(BigInteger.valueOf(i)).mod(BigInteger.valueOf(mod));
        }
        for (int i = 0; i < mod; i++) {
            BigInteger x = this.Ep[0].multiply(BigInteger.valueOf(i).pow(3).add(this.Ep[1].multiply(BigInteger.valueOf(i).pow(2)).add(this.Ep[2].multiply(BigInteger.valueOf(i))).add(this.Ep[3]))).mod(this.mod);
            for (int j = 0; j < mod; j++) {
                if (x.equals(y[j]) && x.equals(BigInteger.ZERO)) {
                    counter++;
                    break;
                } else if (x.equals(y[j])) {
                    counter += 2;
                    break;
                }
            }
        }
        counter++;
        return counter;
    }

    private BigInteger getN1() {//получение порядка кривой
        BigInteger n = BigInteger.valueOf(2);
        BigInteger[] temp = this.G;
        while (1 == 1) {
            temp = additionPoints(temp, G);
            if (temp[0].equals(BigInteger.ZERO) && temp[1].equals(BigInteger.ZERO))
                return n;
            n = n.add(BigInteger.ONE);
        }
    }

    private BigInteger[] kPoint(BigInteger k, BigInteger[] point) {//функция для многократного сложения одной и той же точки с собой
        BigInteger[] result = point;
        BigInteger index = BigInteger.ONE;
        while (index.multiply(BigInteger.valueOf(2)).compareTo(k) <= 0) {
            result = additionPoints(result, result);
            index = index.multiply(BigInteger.valueOf(2));
        }
        if (index.compareTo(k) < 0)
            return result = additionPoints(result, kPoint(k.subtract(index), point));
        else
            return result;
    }

    private BigInteger[] additionPoints(BigInteger[] point1, BigInteger[] point2) {//фукнция сложения двух точек на эллиптической кривой
        BigInteger[] result = new BigInteger[2];

        BigInteger y;
        if (point1.equals(point2)) {
            BigInteger num = point1[0].pow(2).multiply(BigInteger.valueOf(3)).add(Ep[2]);
            BigInteger denum = BigInteger.valueOf(2).multiply(point1[1]);
            y = denum.modInverse(mod);
            y = y.multiply(num).mod(mod);
        } else {
            BigInteger num = point2[1].subtract(point1[1]);
            BigInteger denum = point2[0].subtract(point1[0]);
            if (denum.equals(BigInteger.ZERO))
                return new BigInteger[]{BigInteger.ZERO, BigInteger.ZERO};
            y = denum.modInverse(mod);
            y = y.multiply(num).mod(mod);
        }
        result[0] = y.pow(2).subtract(point1[0]).subtract(point2[0]).mod(mod);
        result[1] = y.multiply(point1[0].subtract(result[0])).subtract(point1[1]).mod(mod);

        return result;
    }

    private int[] toDigital(String text) {// перевод текста из алфавитного эквивалента в цифровой
        int[] digitals = new int[text.length()];
        for (int i = 0; i < digitals.length; i++) {
            digitals[i] = new String(alphabet).indexOf(text.charAt(i));
        }
        return digitals;
    }

    private String toText(int[] digitals) {// перевод текста из цифрового эквивалента в алфавитный
        String text = "";
        for (int i = 0; i < digitals.length; i++) {
            text += alphabet[digitals[i]];
        }
        return text;
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
