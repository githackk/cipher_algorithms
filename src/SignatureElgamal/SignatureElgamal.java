package SignatureElgamal;

import javafx.util.Pair;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class SignatureElgamal {
    public static void main(){
        System.out.println("ЦП Elgamal");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        SignatureElgamal signatureElgamal = new SignatureElgamal(text);
        String sending = signatureElgamal.sending();// подпись Алисы (Алиса отправляет сообщение)
        System.out.println("Цифровая подпись отправителя: " + sending);
        if (signatureElgamal.getting())
            System.out.println("Подпись верна!");
        else
            System.out.println("Подпись неверна!");
    }

    BigInteger[] pKey = new BigInteger[3];// открытый ключ (p, g, y)
    BigInteger sKey;// закрытый ключ x
    String pText;// открытый текст
    Pair<BigInteger, BigInteger> signature;// подпись
    BigInteger m;// хэшированный текст

    SignatureElgamal(String text) {
        this.pText = text;
        SecureRandom rnd = new SecureRandom();
        this.pKey[0] = BigInteger.probablePrime(1024, rnd);// генерация p
        this.pKey[1] = BigInteger.probablePrime(512, rnd);// генерация g
        this.sKey = nextRandomBigInteger(pKey[0].subtract(BigInteger.ONE));// генерация x
        this.pKey[2] = this.pKey[1].modPow(sKey, pKey[0]);// вычисление y
        this.m = Hash(this.pText, this.pKey[0].subtract(BigInteger.ONE));// получение хэша сообщения
    }

    public String sending(){
        SecureRandom rnd = new SecureRandom();
        BigInteger k = BigInteger.probablePrime(this.pKey[0].bitLength() - 1, rnd);// генерация k
        while (gcd(k, this.pKey[0].subtract(BigInteger.ONE)).equals(BigInteger.ZERO))// если k и p-1 не взаимно просто, то генерация нового k
            k = BigInteger.probablePrime(this.pKey[0].bitLength() - 1, rnd);
        BigInteger a = this.pKey[1].modPow(k, this.pKey[0]);// вычисление a
        BigInteger b = this.m.subtract(this.sKey.multiply(a)).mod(pKey[0].subtract(BigInteger.ONE));// вычисление b
        b = b.multiply(k.modInverse(this.pKey[0].subtract(BigInteger.ONE))).mod(this.pKey[0].subtract(BigInteger.ONE));
        this.signature = new Pair<>(a, b);// полученная цифровая подпись
        return this.signature.toString();
    }

    public boolean getting(){
        BigInteger m = Hash(this.pText, this.pKey[0].subtract(BigInteger.ONE));// получение хэша сообщения
        BigInteger A1 = (this.pKey[2].modPow(this.signature.getKey(), this.pKey[0]). // вычисление A1
                multiply(this.signature.getKey().modPow(this.signature.getValue(), this.pKey[0]))).mod(this.pKey[0]);// вычисление A2
        BigInteger A2 = this.pKey[1].modPow(m, this.pKey[0]);
        System.out.println("A1: " + A1);
        System.out.println("A2: " + A2);
        if (A1.equals(A2)) // если A1 равно А2, то возвращаем истину, подпись верна
            return true;
        return false;// иначе ложь, подпись неверна
    }

    public static BigInteger gcd(BigInteger p, BigInteger q) {// алгоритм Евклида для нахождения НОД
        if (q.equals(BigInteger.ZERO)) return p;
        else return gcd(q, p.mod(p));
    }

    private BigInteger nextRandomBigInteger(BigInteger n) {// вычисление рандомного числа Biginteger в заданном пределе
        Random rand = new Random();
        BigInteger result = new BigInteger(n.bitLength(), rand);
        while (result.compareTo(n) >= 0) {
            result = new BigInteger(n.bitLength(), rand);
        }
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
