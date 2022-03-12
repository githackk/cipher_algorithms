package GOST341094;

import javafx.util.Pair;

import java.math.BigInteger;
import java.util.Scanner;

public class GOST341094 {
    public static void main() {
        System.out.println("ГОСТ Р 34.10-94");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        GOST341094 gost341094 = new GOST341094(text);
        System.out.println("Отправка цифровой подписи cтороной А:" + gost341094.sending());
        System.out.println("Получение и проверка цифровой подписи стороной В: ");
        if (gost341094.getting()==true)
            System.out.println("Подпись верна!");
        else
            System.out.println("Подпись неверна!");
    }

    private String text;//текст
    private BigInteger p;//общий параметр
    private BigInteger q;//общий параметр
    private BigInteger a;//общий параметр
    private BigInteger x;//секретный ключ
    private BigInteger y;//открытый ключ
    Pair<BigInteger, BigInteger> signature;// подпись

    GOST341094(String text){
        this.text = text;
        this.p = BigInteger.valueOf(23);
        this.q = BigInteger.valueOf(11);
        this.a = BigInteger.valueOf(6);
        this.x = BigInteger.valueOf(8);
        this.y = this.a.modPow(this.x, this.p);
    }

    public String sending(){//функция отправки цифровой подписи
        System.out.println("Публичные параметры: p = " + this.p + " q = " + this.q + " a = " + this.a);
        System.out.println("Закрытый ключ x = " + this.x);
        System.out.println("Открытый ключ y = " + this.y);
        Scanner scanner = new Scanner(System.in);
        BigInteger hash = Hash(this.text, this.q);
        if (hash.compareTo(BigInteger.ZERO)==0)
            hash = BigInteger.ONE;
        System.out.println("Сторона А, сгенерируйте k (0<k<" + this.q + "):");
        BigInteger k = scanner.nextBigInteger();
        while (k.compareTo(BigInteger.ONE)<0||this.q.compareTo(k)<1){
            System.out.println("Введенное значение k не входит в указанный интервал! Значение k = ");
            k = scanner.nextBigInteger();
        }
        BigInteger r = this.a.modPow(k, this.p).mod(this.q);
        while (r.compareTo(BigInteger.ZERO) == 0){
            System.out.println("Введенное значение k не подходит, введите другое! Значение k = ");
            k = scanner.nextBigInteger();
            r = this.a.modPow(k, this.p).mod(this.q);
        }
        BigInteger s = (this.x.multiply(r).add(k.multiply(hash))).mod(this.q);
        this.signature = new Pair<>(r, s);
        return this.signature.toString();
    }

    public boolean getting(){//функция получения и проверки цифровой подписи
        BigInteger v = Hash(this.text, this.q).modPow(this.q.subtract(BigInteger.valueOf(2)), this.q);
        BigInteger z1 = this.signature.getValue().multiply(v).mod(this.q);
        BigInteger z2 = ((this.q.subtract(this.signature.getKey())).multiply(v)).mod(this.q);
        BigInteger u = ((this.a.modPow(z1, this.p).multiply(this.y.modPow(z2, this.p))).mod(this.p)).mod(this.q);
        System.out.println("Параметр u = " +  u);
        System.out.println("Параметр r = " + this.signature.getKey());
        if (u.compareTo(this.signature.getKey()) == 0)
            return true;
        else
            return false;
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
