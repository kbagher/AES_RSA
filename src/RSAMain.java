import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RSAMain {

    private Scanner scan = new Scanner(System.in);

    RSA rsa = new RSA();


    public static void main(String[] args) throws Exception {

        RSAMain main = new RSAMain();
        main.start();

    }

    public void start() {

        RSAMenuInput userChoice;

        while (true) {
            clearScreen();
            userChoice = mainRSAMenu();

            System.out.println("\n");

            if (userChoice == RSAMenuInput.RSA_MENU_GENERATE_KEYS) {
                int keyMenu = keyMenu();
                while (keyMenu < 1 || keyMenu > 2) {
                    keyMenu = keyMenu();
                }
                if (keyMenu == 0) {
                    rsa.generateKeys(RSA.KeyGenerationType.Euclidean);
                } else {
                    rsa.generateKeys(RSA.KeyGenerationType.ExtendedEuclidean);
                }
                clearScreen();
                rsa.printKeys();
                System.out.println("\n\n\nPress return to continue...");
                scan.nextLine();
                scan.nextLine();
            } else if (userChoice == RSAMenuInput.RSA_MENU_ENCRYPT) {
                String num = encryptText();
                String encrypted = rsa.encrypt(num);
                System.out.println("Encrypted text: " + encrypted);
                System.out.println("\n\n\nPress return to continue...");
                scan.nextLine();
                scan.nextLine();
            } else if (userChoice == RSAMenuInput.RSA_MENU_DECRYPT) {
                String num = decryptText();
                String decrypted = rsa.decrypt(num);
                System.out.println("Decrypted text: " + decrypted);
                System.out.println("\n\n\nPress return to continue...");
                scan.nextLine();
                scan.nextLine();
            } else if (userChoice == RSAMenuInput.RSA_MENU_SIGN) {
                String num = signText();
                String signature = rsa.sign(num);
                System.out.println("Signature: " + signature);
                System.out.println("\n\n\nPress return to continue...");
                scan.nextLine();
                scan.nextLine();
            } else if (userChoice == RSAMenuInput.RSA_MENU_VERIFY) {
                boolean result = verifyText();
                if (result)
                    System.out.println("Valid");
                else
                    System.out.println("Invalid");

                System.out.println("\n\n\nPress return to continue...");
                scan.nextLine();
                scan.nextLine();
            }
        }
    }

    private Boolean verifyText() {
        clearScreen();
        System.out.println("\n======= RSA RSA_MENU_VERIFY =======\n");
        System.out.print("\nNumber: ");
        String num = scan.next();
        System.out.print("\nSignature: ");
        String signature = scan.next();
        return rsa.verify(num, signature);
    }

    private String signText() {
        clearScreen();
        System.out.println("\n======= RSA RSA_MENU_SIGN =======\n");
        System.out.print("\nEnter a number: ");
        return scan.next();
    }

    private String encryptText() {
        clearScreen();
        System.out.println("\n======= RSA Encryption =======\n");
        System.out.print("\nEnter a number: ");
        return scan.next();
    }

    private String decryptText() {
        clearScreen();
        System.out.println("\n======= RSA Decryption =======\n");
        System.out.print("\nEnter a ciphertext: ");
        return scan.next();
    }

    private int keyMenu() {
        clearScreen();

        System.out.println("\n======= RSA key generation =======\n");
        System.out.println("[1] Euclidean");
        System.out.println("[2] Extended Euclidean");
        System.out.print("\nDesired function: ");
        return scan.nextInt();
    }

    private RSAMenuInput mainRSAMenu() {
        System.out.println("\n======= RSA Encryption =======\n");
        System.out.println("[1] Generate RSA keys");
        System.out.println("[2] Encrypt");
        System.out.println("[3] Decrypt");
        System.out.println("[4] Sign");
        System.out.println("[5] Verify");
        System.out.print("\nDesired function: ");
        return RSAMenuInput.valueOf(scan.nextInt());
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

enum RSAMenuInput {
    RSA_MENU_GENERATE_KEYS(1),
    RSA_MENU_ENCRYPT(2),
    RSA_MENU_DECRYPT(3),
    RSA_MENU_SIGN(4),
    RSA_MENU_VERIFY(5);

    private int value;
    private static Map map = new HashMap<>();

    RSAMenuInput(int value) {
        this.value = value;
    }

    static {
        for (RSAMenuInput menuType : RSAMenuInput.values()) {
            map.put(menuType.value, menuType);
        }
    }

    public static RSAMenuInput valueOf(int menuType) {
        return (RSAMenuInput) map.get(menuType);
    }

    public int getValue() {
        return value;
    }
}
