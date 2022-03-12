package GOST34102012;

import java.math.BigInteger;
import java.util.Scanner;

public class GOST34102012 {
    public static void main() {
        System.out.println("ГОСТ Р 34.10-2012");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        GOST34102012 gost34102012 = new GOST34102012(text);
        System.out.println("Отправка цифровой подписи cтороной А:" + gost34102012.sending());
        System.out.println("Получение и проверка цифровой подписи стороной В: ");
        if (gost34102012.getting() == true)
            System.out.println("Подпись верна!");
        else
            System.out.println("Подпись неверна!");
    }

    private BigInteger[] pKeyA;// открытый ключ стороны A
    private BigInteger[] pKeyB;// открытый ключ стороны B
    private BigInteger kA;// закрытый ключ k стороны А
    private BigInteger kB;// закрытый ключ k стороны B
    private String text;// открытый текст
    private BigInteger[] Ep;//эллиптическая кривая
    private BigInteger mod;//модуль p
    private BigInteger[] G = new BigInteger[2];//базовая точка
    private BigInteger q;//порядок кривой
    private BigInteger s;//цифровая подпись
    private BigInteger r;//цифровая подпись

    GOST34102012(String text) {
        this.text = text;
        this.mod = new BigInteger("3623986102229003635907788753683874306021320925534678605086546150450856166624002482588482022271496854025090823603058735163734263822371964987228582907372403");
        this.Ep = new BigInteger[]{BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(7), new BigInteger("1518655069210828534508950034714043154928747527740206436194018823352809982443793732829756914785974674866041605397883677596626326413990136959047435811826396")};
        this.G[0] = new BigInteger("1928356944067022849399309401243137598997786635459507974357075491307766592685835441065557681003184874819658004903212332884252335830250729527632383493573274");
        this.G[1] = new BigInteger("2288728693371972859970012155529478416353562327329506180314497425931102860301572814141997072271708807066593850650334152381857347798885864807605098724013854");
        this.q = new BigInteger("3623986102229003635907788753683874306021320925534678605086546150450856166623969164898305032863068499961404079437936585455865192212970734808812618120619743");
    }

    public String sending() {
        BigInteger h = Hash(this.text, this.mod);
        if (h.compareTo(BigInteger.ZERO) == 0)
            h = BigInteger.ONE;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Сторона А, сгенерируйте k (0<k<" + this.q + "):");
        this.kA = scanner.nextBigInteger();
        while (this.kA.compareTo(BigInteger.ZERO) <= 0 || this.kA.compareTo(this.q) >= 0) {
            System.out.println("Выбранное k не входит в интервал! k = ");
            this.kA = scanner.nextBigInteger();
        }
        this.pKeyA = kPoint(this.kA, G);
        System.out.println("Сторона B, сгенерируйте k (0<k<" + this.q + "):");
        this.kB = scanner.nextBigInteger();
        while (this.kB.compareTo(BigInteger.ZERO) <= 0 || this.kB.compareTo(this.q) >= 0) {
            System.out.println("Выбранное k не входит в интервал! k = ");
            this.kB = scanner.nextBigInteger();
        }
        this.pKeyB = kPoint(this.kB, G);
        this.r = this.pKeyB[0].mod(this.q);
        while (r.equals(BigInteger.ZERO)) {
            System.out.println("Сторона А, сгенерируйте другое k!");
            this.kA = scanner.nextBigInteger();
            while (this.kA.compareTo(BigInteger.ZERO) <= 0 || this.kA.compareTo(this.q) >= 0) {
                System.out.println("Выбранное k не входит в интервал! k = ");
                this.kA = scanner.nextBigInteger();
            }
            this.r = this.pKeyA[0].mod(this.q);
        }
        this.s = this.kB.multiply(h).add(r.multiply(this.kA)).mod(this.q);
        while (s.equals(BigInteger.ZERO)) {
            System.out.println("Сторона А, сгенерируйте другое k!");
            this.kA = scanner.nextBigInteger();
            while (this.kA.compareTo(BigInteger.ZERO) <= 0 || this.kA.compareTo(this.q) >= 0) {
                System.out.println("Выбранное k не входит в интервал! k = ");
                this.kA = scanner.nextBigInteger();
            }
            this.r = this.pKeyA[0].mod(this.q);
            this.s = this.kA.multiply(h).add(r.multiply(pKeyB[0])).mod(this.q);
        }
        System.out.println("\nr = " + this.r + "\ns = " + this.s);
        return "Сообщение с эл. подписью успешно отправлено!";
    }

    public boolean getting() {
        BigInteger h = Hash(this.text, this.mod);
        if (h.compareTo(BigInteger.ZERO) == 0)
            h = BigInteger.ONE;
        if (this.r.compareTo(BigInteger.ZERO) <= 0 || this.s.compareTo(this.q) >= 0)//проверяем условие r>0 и s<r
            return false;
        BigInteger v = h.modInverse(this.q);
        System.out.println("v = " + v);
        BigInteger u1 = this.s.multiply(v).mod(this.q);
        System.out.println("z1 = " + u1);
        BigInteger u2 = (BigInteger.ZERO.subtract(this.r)).multiply(h.modInverse(this.q)).mod(this.q);
        System.out.println("z2 = " + u2);
        BigInteger[] P = additionPoints(kPoint(u1, this.G), kPoint(u2, this.pKeyA));
        BigInteger R = P[0].mod(this.q);
        System.out.println("R = " + R);
        if (P[0].equals(BigInteger.ZERO) && P[1].equals(BigInteger.ZERO))
            return false;
        if (P[0].mod(this.q).equals(this.r))
            return true;
        return false;
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

    private BigInteger Hash(String text, BigInteger mod) {// хэш-функция по модулю
        BigInteger h = BigInteger.valueOf(0);
        for (int i = 0; i < text.length(); i++) {
            int t = (int) text.charAt(i);
            h = BigInteger.valueOf(t).add(h).modPow(BigInteger.valueOf(2), mod);
        }
        return h;
    }
}
