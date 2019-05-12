import java.math.BigInteger;

public class NumberOperation {


    public int euclideanGCD(BigInteger a, BigInteger b) {
        BigInteger gcd = a.gcd(b);
        return gcd.intValue();
    }

    public BigInteger fastModExp(BigInteger num1, BigInteger num2, BigInteger n) {

        num1 = num1.mod(n);

        BigInteger result = new BigInteger("1");

        BigInteger tmp = num1;

        while (num2.compareTo(new BigInteger("0")) > 0) {

            BigInteger leastSignificantBit = num2.mod(new BigInteger("2"));
            num2 = num2.divide(new BigInteger("2"));

            if (leastSignificantBit.compareTo(new BigInteger("1")) == 0) {
                result = result.multiply(tmp);
                result = result.mod(n);
            }

            tmp = tmp.multiply(tmp);
            tmp = tmp.mod(n);
        }
        return result;
    }

    public BigInteger inverseExtendedEuclid(BigInteger a, BigInteger N) {

        BigInteger[] ansNumbers = extendedEuclid(a, N);

        if (ansNumbers[1].compareTo(BigInteger.ZERO) == 1)
            return ansNumbers[1];

        else return ansNumbers[1].add(N);
    }

    private BigInteger[] extendedEuclid(BigInteger a, BigInteger N) {
        BigInteger[] ansNumbers = new BigInteger[3];
        BigInteger ax, yN;

        if (N.equals(BigInteger.ZERO)) {
            ansNumbers[0] = a;
            ansNumbers[1] = BigInteger.ONE;
            ansNumbers[2] = BigInteger.ZERO;
            return ansNumbers;
        }

        ansNumbers = extendedEuclid(N, a.mod(N));

        ax = ansNumbers[1];
        yN = ansNumbers[2];
        ansNumbers[1] = yN;
        BigInteger temp = a.divide(N);
        temp = yN.multiply(temp);
        ansNumbers[2] = ax.subtract(temp);


        return ansNumbers;
    }

}
