import java.util.Scanner;

public class Decoder
 {

    static int windowSize = 32; // Size of the sliding window
    static int searchBuffer = 2; // Size of the search buffer
    static int lookAheadBuffer = 1; // Size of the look-ahead buffer
    static int counter = 0; // Counter for tokens

    static class Token {
        int offset; // Offset of the longest match
        int lengthOfMatch; // Length of the longest match
        char codeWord; // Code word

        Token(int offset, int lengthOfMatch, char codeWord) {
            this.offset = offset;
            this.lengthOfMatch = lengthOfMatch;
            this.codeWord = codeWord;
        }
    }

    static Token[] compressionLZ77(String input) {
        int inputLength = input.length();
        int finalSize = inputLength - lookAheadBuffer + 1;
        Token[] data = new Token[finalSize];
        int position = 0;
        while (position < inputLength) {
            Token token = new Token(0, 0, input.charAt(position));

            // Calculate the maximum offset based on the search buffer
            int maxOffset = Math.min(position, searchBuffer);

            // Calculate the maximum search length based on the look-ahead buffer
            int maxSearchLength = Math.min(lookAheadBuffer, inputLength - position);

            // Search for the longest match in the search buffer
            for (int offset = 1; offset <= maxOffset; offset++) {
                int len = 0;
                while (len < maxSearchLength && input.charAt(position - offset + len) == input.charAt(position + len)) {
                    len++;
                }

                // Update the token if a longer match is found
                if (len > token.lengthOfMatch) {
                    token.offset = offset;
                    token.lengthOfMatch = len;
                    token.codeWord = input.charAt(position + len);
                }
            }

            data[counter] = token;
            counter++;
            position += token.lengthOfMatch + 1;
        }

        return data;
    }

    static String decompressLZ77(Token[] arr) {
        StringBuilder tmp = new StringBuilder();
        int pos = 0;
        for (int i = 0; i < counter; i++) {
            if (arr[i].offset != 0) {
                int start = pos - arr[i].offset;
                int len = arr[i].lengthOfMatch;
                // Copy the matched substring
                while (len > 0) {
                    tmp.append(tmp.charAt(start));
                    start++;
                    len--;
                    pos++;
                }
            }
            if (arr[i].codeWord != '\0') {
                tmp.append(arr[i].codeWord);
            }
            pos++;
        }

        return tmp.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Input:");
        String input = scanner.nextLine();
        Token[] arr = compressionLZ77(input);

        System.out.println("Compression:");
        for (int i = 0; i < counter; i++) {
            System.out.print(arr[i].codeWord);
        }
        System.out.println();

        System.out.println("Decompression:");
        String str = decompressLZ77(arr);
        System.out.println(str);

        scanner.close();
    }
}
