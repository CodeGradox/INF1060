import java.io.*;
import java.util.ArrayList;

/**
 * Created by Magnus on 11.08.2015.
 */
public class INF1060 {
    static ArrayList<Byte> byteList = new ArrayList<>();

    public static void main(String[] args) {

        if (args.length >= 2) {
            try {
                if (args[0].equals("p")) {          // Print
                    printFile(args[1]);
                } else if (args[0].equals("e")) {   // Encrypt
                    encode(args[1]);
                    writeFile(args[2]);
                } else if (args[0].equals("d")) {   // Decrypt
                    decrypt(args[1]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Missing parameters.");
            } catch (FileNotFoundException e) {
                System.out.println("Input file not found.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Usage: java [p/e/d] input_file output_file");
        }
    }

    /**
     * Encodes input file and stores the values in byteList
     * @param file input file
     * @throws IOException missing file
     */
    static void encode(String file) throws IOException{
        FileInputStream input = new FileInputStream(file);
        int in, cnt = 0;
        byte out = 0;

        while ((in = input.read()) != -1) {
            if (cnt == 4) {
                byteList.add(out);
                cnt = out = 0;
            }

            cnt++;
            out <<= 2;

            if      (in == ':')  out += 1;
            else if (in == '@')  out += 2;
            else if (in == '\n') out += 3;

        }

        if (cnt != 0) {
            int tmp = out << (8 - 2*cnt);
            byteList.add((byte) tmp);
        }
        input.close();
        System.out.println(file + " has been encoded.");
    }

    /**
     * Decrypts the file and prints it out.
     * @param file encrypted file
     * @throws IOException missing file
     */
    static void decrypt(String file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        final char symbols[] = {' ', ':', '@', '\n'};

        for (int in = input.read(); in != -1; in = input.read()) {
            for (int i = 3; i >= 0; i--) {
                char c = (char) ((in >> i*2) & 3); // shift 6/4/2/0 bits to the right, then keep the two LSBs
                System.out.print(symbols[c]); // will always be 
            }
        }

        input.close();
    }

    /**
     * Prints out the content of the given file
     * @param file print this
     * @throws IOException missing file
     */
    static void printFile(String file) throws IOException {
        FileInputStream input = new FileInputStream(file);

        for (int in = input.read(); in != -1; in = input.read()) {
            System.out.print((char) in);
        }
        input.close();
    }

    /**
     * Writes the content of byteList to file
     * @param file the filename of the output file
     * @throws IOException no name given
     */
    static void writeFile(String file) throws IOException {
        FileOutputStream output = new FileOutputStream(file);
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = byteList.get(i).byteValue();
        }

        output.write(bytes);
        output.close();
    }
}
