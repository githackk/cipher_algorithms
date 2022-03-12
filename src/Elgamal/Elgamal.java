package Elgamal;

import javafx.util.Pair;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class Elgamal {
    public static void main() {
        System.out.println("Elgamal");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        Elgamal elgamal = new Elgamal(text);
        System.out.println("Зашифрованный текст:" + elgamal.encode());
        System.out.println("Расшифрованный текст:" + elgamal.decode());
    }

    BigInteger[] pKey = new BigInteger[3];// открытый ключ (p, g, y)
    BigInteger sKey;// закрытый ключ x
    String pText;// открытый текст
    List<Pair<BigInteger, BigInteger>> cText;// закрытый текст
    String dText;// расшифрованный текст

    Elgamal(String text) {
        SecureRandom rnd = new SecureRandom();
        this.pKey[0] = BigInteger.probablePrime(11, rnd);
        do {
            this.pKey[1] = BigInteger.probablePrime(this.pKey[0].bitLength(), new Random());
        } while (!this.pKey[1].pow(this.pKey[0].intValue() - 1).mod(this.pKey[0]).equals(BigInteger.ONE));

        BigInteger maxLimit = pKey[0].subtract(BigInteger.ONE);
        BigInteger minLimit = new BigInteger("2");
        BigInteger bigInteger = maxLimit.subtract(minLimit);
        Random randNum = new Random();
        int len = maxLimit.bitLength();
        this.sKey = new BigInteger(len, randNum);

        this.pKey[2] = this.pKey[1].modPow(sKey, pKey[0]);
        this.pText = text;
        this.cText = new LinkedList<>();
        this.dText = "";
    }

    public String encode() {
        char[] m_chars = this.pText.toCharArray();
        int[] dig_chars = new int[m_chars.length];
        for (int i = 0; i < dig_chars.length; i++) {
            dig_chars[i] = (int) m_chars[i];
        }


        Random randNum = new Random();
        int len = pKey[0].bitLength();

        for (int i = 0; i < dig_chars.length; i++) {
            BigInteger k;
            do {
                k = new BigInteger(len, randNum);
            } while (k.gcd(this.pKey[0].subtract(BigInteger.ONE)).compareTo(BigInteger.ONE) != 0 ||//(k, p-1)=1
                    k.compareTo(this.pKey[0].subtract(BigInteger.ONE)) >= 0 ||// 1<k<p-1
                    k.compareTo(BigInteger.ONE) < 1);// 1<k<p-1
            BigInteger a = pKey[1].modPow(k, pKey[0]);
            BigInteger b = (pKey[2].pow(k.intValue())).multiply(BigInteger.valueOf(dig_chars[i])).mod(pKey[0]);

            this.cText.add(new Pair<BigInteger, BigInteger>(a, b));
        }
        return cText.toString();
    }

    public String decode() {
        for (int i = 0; i < this.cText.size(); i++) {
            BigInteger a = cText.get(i).getKey();
            BigInteger b = cText.get(i).getValue();
            int result = b.multiply(a.pow(pKey[0].subtract(sKey).intValue() - 1)).mod(pKey[0]).intValue();
            this.dText += (char) result;
        }
        return this.dText;
    }

    public BigInteger nextRandomBigInteger(BigInteger n) {
        Random rand = new Random();
        BigInteger result = new BigInteger(n.bitLength(), rand);
        while (result.compareTo(n) >= 0) {
            result = new BigInteger(n.bitLength(), rand);
        }
        return result;
    }
}
