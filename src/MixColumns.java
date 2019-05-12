import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MixColumns {

    private String s00;
    private String s01;
    private String s10;
    private String s11;

    private final Map<String, String> gfLookupTable2; // contains multiplication by 2 only
    private final Map<String, String> gfLookupTable4; // contains multiplication by 4 only
    private final Map<String, String> gfLookupTable9; // contains multiplication by 9 only

    public MixColumns() {
        gfLookupTable2 = new HashMap<>();
        gfLookupTable4 = new HashMap<>();
        gfLookupTable9 = new HashMap<>();
        populateGFTables();
    }

    public String matrixMultiply(String bitBinary) {
        populateS(bitBinary);

        // s'00
        String sR00 = XOR(s00, gfLookupTable4.get(s10),4);
        // s'01
        String sR01 = XOR(s01, gfLookupTable4.get(s11),4);
        // s'10
        String sR10 = XOR(gfLookupTable4.get(s00), s10,4);
        // s'11
        String sR11 = XOR(gfLookupTable4.get(s01), s11,4);

        return sR00 + sR10 + sR01 + sR11;
    }


    public String inverseMatrixMultiply(String bitBinary) {
        populateS(bitBinary);

        // s'00
        String sR00 = XOR(gfLookupTable9.get(s00), gfLookupTable2.get(s01), 4);
        // s'01
        String sR01 = XOR(gfLookupTable2.get(s00), gfLookupTable9.get(s01), 4);
        // s'10
        String sR10 = XOR(gfLookupTable9.get(s10), gfLookupTable2.get(s11), 4);
        // s'11
        String sR11 = XOR(gfLookupTable2.get(s10), gfLookupTable9.get(s11), 4);

        return sR00 + sR10 + sR01 + sR11;
    }


    private void populateGFTables() {
        gfLookupTable2.put("0000", "0000"); // multiply by 0
        gfLookupTable2.put("0001", "0010"); // multiply by 1
        gfLookupTable2.put("0010", "0100"); // multiply by 2
        gfLookupTable2.put("0011", "0110"); // multiply by 3
        gfLookupTable2.put("0100", "1000"); // multiply by 4
        gfLookupTable2.put("0101", "1010"); // multiply by 5
        gfLookupTable2.put("0110", "1100"); // multiply by 6
        gfLookupTable2.put("0111", "1110"); // multiply by 7
        gfLookupTable2.put("1000", "0011"); // multiply by 8
        gfLookupTable2.put("1001", "0001"); // multiply by 9
        gfLookupTable2.put("1010", "0111"); // multiply by 10 (A)
        gfLookupTable2.put("1011", "0101"); // multiply by 11 (B)
        gfLookupTable2.put("1100", "1011"); // multiply by 12 (C)
        gfLookupTable2.put("1101", "1001"); // multiply by 13 (D)
        gfLookupTable2.put("1110", "1111"); // multiply by 14 (E)
        gfLookupTable2.put("1111", "1101"); // multiply by 15 (F)


        gfLookupTable4.put("0000", "0000"); // multiply by 0
        gfLookupTable4.put("0001", "0100"); // multiply by 1
        gfLookupTable4.put("0010", "1000"); // multiply by 2
        gfLookupTable4.put("0011", "1100"); // multiply by 3
        gfLookupTable4.put("0100", "0011"); // multiply by 4
        gfLookupTable4.put("0101", "0111"); // multiply by 5
        gfLookupTable4.put("0110", "1011"); // multiply by 6
        gfLookupTable4.put("0111", "1111"); // multiply by 7
        gfLookupTable4.put("1000", "0110"); // multiply by 8
        gfLookupTable4.put("1001", "0010"); // multiply by 9
        gfLookupTable4.put("1010", "1110"); // multiply by 10 (A)
        gfLookupTable4.put("1011", "1010"); // multiply by 11 (B)
        gfLookupTable4.put("1100", "0101"); // multiply by 12 (C)
        gfLookupTable4.put("1101", "0001"); // multiply by 13 (D)
        gfLookupTable4.put("1110", "1101"); // multiply by 14 (E)
        gfLookupTable4.put("1111", "1001"); // multiply by 15 (F)

        gfLookupTable9.put("0000", "0000"); // multiply by 0
        gfLookupTable9.put("0001", "1001"); // multiply by 1
        gfLookupTable9.put("0010", "0001"); // multiply by 2
        gfLookupTable9.put("0011", "1000"); // multiply by 3
        gfLookupTable9.put("0100", "0010"); // multiply by 4
        gfLookupTable9.put("0101", "1011"); // multiply by 5
        gfLookupTable9.put("0110", "0011"); // multiply by 6
        gfLookupTable9.put("0111", "1010"); // multiply by 7
        gfLookupTable9.put("1000", "0100"); // multiply by 8
        gfLookupTable9.put("1001", "1101"); // multiply by 9
        gfLookupTable9.put("1010", "0101"); // multiply by 10 (A)
        gfLookupTable9.put("1011", "1100"); // multiply by 11 (B)
        gfLookupTable9.put("1100", "0110"); // multiply by 12 (C)
        gfLookupTable9.put("1101", "1111"); // multiply by 13 (D)
        gfLookupTable9.put("1110", "0111"); // multiply by 14 (E)
        gfLookupTable9.put("1111", "1110"); // multiply by 15 (F)

    }

    private void populateS(String bitBinary) {
        Matcher m = Pattern.compile(".{1,4}").matcher(bitBinary);
        s00 = m.find() ? bitBinary.substring(m.start(), m.end()) : "";
        s01 = m.find() ? bitBinary.substring(m.start(), m.end()) : "";
        s10 = m.find() ? bitBinary.substring(m.start(), m.end()) : "";
        s11 = m.find() ? bitBinary.substring(m.start(), m.end()) : "";
    }

    public String XOR(String num1, String num2, int pad) {

        long intNum1 = Integer.parseInt(num1, 2);
        long intNum2 = Integer.parseInt(num2, 2);
        long result = intNum1 ^ intNum2;

        String s = Long.toBinaryString(result);

        s = String.format("%0" + pad + "d", Long.parseLong(s));

        return s;
    }

}
