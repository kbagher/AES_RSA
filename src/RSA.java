import java.math.BigInteger;
import java.util.Random;


public class RSA {

    enum KeyGenerationType {
        Euclidean,
        ExtendedEuclidean
    }

    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;
    private BigInteger p;
    private BigInteger q;
    private int bitLength = 64;
    NumberOperation numOp =  new NumberOperation();


    public void generateKeys(KeyGenerationType type) {
        this.p = getRandomPrime(bitLength, "1");
        this.q = getRandomPrime(bitLength, "1");

        this.N = p.multiply(q);
        this.phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        this.e = getRandomPrime((bitLength / 2), "1");

        if (type == KeyGenerationType.Euclidean) {
            while (numOp.euclideanGCD(e, phi) != 1 && e.compareTo(phi) < 0) {
                e.add(BigInteger.ONE);
            }

            d = e.modInverse(phi);
        }
        else{
            d = numOp.inverseExtendedEuclid(e,phi);
        }
    }

    public void printKeys() {
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("n: " + N);
        System.out.println("e: " + e);
        System.out.println("d: " + d);
    }

    private BigInteger getRandomPrime(int bits, String minimum) {
        Random random = new Random();
        BigInteger min = new BigInteger(minimum);
        BigInteger prime = BigInteger.probablePrime(bits, random);
        while (prime.compareTo(new BigInteger(minimum)) == -1 || !prime.isProbablePrime(0)) {
            prime = BigInteger.probablePrime(bits, random);
        }
        return prime;
    }

    public String sign(String message) {
        return numOp.fastModExp(new BigInteger(message), d, N).toString();
    }

    public boolean verify(String message, String signature) {
        BigInteger verify = numOp.fastModExp(new BigInteger(signature), e, N);
        return verify.compareTo(new BigInteger(message)) == 0;
    }

    public String encrypt(String plaintext) {
        return numOp.fastModExp(new BigInteger(plaintext), e, N).toString();
    }

    public String decrypt(String ciphertext) {
        return numOp.fastModExp(new BigInteger(ciphertext), d, N).toString();
    }

}