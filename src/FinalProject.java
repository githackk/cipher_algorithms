import A51.A51_cipher;
import A52.A52_cipher;
import AES.AES_cipher;
import Atbasch.Atbasch;
import Belazo.Belazo;
import CardanoGrille.CardanoGrille;
import Cesar.Cesar;
import ClaudeShannon.ClaudeShannon;
import DiffieHelman.DiffieHellman;
import ECC.ECC;
import Elgamal.Elgamal;
import Feistel.Feistel;
import GOST341094.GOST341094;
import GOST34102012.GOST34102012;
import Kuznechik.Kuznechik;
import Magma.Magma;
import MagmaGamma.MagmaGamma;
import MatrixCipher.MatrixCipher;
import Playfair.Playfair;
import Polibius.Polibius;
import RSA.RSA;
import SBlock.SBlock;
import SignatureElgamal.SignatureElgamal;
import SignatureRSA.SignatureRSA;
import Trithemius.Trithemius;
import VerticalTransposition.VerticalTransposition;
import Viginere.*;

import java.util.Scanner;

public class FinalProject {
    public static void main(String[] args){
        int c;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Выберите шифр: \n" +
                    "1. Атбаш\n" +
                    "2. A5/1\n" +
                    "3. A5/2\n" +
                    "4. AES\n" +
                    "5. Белазо\n" +
                    "6. Решетка Кардано\n" +
                    "7. Цезарь\n" +
                    "8. Одноразовый блокнот К.Шеннона\n" +
                    "9. Обмен ключами по Диффи-Хеллману\n" +
                    "10.ECC\n" +
                    "11.Elgamal\n" +
                    "12.Сеть Фейстеля\n" +
                    "13.ГОСТ Р 34.10-94\n" +
                    "14.ГОСТ Р 34.10-2012\n" +
                    "15.Кузнечик\n" +
                    "16.Магма\n" +
                    "17.Магма в режиме гаммирования\n" +
                    "18.Матричный\n" +
                    "19.Плэйфер\n" +
                    "20.Полибий\n" +
                    "21.RSA\n" +
                    "22.S-Block\n" +
                    "23.ЦП Elgamal\n" +
                    "24.ЦП RSA\n" +
                    "25.Тритемий\n" +
                    "26.Вертикальная перестановка\n" +
                    "27.Вижинер");
            c = scanner.nextInt();
            switch (c){
                case 1:
                    Atbasch.main();
                    break;
                case 2:
                    A51_cipher.main();
                    break;
                case 3:
                    A52_cipher.main();
                    break;
                case 4:
                    AES_cipher.main();
                    break;
                case 5:
                    Belazo.main();
                    break;
                case 6:
                    CardanoGrille.main();
                    break;
                case 7:
                    Cesar.main();
                    break;
                case 8:
                    ClaudeShannon.main();
                    break;
                case 9:
                    DiffieHellman.main();
                    break;
                case 10:
                    ECC.main();
                    break;
                case 11:
                    Elgamal.main();
                    break;
                case 12:
                    Feistel.main();
                    break;
                case 13:
                    GOST341094.main();
                    break;
                case 14:
                    GOST34102012.main();
                    break;
                case 15:
                    Kuznechik.main();
                    break;
                case 16:
                    Magma.main();
                    break;
                case 17:
                    MagmaGamma.main();
                    break;
                case 18:
                    MatrixCipher.main();
                    break;
                case 19:
                    Playfair.main();
                    break;
                case 20:
                    Polibius.main();
                    break;
                case 21:
                    RSA.main();
                    break;
                case 22:
                    SBlock.main();
                    break;
                case 23:
                    SignatureElgamal.main();
                    break;
                case 24:
                    SignatureRSA.main();
                    break;
                case 25:
                    Trithemius.main();
                    break;
                case 26:
                    VerticalTransposition.main();
                    break;
                case 27:
                    Vigenere.main();
                    break;

            }
            System.out.println("Продолжить? Да - 1, Нет - 0");
            c = scanner.nextInt();
        }while (c!=0);
    }
}
