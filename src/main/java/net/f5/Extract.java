package net.f5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.f5.crypt.F5Random;
import net.f5.crypt.Permutation;
import net.f5.ortega.HuffmanDecode;


public class Extract {

    private static byte[] carrier; // carrier data

    private static int[] coeff; // dct values

    public static void extract(final InputStream fis, final int flength, final OutputStream fos, final String password)
            throws IOException {
        carrier = new byte[flength];
        fis.read(carrier);
        final HuffmanDecode hd = new HuffmanDecode(carrier);
        System.out.println("Huffman decoding starts");
        coeff = hd.decode();
        System.out.println("Permutation starts");
        final F5Random random = new F5Random(password.getBytes());
        final Permutation permutation = new Permutation(coeff.length, random);
        System.out.println(coeff.length + " indices shuffled");
        int extractedByte = 0;
        int availableExtractedBits = 0;
        int extractedFileLength = 0;
        int nBytesExtracted = 0;
        int shuffledIndex = 0;
        int extractedBit;
        int i;
        System.out.println("Extraction starts");

        // extract length information
        for (i = 0; availableExtractedBits < 32; i++) {
            shuffledIndex = permutation.getShuffled(i);
            if (shuffledIndex % 64 == 0) {
                continue; // skip DC coefficients
            }
            shuffledIndex = shuffledIndex - shuffledIndex % 64 + HuffmanDecode.deZigZag[shuffledIndex % 64];
            if (coeff[shuffledIndex] == 0) {
                continue; // skip zeroes
            }
            if (coeff[shuffledIndex] > 0) {
                extractedBit = coeff[shuffledIndex] & 1;
            } else {
                extractedBit = 1 - (coeff[shuffledIndex] & 1);
            }
            extractedFileLength |= extractedBit << availableExtractedBits++;
        }
        // remove pseudo random pad
        extractedFileLength ^= random.getNextByte();
        extractedFileLength ^= random.getNextByte() << 8;
        extractedFileLength ^= random.getNextByte() << 16;
        extractedFileLength ^= random.getNextByte() << 24;
        int k = extractedFileLength >> 24;
        k %= 32;
        final int n = (1 << k) - 1;
        extractedFileLength &= 0x007fffff;
        System.out.println("Length of embedded file: " + extractedFileLength + " bytes");
        availableExtractedBits = 0;
        if (n > 0) {
            int startOfN = i;
            int hash;
            System.out.println("(1, " + n + ", " + k + ") code used");
            extractingLoop:
            do {
                // 1. read n places, and calculate k bits
                hash = 0;
                int code = 1;
                for (i = 0; code <= n; i++) {
                    // check for pending end of coeff
                    if (startOfN + i >= coeff.length) {
                        break extractingLoop;
                    }
                    shuffledIndex = permutation.getShuffled(startOfN + i);
                    if (shuffledIndex % 64 == 0) {
                        continue; // skip DC coefficients
                    }
                    shuffledIndex = shuffledIndex - shuffledIndex % 64 + HuffmanDecode.deZigZag[shuffledIndex % 64];
                    if (coeff[shuffledIndex] == 0) {
                        continue; // skip zeroes
                    }
                    if (coeff[shuffledIndex] > 0) {
                        extractedBit = coeff[shuffledIndex] & 1;
                    } else {
                        extractedBit = 1 - (coeff[shuffledIndex] & 1);
                    }
                    if (extractedBit == 1) {
                        hash ^= code;
                    }
                    code++;
                }
                startOfN += i;
                // 2. write k bits bytewise
                for (i = 0; i < k; i++) {
                    extractedByte |= (hash >> i & 1) << availableExtractedBits++;
                    if (availableExtractedBits == 8) {
                        // remove pseudo random pad
                        extractedByte ^= random.getNextByte();
                        fos.write((byte) extractedByte);
                        extractedByte = 0;
                        availableExtractedBits = 0;
                        nBytesExtracted++;
                        // check for pending end of embedded data
                        if (nBytesExtracted == extractedFileLength) {
                            break extractingLoop;
                        }
                    }
                }
            } while (true);
        } else {
            System.out.println("Default code used");
            for (; i < coeff.length; i++) {
                shuffledIndex = permutation.getShuffled(i);
                if (shuffledIndex % 64 == 0) {
                    continue; // skip DC coefficients
                }
                shuffledIndex = shuffledIndex - shuffledIndex % 64 + HuffmanDecode.deZigZag[shuffledIndex % 64];
                if (coeff[shuffledIndex] == 0) {
                    continue; // skip zeroes
                }
                if (coeff[shuffledIndex] > 0) {
                    extractedBit = coeff[shuffledIndex] & 1;
                } else {
                    extractedBit = 1 - (coeff[shuffledIndex] & 1);
                }
                extractedByte |= extractedBit << availableExtractedBits++;
                if (availableExtractedBits == 8) {
                    // remove pseudo random pad
                    extractedByte ^= random.getNextByte();
                    fos.write((byte) extractedByte);
                    extractedByte = 0;
                    availableExtractedBits = 0;
                    nBytesExtracted++;
                    if (nBytesExtracted == extractedFileLength) {
                        break;
                    }
                }
            }
        }
        if (nBytesExtracted < extractedFileLength) {
            System.out.println("Incomplete file: only " + nBytesExtracted + " of " + extractedFileLength
                    + " bytes extracted");
        }
    }

    public static void extract(String password, String decodedMessageFilename, String encodedFilePath) {
        try {
            File encodedFile = new File(encodedFilePath);
            final FileInputStream fis = new FileInputStream(encodedFile);
            FileOutputStream fos = new FileOutputStream(new File(decodedMessageFilename));
            extract(fis, (int) encodedFile.length(), fos, password);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
