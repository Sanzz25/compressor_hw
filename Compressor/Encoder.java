import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Encoder {
    
    private static String File_Input = "C:\\Users\\lenovo\\Desktop\\vlsi CnD\\Compressor\\input.txt";
    private static double MAX_TABLE_SIZE;
    private static String LZWfilename;
    public static void Encode_string(String input_string, double Bit_Length) throws IOException 
    {
    
        MAX_TABLE_SIZE = Math.pow(2, 128);   
        double table_Size = 255;
        Map<String, Integer> TABLE = new HashMap<String, Integer>();
        for (int i = 0; i < 255 ; i++)
        TABLE.put("" + (char) i, i);
        String initString = "";
        List<Integer> encoded_values = new ArrayList<Integer>();
        for (char symbol : input_string.toCharArray()) 
        {
            String Str_Symbol = initString + symbol;
            if (TABLE.containsKey(Str_Symbol))
            {
                initString = Str_Symbol;
            }
            else 
            {
                encoded_values.add(TABLE.get(initString));
                if(table_Size < MAX_TABLE_SIZE)
                TABLE.put(Str_Symbol, (int) table_Size++);
                initString = "" + symbol;
            }
        }

        if (!initString.equals(""))
            encoded_values.add(TABLE.get(initString));
        
        printEncodedValues(encoded_values); // Print the encoded values
        CreateLZWfile(encoded_values); 
        
    }

   
    private static void printEncodedValues(List<Integer> encoded_values) {
        System.out.println("Encoded values:");
        for (int value : encoded_values) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    private static void CreateLZWfile(List<Integer> encoded_values) throws IOException {
        
        BufferedWriter out = null;
        
        LZWfilename = File_Input.substring(0,File_Input.indexOf(".")) + ".lzw";
        
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(LZWfilename),"UTF_16BE")); 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Iterator<Integer> Itr = encoded_values.iterator();
            while (Itr.hasNext()) {
                out.write(Itr.next());
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        
        out.flush();
        out.close();    
    }


    public static void main(String[] args) throws IOException {
        
        int Bit_Length = 12;
        
        
        StringBuilder input_string1 = new StringBuilder();
        
        try (Scanner fileScanner = new Scanner(new File(File_Input), StandardCharsets.UTF_8.name())) {
            while (fileScanner.hasNextLine()) {
                input_string1.append(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    
        Encode_string(input_string1.toString(), Bit_Length);
            
    }
}
