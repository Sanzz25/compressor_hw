import java.util.ArrayList;
public class LZ77Compression 
{
    public static final int SEARCH_BUFFER = 4;
    public static final int LOOK_AHEAD_BUFFER =3;
    public static class Token 
    {
        public int offset;
        public int length;
        public char character;
        public Token(int offset, int length, char character) 
        {
            this.offset = offset;
            this.length = length;
            this.character = character;
        }
    }
    public static ArrayList<Token> compress(String input)
    {
        int inputLength = input.length();
        int finalSize = inputLength - LOOK_AHEAD_BUFFER + 1;
        ArrayList<Token> tokens = new ArrayList<>(finalSize);
        int position = 0;
        while (position < inputLength) {
            Token token = new Token(0, 0, input.charAt(position));
            int maxOffset = Math.min(position, SEARCH_BUFFER);
            int maxSearchLength = Math.min(LOOK_AHEAD_BUFFER, inputLength - position);
            
            for (int offset = 1; offset <= maxOffset; offset++) 
            {     int length = 0;           
                while (length < maxSearchLength && position - offset + length < inputLength && input.charAt(position - offset + length) == input.charAt(position + length))
                {
                    length++;
                }
                if (length > token.length) 
                {
                    token.offset = offset;
                    token.length = length;
                    token.character = input.charAt(position + length);
                }
            }
            tokens.add(token);
            position += token.length + 1;
        }

        return tokens;
    }
    public static void main(String[] args) 
    {
        String input = "11010110100110010100100101101011";
        System.out.println("Input Data Is");
        System.out.println(input);
        ArrayList<Token> compressedTokens = compress(input);
        System.out.println("Compressed Tokens:");
        for (Token token : compressedTokens) 
        {
            System.out.print( token.character);
            
        }
    }
}
