package RSA;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;

public class RSA {
    public static void main() {
        System.out.println("RSA");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        RSA rsa = new RSA(text);
        try {
            System.out.println("Зашифрованный текст:" + rsa.encode());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("Расшифрованный текст:" + rsa.decode());

    }

    BigInteger[] pKey = new BigInteger[2];// открытый ключ (public key)
    BigInteger[] sKey = new BigInteger[2];// закрытый ключ (secret key)
    String pText;// открытый текст
    BigInteger[] cText;// закрытый текст
    String dText;// расшифрованный текст

    RSA(String text) {
        this.pText = text;
        BigInteger p, q, n, euler_val, e, d;
        BigInteger one = BigInteger.valueOf(1);

        // p и q - случайные простые числа размером бит bitLength
        int bitLength = 1024;
        SecureRandom rnd = new SecureRandom();
        p = BigInteger.probablePrime(bitLength, rnd);
        q = BigInteger.probablePrime(bitLength, rnd);
        System.out.println("p = " + p + " q = " + q);

        // n = p*q
        n = p.multiply(q);

        // Ф(n) = (p - 1)*(q - 1)
        euler_val = p.subtract(one).multiply(q.subtract(one));

        // 1 < e < Ф(n),  gcd(e, Ф) = 1
        System.out.println("Введите небольшое 1<e<" + euler_val + " и взаимно простое с " + euler_val + ": ");
        Scanner scanner = new Scanner(System.in);
        e = BigInteger.valueOf(Long.parseLong(scanner.nextLine()));
        while (BigInteger.ONE.compareTo(e) > 0 || e.compareTo(euler_val) > 0 || !euler_val.gcd(e).equals(one)) {
            System.out.println("e не входит в интервал или не взаимно просто с " + euler_val + "! Введите e:");
            e = BigInteger.valueOf(Long.parseLong(scanner.nextLine()));
        }
        /*while ((e.compareTo(euler_val) < 0) && !euler_val.gcd(e).equals(one)) {
            e = e.add(one);
        }*/

        // 1 < d < Ф, e*d = 1(mod Ф(n))
        // d = e^(-1) mod Ф(n)
        d = e.modInverse(euler_val);

        pKey[0] = e;
        pKey[1] = n;

        sKey[0] = d;
        sKey[1] = n;
    }

    public String encode() throws NoSuchAlgorithmException {// функция шифрования
        char[] m_chars = this.pText.toCharArray();
        this.cText = new BigInteger[m_chars.length];
        for (int i = 0; i < m_chars.length; i++) {
            // c = m^e mod n
            int c = (int) m_chars[i];
            cText[i] = (BigInteger.valueOf((int) m_chars[i])).modPow(this.pKey[0], this.pKey[1]);
        }

        String str = "";
        for (int i = 0; i < cText.length; i++) {
            str += cText[i] + "\n";
        }
        return str;
    }

    public String decode() {// функция расшифрования
        System.out.println("Secret key [d, n] is: " + Arrays.toString(sKey));

        char[] m_chars = new char[cText.length];
        StringBuilder decrypted_str = new StringBuilder();
        for (int i = 0; i < this.cText.length; i++) {
            // m = c^d mod n
            m_chars[i] = (char) this.cText[i].modPow(sKey[0], sKey[1]).intValue();
            decrypted_str.append(m_chars[i]);
        }
        return this.dText = decrypted_str.toString();
    }


}
