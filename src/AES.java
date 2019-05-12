import java.util.Base64;

public class AES {


    AESKey aesKeys = new AESKey();

    private String[] sBox = {"1001", "0100", "1010",
            "1011", "1101", "0001",
            "1000", "0101", "0110",
            "0010", "0000", "0011",
            "1100", "1110", "1111",
            "0111"};


    public AESKey generateKeys(String key) {
        MixColumns mixColumns = new MixColumns();

        String w0, w1, w2, w3, w4, w5;

        w0 = key.substring(0, 8);
        w1 = key.substring(8, 16);
        w2 = mixColumns.XOR(w0, mixColumns.XOR("10000000", subNibKey(rotationNib(w1)), 8), 8);
        w3 = mixColumns.XOR(w2, w1, 8);
        w4 = mixColumns.XOR(w2, mixColumns.XOR("00110000", subNibKey(rotationNib(w3)), 8), 8);
        w5 = mixColumns.XOR(w4, w3, 8);

        AESKey aesKey = new AESKey();
        aesKey.k0 = key;
        aesKey.k1 = w2 + w3;
        aesKey.k2 = w4 + w5;

        this.aesKeys = aesKey;

        return aesKey;
    }

    public String convertToBase64(String[] text) {
        String longString = "";
        for (String s : text) {
            longString += s;
        }
        return Base64.getEncoder().encodeToString(longString.getBytes());
    }

    public void printKeys() {
        System.out.println("k0: " + aesKeys.k0);
        System.out.println("k1: " + aesKeys.k1);
        System.out.println("k2: " + aesKeys.k2);
    }

    public String decrypt(String[] ciphertext, AESKey key) {

        MixColumns mixColumns = new MixColumns();
        String[] blocks = new String[ciphertext.length];
        String decrypted = "";

        for (int i = 0; i < ciphertext.length; i++) {
            // add a round key2
            String blockDecryption = mixColumns.XOR(ciphertext[i], key.k2, 16);

            // inverse shift row
            blockDecryption = shiftRows(blockDecryption);

            // inverse substitute nibbles
            blockDecryption = inverseSubNibRound(blockDecryption);

            // add round key1
            blockDecryption = mixColumns.XOR(blockDecryption, key.k1, 16);

            // inverse mix column
            blockDecryption = mixColumns.inverseMatrixMultiply(blockDecryption);

            // inverse shift row
            blockDecryption = shiftRows(blockDecryption);

            // inverse sub nibbs
            blockDecryption = inverseSubNibRound(blockDecryption);

            // add round key0
            blockDecryption = mixColumns.XOR(blockDecryption, key.k0, 16);
            blocks[i] = blockDecryption;
        }

        decrypted = convertBinaryToText(blocks);


        return decrypted;
    }


    public String inverseSubNibRound(String nibble) {
        String a = nibble.substring(0, 4);
        String b = nibble.substring(4, 8);
        String c = nibble.substring(8, 12);
        String d = nibble.substring(12, 16);
        return inverseSBox(a) + inverseSBox(b) + inverseSBox(c) + inverseSBox(d);
    }


    public String inverseSBox(String nibble) {
        int foundIndex = 0;
        for (int i = 0; i < sBox.length; i++) {
            if (nibble.equals(sBox[i])) {
                foundIndex = i;
                break;
            }
        }
        String s = Integer.toBinaryString(foundIndex);
        return String.format("%04d", Integer.parseInt(s));
    }


    public String[] encrypt(String plaintext, AESKey key) {


        String[] blocks = convertToBlocks(plaintext);

        String[] encrypted = new String[blocks.length];

        for (int x = 0; x < blocks.length; x++) {

            MixColumns mixColumns = new MixColumns();
            String block = blocks[x];

            // Round key 0
            String round0 = mixColumns.XOR(block, key.k0, 16);

            // Nibble substitution
            String blockEncryption = subNibRound(round0);

            // Shift row
            blockEncryption = shiftRows(blockEncryption);

            // 3. Mix Columns
            blockEncryption = mixColumns.matrixMultiply(blockEncryption);

            //add round
            blockEncryption = mixColumns.XOR(blockEncryption, key.k1, 16);

            //Final Round

            // 1.substitute nibbles
            blockEncryption = subNibRound(blockEncryption);
            //System.out.println(temp);

            //2. Shift row
            blockEncryption = shiftRows(blockEncryption);
            //System.out.println(temp);

            //add round
            blockEncryption = mixColumns.XOR(blockEncryption, key.k2, 16);
            encrypted[x] = blockEncryption;
        }
        return encrypted;
    }


    private String shiftRows(String nibble) {
        String a = nibble.substring(0, 4);
        String b = nibble.substring(4, 8);
        String c = nibble.substring(8, 12);
        String d = nibble.substring(12, 16);
        return a + d + c + b;
    }

    private String subNibRound(String nibble) {
        String a = nibble.substring(0, 4);
        String b = nibble.substring(4, 8);
        String c = nibble.substring(8, 12);
        String d = nibble.substring(12, 16);
        return sBox(a) + sBox(b) + sBox(c) + sBox(d);
    }

    public String subNibKey(String nibble) {
        String leftNib = nibble.substring(0, 4);
        String rightNib = nibble.substring(4, 8);

        return sBox(leftNib) + sBox(rightNib);
    }


    private String sBox(String nibble) {
        int subA = Integer.parseInt(nibble, 2);
        return sBox[subA];
    }

    public String rotationNib(String nibble) {
        String result;

        String leftNib = nibble.substring(0, 4);
        String rightNib = nibble.substring(4, 8);

        result = rightNib + leftNib;

        return result;
    }

    private String[] convertToBlocks(String text) {

        boolean pad = false;

        if (text.length() % 2 != 0)
            pad = true;

        String[] arr = new String[(int) Math.ceil(text.length() / 2.0)];

        for (int x = 0; x < arr.length * 2; x += 2) {
            if (x == arr.length * 2 - 2 && pad) {
                arr[x / 2] = convertTextToBinary(text.substring(x, x + 1)) + "00000000";
                break;
            } else {
                arr[x / 2] = convertTextToBinary(text.substring(x, x + 1)) + convertTextToBinary(text.substring(x + 1, x + 2));
            }
        }

        return arr;
    }


    private String convertBinaryToText(String[] binary) {
        String txt = "";
        for (String s : binary) {
            int charCode = Integer.parseInt(s.substring(0, 8), 2);
            txt += new Character((char) charCode).toString();

            charCode = Integer.parseInt(s.substring(9, 16), 2);
            if (charCode == 0)
                continue;
            else
                txt += new Character((char) charCode).toString();
        }
        return txt;
    }


    private String convertTextToBinary(String ascii) {
        byte[] bytes = ascii.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

}

class AESKey {
    String k0;
    String k1;
    String k2;
}