package AES;

import java.util.Random;
import java.util.Scanner;


public class AES_cipher {
    public static void main() {
        System.out.println("AES");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст:");
        String text = scanner.nextLine();
        AES_cipher aes = new AES_cipher(text);
        System.out.println("Зашифрованный текст:" + aes.encode());
        System.out.println("Расшифрованный текст:" + aes.decode());
    }

    // матрица для расширения ключа
    final String[][] R_CON = {
            {"01", "00", "00", "00"},
            {"02", "00", "00", "00"},
            {"04", "00", "00", "00"},
            {"08", "00", "00", "00"},
            {"10", "00", "00", "00"},
            {"20", "00", "00", "00"},
            {"40", "00", "00", "00"},
            {"80", "00", "00", "00"},
            {"1b", "00", "00", "00"},
            {"36", "00", "00", "00"},
    };

    // таблица S подстановок для зашифрования
    final String[][] SUBSTITUTE_BOX = {
            {"63", "7c", "77", "7b", "f2", "6b", "6f", "c5", "30", "01", "67", "2b", "fe", "d7", "ab", "76"},
            {"ca", "82", "c9", "7d", "fa", "59", "47", "f0", "ad", "d4", "a2", "af", "9c", "a4", "72", "c0"},
            {"b7", "fd", "93", "26", "36", "3f", "f7", "cc", "34", "a5", "e5", "f1", "71", "d8", "31", "15"},
            {"04", "c7", "23", "c3", "18", "96", "05", "9a", "07", "12", "80", "e2", "eb", "27", "b2", "75"},
            {"09", "83", "2c", "1a", "1b", "6e", "5a", "a0", "52", "3b", "d6", "b3", "29", "e3", "2f", "84"},
            {"53", "d1", "00", "ed", "20", "fc", "b1", "5b", "6a", "cb", "be", "39", "4a", "4c", "58", "cf"},
            {"d0", "ef", "aa", "fb", "43", "4d", "33", "85", "45", "f9", "02", "7f", "50", "3c", "9f", "a8"},
            {"51", "a3", "40", "8f", "92", "9d", "38", "f5", "bc", "b6", "da", "21", "10", "ff", "f3", "d2"},
            {"cd", "0c", "13", "ec", "5f", "97", "44", "17", "c4", "a7", "7e", "3d", "64", "5d", "19", "73"},
            {"60", "81", "4f", "dc", "22", "2a", "90", "88", "46", "ee", "b8", "14", "de", "5e", "0b", "db"},
            {"e0", "32", "3a", "0a", "49", "06", "24", "5c", "c2", "d3", "ac", "62", "91", "95", "e4", "79"},
            {"e7", "c8", "37", "6d", "8d", "d5", "4e", "a9", "6c", "56", "f4", "ea", "65", "7a", "ae", "08"},
            {"ba", "78", "25", "2e", "1c", "a6", "b4", "c6", "e8", "dd", "74", "1f", "4b", "bd", "8b", "8a"},
            {"70", "3e", "b5", "66", "48", "03", "f6", "0e", "61", "35", "57", "b9", "86", "c1", "1d", "9e"},
            {"e1", "f8", "98", "11", "69", "d9", "8e", "94", "9b", "1e", "87", "e9", "ce", "55", "28", "df"},
            {"8c", "a1", "89", "0d", "bf", "e6", "42", "68", "41", "99", "2d", "0f", "b0", "54", "bb", "16"},
    };

    // таблица s подстановок для расшифрования
    final String[][] INVERSE_SUBSTITUTE_BOX = {
            {"52", "09", "6a", "d5", "30", "36", "a5", "38", "bf", "40", "a3", "9e", "81", "f3", "d7", "fb"},
            {"7c", "e3", "39", "82", "9b", "2f", "ff", "87", "34", "8e", "43", "44", "c4", "de", "e9", "cb"},
            {"54", "7b", "94", "32", "a6", "c2", "23", "3d", "ee", "4c", "95", "0b", "42", "fa", "c3", "4e"},
            {"08", "2e", "a1", "66", "28", "d9", "24", "b2", "76", "5b", "a2", "49", "6d", "8b", "d1", "25"},
            {"72", "f8", "f6", "64", "86", "68", "98", "16", "d4", "a4", "5c", "cc", "5d", "65", "b6", "92"},
            {"6c", "70", "48", "50", "fd", "ed", "b9", "da", "5e", "15", "46", "57", "a7", "8d", "9d", "84"},
            {"90", "d8", "ab", "00", "8c", "bc", "d3", "0a", "f7", "e4", "58", "05", "b8", "b3", "45", "06"},
            {"d0", "2c", "1e", "8f", "ca", "3f", "0f", "02", "c1", "af", "bd", "03", "01", "13", "8a", "6b"},
            {"3a", "91", "11", "41", "4f", "67", "dc", "ea", "97", "f2", "cf", "ce", "f0", "b4", "e6", "73"},
            {"96", "ac", "74", "22", "e7", "ad", "35", "85", "e2", "f9", "37", "e8", "1c", "75", "df", "6e"},
            {"47", "f1", "1a", "71", "1d", "29", "c5", "89", "6f", "b7", "62", "0e", "aa", "18", "be", "1b"},
            {"fc", "56", "3e", "4b", "c6", "d2", "79", "20", "9a", "db", "c0", "fe", "78", "cd", "5a", "f4"},
            {"1f", "dd", "a8", "33", "88", "07", "c7", "31", "b1", "12", "10", "59", "27", "80", "ec", "5f"},
            {"60", "51", "7f", "a9", "19", "b5", "4a", "0d", "2d", "e5", "7a", "9f", "93", "c9", "9c", "ef"},
            {"a0", "e0", "3b", "4d", "ae", "2a", "f5", "b0", "c8", "eb", "bb", "3c", "83", "53", "99", "61"},
            {"17", "2b", "04", "7e", "ba", "77", "d6", "26", "e1", "69", "14", "63", "55", "21", "0c", "7d"},
    };

    char[] ptext;//открытый текст
    char[] ctext;//закрытый текст
    char[] dtext;//расшифрованный текст
    String key;//ключ

    AES_cipher(String text) {
        this.ptext = text.toCharArray();
        this.ctext = new char[ptext.length / 12 * 12 + 12];
        this.dtext = new char[ptext.length / 12 * 12 + 12];
        this.key = "";
    }

    public String encode() {//зашифрование
        //-------------расширение ключа--------------------
        genKey();
        String key[][] = new String[4][4];
        int point = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                key[i][j] = Integer.toHexString(Integer.parseInt(this.key.substring(point, point + 8), 2));
                if (key[i][j].length() == 1)
                    key[i][j] = "0" + key[i][j];
                point += 8;
            }
        }
        String[][] rKey = genRKey(key);

        //------------------преобразование текста в блочный вид------------------------
        //каждый символ будет иметь размерность в 11 бит, но в блоке, как и положено, одно слово - 8 бит
        String btext = "";//открытый текст в виде двоичного кода
        String[][][] blockText;
        String bin;//символ в двоичном коде
        for (int i = 0; i < ptext.length; i++) {//преобразование в двоичный код-строку
            bin = Integer.toBinaryString(ptext[i]);
            while (bin.length() < 11) {
                bin = '0' + bin;
            }
            btext += bin;
        }
        if (btext.length() % 128 == 0)
            blockText = new String[btext.length() / 128][4][4];
        else
            blockText = new String[(btext.length() / 128) + 1][4][4];
        point = 0;
        boolean flag = false;//если биты сообщения закончились
        for (int i = 0; i < blockText.length; i++) {
            if (point + 128 > btext.length()) {//если требуемые в блоке следующие 128 битов будут частично отсутствовать, то применяем алгоритм поиска такой границы
                for (int j = 0; j < 4; j++) {
                    if (point + 32 > btext.length()) {//алгоритм поиска границы
                        for (int k = 0; k < 4; k++) {
                            if (point + 8 > btext.length()) {//если следующие 8 битов отсутствуют или частично отсутствуют, то оставшиеся ячейки блока заполняем ascii 2-ным кодом пробела
                                String rembits;//принимаем оставшиеся нереализованные биты и преобразовываем их в 8-битную последовательность
                                if (!flag) {
                                    rembits = btext.substring(point, btext.length());
                                    while (rembits.length() != 8)
                                        rembits = rembits + '0';
                                    blockText[i][j][k] = Integer.toHexString(Integer.parseInt(rembits, 2));
                                    if (blockText[i][j][k].length() == 1)
                                        blockText[i][j][k] = "0" + blockText[i][j][k];
                                    point += 8;
                                    flag = true;
                                    continue;
                                }
                                blockText[i][j][k] = Integer.toHexString(Integer.parseInt("00010000", 2));
                                if (blockText[i][j][k].length() == 1)
                                    blockText[i][j][k] = "0" + blockText[i][j][k];
                                point += 8;
                                continue;
                            }
                            blockText[i][j][k] = Integer.toHexString(Integer.parseInt(btext.substring(point, point + 8), 2));
                            if (blockText[i][j][k].length() == 1)
                                blockText[i][j][k] = "0" + blockText[i][j][k];
                            point += 8;
                        }
                    }
                    if (flag)
                        continue;
                    for (int k = 0; k < 4; k++) {
                        blockText[i][j][k] = Integer.toHexString(Integer.parseInt(btext.substring(point, point + 8), 2));
                        if (blockText[i][j][k].length() == 1)
                            blockText[i][j][k] = "0" + blockText[i][j][k];
                        point += 8;
                    }
                }
            }
            if (flag)
                continue;
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    blockText[i][j][k] = Integer.toHexString(Integer.parseInt(btext.substring(point, point + 8), 2));
                    if (blockText[i][j][k].length() == 1)
                        blockText[i][j][k] = "0" + blockText[i][j][k];
                    point += 8;
                }
            }
        }

        //------------------------------складываем блоки открытого текста с ключом----------------
        blockText = AddRoundKey(blockText, key);

        for (int i = 0; i < 9; i++) {
            //----------------------------SubBytes-------------------------------------
            blockText = SubBytes(blockText);

            //--------------------ShiftRows----------------------------
            blockText = ShiftRows(blockText);

            //--------------------MixColumns----------------------------
            blockText = MixColumns(blockText);

            //------------------AddRoundKey------------------------------
            String[][] RoundKey = new String[4][4];
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    RoundKey[j][k] = rKey[j][i * 4 + k];
                }
            }
            blockText = AddRoundKey(blockText, RoundKey);
        }

        String[][] RoundKey = new String[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                RoundKey[i][j] = rKey[i][36 + j];
            }
        }
        blockText = SubBytes(blockText);
        blockText = ShiftRows(blockText);
        blockText = AddRoundKey(blockText, RoundKey);

        String EncryptBits = "";
        String get;
        for (int i = 0; i < blockText.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    get = Integer.toBinaryString(Integer.parseInt(blockText[i][j][k], 16));
                    while (get.length() != 8)
                        get = '0' + get;
                    EncryptBits += get;
                }
            }
        }

        this.ctext = new char[EncryptBits.length() / 11 + 1];
        point = 0;
        int count = 0;
        while (point < EncryptBits.length()) {
            if (point + 11 > EncryptBits.length())
                this.ctext[count] = (char) Integer.parseInt(EncryptBits.substring(point, EncryptBits.length()), 2);
            else
                this.ctext[count] = (char) Integer.parseInt(EncryptBits.substring(point, point + 11), 2);
            point += 11;
            count++;
        }

        return String.valueOf(this.ctext);
    }

    public String decode() {
        //-------------расширение ключа--------------------
        String key[][] = new String[4][4];
        int point = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                key[i][j] = Integer.toHexString(Integer.parseInt(this.key.substring(point, point + 8), 2));
                if (key[i][j].length() == 1)
                    key[i][j] = "0" + key[i][j];
                point += 8;
            }
        }
        String[][] rKey = genRKey(key);


        String btext = "";
        String[][][] blockText;
        String bin;//символ в двоичном коде
        for (int i = 0; i < ctext.length; i++) {//преобразование в двоичный код-строку
            int l = btext.length();
            if (i == ctext.length - 1) {
                bin = Integer.toBinaryString((int) ctext[i]);
                while (bin.length() < 128 - i * 11 % 128)
                    bin = '0' + bin;
                btext += bin;
            } else {
                bin = Integer.toBinaryString((int) ctext[i]);
                while (bin.length() < 11) {
                    bin = '0' + bin;
                }
                btext += bin;
            }
        }

        blockText = new String[btext.length() / 128][4][4];
        point = 0;
        for (int i = 0; i < blockText.length && point != btext.length(); i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    blockText[i][j][k] = Integer.toHexString(Integer.parseInt(btext.substring(point, point + 8), 2));
                    if (blockText[i][j][k].length() == 1)
                        blockText[i][j][k] = '0' + blockText[i][j][k];
                    point += 8;
                }
            }
        }

        String[][] RoundKey = new String[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                RoundKey[i][j] = rKey[i][36 + j];
            }
        }

        blockText = AddRoundKey(blockText, RoundKey);
        blockText = InvShiftRows(blockText);
        blockText = InvSubBytes(blockText);

        for (int i = 8; i >= 0; i--) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    RoundKey[j][k] = rKey[j][i * 4 + k];
                }
            }
            blockText = AddRoundKey(blockText, RoundKey);
            blockText = InvMixColumns(blockText);
            blockText = InvShiftRows(blockText);
            blockText = InvSubBytes(blockText);

        }

        blockText = AddRoundKey(blockText, key);

        String DecryptBits = "";
        String get;
        for (int i = 0; i < blockText.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    get = Integer.toBinaryString(Integer.parseInt(blockText[i][j][k], 16));
                    while (get.length() != 8)
                        get = '0' + get;
                    DecryptBits += get;
                }
            }
        }

        this.dtext = new char[DecryptBits.length()/11+1];
        point = 0;
        int count = 0;
        int l = DecryptBits.length();
        while (point < DecryptBits.length()) {
            if (point + 11 > DecryptBits.length())
                this.dtext[count] += (char) Integer.parseInt(DecryptBits.substring(point, DecryptBits.length()), 2);
            else
                this.dtext[count] += (char) Integer.parseInt(DecryptBits.substring(point, point + 11), 2);
            point += 11;
            count++;
        }

        return String.valueOf(this.dtext);
    }

    private void genKey() {// генерация первичного ключа в двоичной СС
        Random rd = new Random();
        for (int i = 0; i < 128; i++) this.key += rd.nextInt(2);
    }

    private String[][] genRKey(String[][] key) {// расширение ключа
        String[][] rKey = new String[4][40];
        for (int i = 0; i < 40; i++) {
            if (i == 0) {
                rKey[0][0] = key[1][3];
                rKey[1][0] = key[2][3];
                rKey[2][0] = key[3][3];
                rKey[3][0] = key[0][3];
                for (int j = 0; j < 4; j++) {
                    rKey[j][i] = Sub(rKey[j][i]);
                    rKey[j][i] = xorHex(xorHex(key[j][i], rKey[j][i]), R_CON[i][j]);
                }
            } else if (i < 4) {
                for (int j = 0; j < 4; j++) {
                    rKey[j][i] = xorHex(key[j][i], rKey[j][i - 1]);
                }
            } else if (i % 4 == 0) {
                rKey[0][i] = rKey[1][i - 1];
                rKey[1][i] = rKey[2][i - 1];
                rKey[2][i] = rKey[3][i - 1];
                rKey[3][i] = rKey[0][i - 1];
                for (int j = 0; j < 4; j++) {
                    rKey[j][i] = Sub(rKey[j][i]);
                    rKey[j][i] = xorHex(xorHex(rKey[j][i - 4], rKey[j][i]), R_CON[i / 4][j]);
                }
            } else {
                for (int j = 0; j < 4; j++) {
                    rKey[j][i] = xorHex(rKey[j][i - 1], rKey[j][i - 4]);
                }
            }
        }
        return rKey;
    }

    private String[][][] SubBytes(String[][][] block) {// функция s подстановки
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    block[i][j][k] = Sub(block[i][j][k]);
                }
            }
        }
        return block;
    }

    private String Sub(String hex) {// подстановка
        int a = Integer.parseInt(String.valueOf(hex.substring(0, 1)), 16);
        int b = Integer.parseInt(String.valueOf(hex.substring(1)), 16);
        return SUBSTITUTE_BOX[a][b];
    }

    private String[][][] InvSubBytes(String[][][] block) {// функция обратной s обстановки
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    block[i][j][k] = InvSub(block[i][j][k]);
                }
            }
        }
        return block;
    }

    private String InvSub(String hex) {// обратная s подстановка
        int a = Integer.parseInt(String.valueOf(hex.substring(0, 1)), 16);
        int b = Integer.parseInt(String.valueOf(hex.substring(1)), 16);
        return INVERSE_SUBSTITUTE_BOX[a][b];
    }

    private String[][][] ShiftRows(String[][][] box) {// функция сдвига
        for (int i = 0; i < box.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0)
                    continue;
                else if (j == 1) {
                    String temp = box[i][j][0];
                    box[i][j][0] = box[i][j][1];
                    box[i][j][1] = box[i][j][2];
                    box[i][j][2] = box[i][j][3];
                    box[i][j][3] = temp;
                } else if (j == 2) {
                    String temp1 = box[i][j][0];
                    String temp2 = box[i][j][1];
                    box[i][j][0] = box[i][j][2];
                    box[i][j][1] = box[i][j][3];
                    box[i][j][2] = temp1;
                    box[i][j][3] = temp2;
                } else {
                    String temp = box[i][j][3];
                    box[i][j][3] = box[i][j][2];
                    box[i][j][2] = box[i][j][1];
                    box[i][j][1] = box[i][j][0];
                    box[i][j][0] = temp;
                }
            }
        }
        return box;
    }

    private String[][][] InvShiftRows(String[][][] box) {// функция обратного сдвига
        for (int i = 0; i < box.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0)
                    continue;
                else if (j == 1) {
                    String temp = box[i][j][3];
                    box[i][j][3] = box[i][j][2];
                    box[i][j][2] = box[i][j][1];
                    box[i][j][1] = box[i][j][0];
                    box[i][j][0] = temp;
                } else if (j == 2) {
                    String temp1 = box[i][j][2];
                    String temp2 = box[i][j][3];
                    box[i][j][3] = box[i][j][1];
                    box[i][j][2] = box[i][j][0];
                    box[i][j][1] = temp2;
                    box[i][j][0] = temp1;
                } else {
                    String temp = box[i][j][0];
                    box[i][j][0] = box[i][j][1];
                    box[i][j][1] = box[i][j][2];
                    box[i][j][2] = box[i][j][3];
                    box[i][j][3] = temp;
                }
            }
        }
        return box;
    }

    private String[][][] MixColumns(String[][][] box) {// функция перемешивания
        String[][] state = new String[4][4];
        for (int i = 0; i < box.length; i++) {
            for (int j = 0; j < 4; j++) {
                int a, b, c, d;
                state[0][j] = Integer.toHexString(Integer.parseInt(helpMul2(box[i][0][j]), 16) ^
                        Integer.parseInt(helpMul3(box[i][1][j]), 16) ^
                        Integer.parseInt(box[i][2][j], 16) ^
                        Integer.parseInt(box[i][3][j], 16));
                if (state[0][j].length() == 1)
                    state[0][j] = '0' + state[0][j];

                state[1][j] = Integer.toHexString(Integer.parseInt(box[i][0][j], 16) ^
                        Integer.parseInt(helpMul2(box[i][1][j]), 16) ^
                        Integer.parseInt(helpMul3(box[i][2][j]), 16) ^
                        Integer.parseInt(box[i][3][j], 16));
                if (state[1][j].length() == 1)
                    state[1][j] = '0' + state[1][j];

                state[2][j] = Integer.toHexString(Integer.parseInt(box[i][0][j], 16) ^
                        Integer.parseInt(box[i][1][j], 16) ^
                        Integer.parseInt(helpMul2(box[i][2][j]), 16) ^
                        Integer.parseInt(helpMul3(box[i][3][j]), 16));
                if (state[2][j].length() == 1)
                    state[2][j] = '0' + state[2][j];

                state[3][j] = Integer.toHexString(Integer.parseInt(helpMul3(box[i][0][j]), 16) ^
                        Integer.parseInt(box[i][1][j], 16) ^
                        Integer.parseInt(box[i][2][j], 16) ^
                        Integer.parseInt(helpMul2(box[i][3][j]), 16));
                if (state[3][j].length() == 1)
                    state[3][j] = '0' + state[3][j];
            }
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    box[i][j][k] = state[j][k];
                }
            }
        }
        return box;
    }

    private String helpMul2(String el) {// функция-помощник для перемешивания 2x
        int d = Integer.parseInt(el, 16);
        if (d < 128) {
            d = (d << 1);
            el = Integer.toBinaryString(d);
            el = Integer.toHexString(Integer.parseInt(el, 2));
        } else {
            d = (d << 1);
            el = Integer.toBinaryString(d);
            el = Integer.toHexString(Integer.parseInt(el, 2) ^ Integer.parseInt("1b", 16));
        }
        if (Integer.parseInt(el, 16) > 255)
            el = Integer.toHexString(Integer.parseInt(el, 16) - 256);
        return el;
    }

    private String helpMul3(String el) {// функция-помощник для перемешивания 3x
        int d = Integer.parseInt(el, 16);
        String res;
        if (d < 128) {
            d = (d << 1);
            res = Integer.toBinaryString(d);
            res = Integer.toHexString(Integer.parseInt(res, 2));
        } else {
            d = (d << 1);
            res = Integer.toBinaryString(d);
            res = Integer.toHexString(Integer.parseInt(res, 2) ^ Integer.parseInt("1b", 16));
        }
        if (Integer.parseInt(res, 16) > 255)
            res = Integer.toHexString(Integer.parseInt(res, 16) - 256);
        res = Integer.toHexString(Integer.parseInt(res, 16) ^ Integer.parseInt(el, 16));
        return res;
    }

    private String[][][] InvMixColumns(String[][][] box) {// функция обратного перемешивания
        String[][] state = new String[4][4];
        for (int i = 0; i < box.length; i++) {
            for (int j = 0; j < 4; j++) {
                int a, b, c, d;
                state[0][j] = Integer.toHexString(Integer.parseInt(mul0e(box[i][0][j]), 16) ^
                        Integer.parseInt(mul0b(box[i][1][j]), 16) ^
                        Integer.parseInt(mul0d(box[i][2][j]), 16) ^
                        Integer.parseInt(mul09(box[i][3][j]), 16));
                if (state[0][j].length() == 1)
                    state[0][j] = '0' + state[0][j];

                state[1][j] = Integer.toHexString(Integer.parseInt(mul09(box[i][0][j]), 16) ^
                        Integer.parseInt(mul0e(box[i][1][j]), 16) ^
                        Integer.parseInt(mul0b(box[i][2][j]), 16) ^
                        Integer.parseInt(mul0d(box[i][3][j]), 16));
                if (state[1][j].length() == 1)
                    state[1][j] = '0' + state[1][j];

                state[2][j] = Integer.toHexString(Integer.parseInt(mul0d(box[i][0][j]), 16) ^
                        Integer.parseInt(mul09(box[i][1][j]), 16) ^
                        Integer.parseInt(mul0e(box[i][2][j]), 16) ^
                        Integer.parseInt(mul0b(box[i][3][j]), 16));
                if (state[2][j].length() == 1)
                    state[2][j] = '0' + state[2][j];

                state[3][j] = Integer.toHexString(Integer.parseInt(mul0b(box[i][0][j]), 16) ^
                        Integer.parseInt(mul0d(box[i][1][j]), 16) ^
                        Integer.parseInt(mul09(box[i][2][j]), 16) ^
                        Integer.parseInt(mul0e(box[i][3][j]), 16));
                if (state[3][j].length() == 1)
                    state[3][j] = '0' + state[3][j];
            }
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    box[i][j][k] = state[j][k];
                }
            }
        }
        return box;
    }

    private String mul09(String el) {// функция-помощник для обратного перемешивания 09x
        return Integer.toHexString(Integer.parseInt(helpMul2(helpMul2(helpMul2(el))), 16) ^ Integer.parseInt(el, 16));
    }

    private String mul0b(String el) {// функция-помощник для обратного перемешивания 0bx
        return Integer.toHexString(Integer.parseInt(helpMul2(helpMul2(helpMul2(el))), 16) ^ Integer.parseInt(helpMul2(el), 16) ^ Integer.parseInt(el, 16));
    }

    private String mul0d(String el) {// функция-помощник для обратного перемешивания 0dx
        return Integer.toHexString(Integer.parseInt(helpMul2(helpMul2(helpMul2(el))), 16) ^ Integer.parseInt(helpMul2(helpMul2(el)), 16) ^ Integer.parseInt(el, 16));
    }

    private String mul0e(String el) {// функция-помощник для обратного перемешивания 0ex
        return Integer.toHexString(Integer.parseInt(helpMul2(helpMul2(helpMul2(el))), 16) ^ Integer.parseInt(helpMul2(helpMul2(el)), 16) ^ Integer.parseInt(helpMul2(el), 16));
    }


    private String[][][] AddRoundKey(String[][][] block, String[][] key) {// xor фукнция ключа с блоками
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    block[i][j][k] = xorHex(block[i][j][k], key[j][k]);
                    if (block[i][j][k].length() == 1)
                        block[i][j][k] = '0' + block[i][j][k];
                }
            }
        }
        return block;
    }

    private String xorHex(String a, String b) {//операция xor над шестнадцатеричными числами
        int d1 = Integer.parseInt(String.valueOf(a), 16);
        int d2 = Integer.parseInt(String.valueOf(b), 16);
        int res = d1 ^ d2;
        if (res < 16)
            return String.valueOf("0" + Integer.toHexString(d1 ^ d2));
        else
            return Integer.toHexString(d1 ^ d2);
    }


}
