package SignatureRSA;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

public class SignatureRSA {
    public static void main(){
        System.out.println("ЦП RSA");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        SignatureRSA signatureRSA = new SignatureRSA(text);
        BigInteger A = null;// подпись Алисы (Алиса отправляет сообщение)
        try {
            A = signatureRSA.sending();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("Цифровая подпись отправителя: " + A);
        if (signatureRSA.getting())
            System.out.println("Подпись верна!");
        else
            System.out.println("Подпись неверна!");

    }

    BigInteger[] pKey = new BigInteger[2];// открытый ключ (public key)
    BigInteger[] sKey = new BigInteger[2];// закрытый ключ (secret key)
    String pText;// открытый текст
    BigInteger signature;
    BigInteger hashA;
    BigInteger hashB;

    SignatureRSA(String text){
        this.pText = text;
        BigInteger p, q, n, euler_val, e, d;
        BigInteger one = BigInteger.valueOf(1);

        // p и q - случайные простые числа размером бит bitLength
        int bitLength = 1024;
        SecureRandom rnd = new SecureRandom();
        p = BigInteger.probablePrime(bitLength, rnd);
        q = BigInteger.probablePrime(bitLength, rnd);

        // n = p*q
        n = p.multiply(q);

        // Ф(n) = (p - 1)*(q - 1)
        euler_val = p.subtract(one).multiply(q.subtract(one));

        // 1 < e < Ф(n),  gcd(e, Ф) = 1
        e = BigInteger.valueOf(2);
        while ((e.compareTo(euler_val) < 0) && !euler_val.gcd(e).equals(one)) {
            e = e.add(one);
        }

        // 1 < d < Ф, e*d = 1(mod Ф(n))
        // or, in other words,
        // d = e^(-1) mod Ф(n)
        d = e.modInverse(euler_val);

        pKey[0] = e;
        pKey[1] = n;

        sKey[0] = d;
        sKey[1] = n;
    }

    public BigInteger sending() throws NoSuchAlgorithmException {// функция шифрования
        this.hashA = Hash(this.pText);
        System.out.println("Хэш сообщения отправителя: " + this.hashA);
        this.signature = this.hashA.modPow(this.sKey[0], this.sKey[1]);
        return this.signature;
    }

    public boolean getting(){// функция расшифрования
        this.hashB = this.signature.modPow(pKey[0], pKey[1]);
        System.out.println("Расшифрованная подпись (хэш): " + this.hashB);
        if (this.hashA.equals(this.hashB))
            return true;
        else
            return false;
    }

    private BigInteger Hash(String text){
        BigInteger h = BigInteger.valueOf(0);
        for (int i = 0; i<text.length();i++){
            int t = (int) text.charAt(i);
            h = BigInteger.valueOf(t).add(h).modPow(BigInteger.valueOf(2), this.pKey[1]);
        }
        return h;
    }


}
