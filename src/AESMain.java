import java.io.BufferedInputStream;
import java.util.*;

public class AESMain {

    private Scanner scan = new Scanner(System.in);

    AES aes = new AES();

    public static void main(String[] args) throws Exception {
        AESMain main = new AESMain();
        main.start();
    }

    public void start() {

        AESMenuInput userChoice;
        AESKey aesKeys = new AESKey();

        while (true) {
            clearScreen();
            scan = new Scanner(System.in);
            userChoice = mainAESMenu();
            System.out.println("\n");

            if (userChoice == AESMenuInput.AES_MENU_GENERATE_KEYS) {
                clearScreen();
                String key = generateKeyText();
                System.out.println();
                aesKeys = aes.generateKeys(key);
                aes.printKeys();
                System.out.println("\n\n\nPress return to continue...");
                scan.nextLine();
                scan.nextLine();
            } else if (userChoice == AESMenuInput.AES_MENU_ENCRYPT) {
                String num = encryptText();
                System.out.println(num);
                String encrypted[] = aes.encrypt(num, aesKeys);
                System.out.println("Ciphertext blocks: ");
                for (String block : encrypted) {
                    System.out.println(block);
                }
                System.out.println("Base64 cipher: " + aes.convertToBase64(encrypted));

//                String[] de = {"1000111101111000", "1111110010111111"};

                System.out.println("\nPlaintext: " + aes.decrypt(encrypted, aesKeys));
                System.out.println("\n\n\nPress return to continue...");
                scan.nextLine();
            }
        }
    }


    private String generateKeyText() {
        clearScreen();
        scan = new Scanner(System.in);
        System.out.println("\n======= AES Key Generation =======\n");
        System.out.print("\nEnter 16 bit key: ");
        return scan.nextLine();
    }

    private String encryptText() {
        clearScreen();
        scan = new Scanner(System.in);
        System.out.println("\n======= AES Encryption =======\n");
        System.out.print("\nEnter a plaintext: ");
        return scan.nextLine();
    }

    private AESMenuInput mainAESMenu() {
        System.out.println("\n======= AES Encryption =======\n");
        System.out.println("[1] Generate AES keys");
        System.out.println("[2] Encrypt and Decrypt");
        System.out.print("\nDesired function: ");
        return AESMenuInput.valueOf(scan.nextInt());
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}

enum AESMenuInput {
    AES_MENU_GENERATE_KEYS(1),
    AES_MENU_ENCRYPT(2);

    private int value;
    private static Map map = new HashMap<>();

    AESMenuInput(int value) {
        this.value = value;
    }

    static {
        for (AESMenuInput menuType : AESMenuInput.values()) {
            map.put(menuType.value, menuType);
        }
    }

    public static AESMenuInput valueOf(int menuType) {
        return (AESMenuInput) map.get(menuType);
    }

    public int getValue() {
        return value;
    }
}