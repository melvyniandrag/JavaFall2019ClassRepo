package rsf.astron.jpl;

// Class to convert ASCII or binary format data as it comes from the JPL to a 
// Java-friendly format. The result is a file that can be loaded with JPLData.load().

import java.io.StreamTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;

import rsf.ui.ChoiceDialogRadio;
import rsf.ui.LoadOrSaveDialog;


public class JPLConverter {
  
  public static void main(String[] args) {
    
    // Convert a binary or an ASCII file?
    String[] choice = new String[2];
    choice[0] = "Convert ASCII Ephemeris File";
    choice[1] = "Convert Binary Ephemeris File";
    int fileType = (new ChoiceDialogRadio(choice)).getChoice();
    if (fileType < 0)
      System.exit(0);
    JPLData convertedData = null;
    if (fileType == 0)
      {
        // Convert an ASCII file. Find the header file, then the data file.
        String[] headerChoice = LoadOrSaveDialog.getLoadChoice( "Open ASCII Header File");
        if (headerChoice == null)
          System.exit(0);
        String[] dataChoice = LoadOrSaveDialog.getLoadChoice( "Open ASCII Data File");
        if (dataChoice == null)
          System.exit(0);

        convertedData = loadFromAsciiFile(headerChoice,dataChoice);
      }
    else
      {
        // Convert a binary file.
        String[] binaryChoice = LoadOrSaveDialog.getLoadChoice( "Open Binary Data File");
        if (binaryChoice == null)
          System.exit(0);
        convertedData = loadFromUnixBinFile(binaryChoice[0],binaryChoice[1]);
      }
    
    // Save the converted data.
    choice = LoadOrSaveDialog.getSaveChoice("Save in Java Format");
    if (choice == null)
      System.exit(0);
    convertedData.save(choice[0],choice[1]);
    System.exit(0);
  }
  
  private static JPLData loadFromAsciiFile(String[] headerLoc,String[] dataLoc) {

    // Load data in ASCII format. The location arguments have the directory
    // at the zero index and the file name at the 1 index.
    JPLData answer = new JPLData();
    
    // Open the header file.
    StreamTokenizer tokenizer = null;
    BufferedReader br = null;
    FileReader reader = null;
    try {
      File hFile = new File(headerLoc[0],headerLoc[1]);
      reader = new FileReader(hFile);
      br = new BufferedReader(reader);
      tokenizer = new StreamTokenizer(br);
      // For some reason '=' confuses the tokenizer.
      tokenizer.whitespaceChars('=','=');
    } catch (Exception e) {
      System.err.println("Trouble opening header file: " +e);
      System.exit(1);
    }

    try {
      // Read the header.
      // Get KSIZE, throwing away 'KSIZE=', 'NCOEFF=', and NCOEFF decimal constant.
      int type = tokenizer.nextToken(); // Skip KSIZE=
      type = tokenizer.nextToken();
      int KSIZE = (int) tokenizer.nval;
      type = tokenizer.nextToken(); // Skip NCOEFF=
      type = tokenizer.nextToken(); // and its value.

      // Set max size of coefficient array for this particular ephemeris file. 
      // If KSIZE is an odd number, then DB_size = (KSIZE / 2) + (KSIZE % 2).
      int DB_size = KSIZE / 2;

      // The next two tokens should be 'GROUP' and '1010'.
      type = tokenizer.nextToken();
      String tmpstr = tokenizer.sval;
      type = tokenizer.nextToken();
      int tempint = (int) tokenizer.nval;
      
      // BEWARE: I am using br here, not the tokenizer. The code assumes that they
      // will remain synchronized. This is title data. First some blanks.
      tmpstr = br.readLine(); tmpstr = br.readLine();
      answer.title = new String[3];
      answer.title[0] = br.readLine();
      answer.title[1] = br.readLine();
      answer.title[2] = br.readLine();

      // The next two tokens should be 'GROUP' and '1030'.
      type = tokenizer.nextToken();
      tmpstr = tokenizer.sval;
      type = tokenizer.nextToken();
      tempint = (int) tokenizer.nval;
      
      // Not storing the start and end JD of the header file.
      type = tokenizer.nextToken();
      type = tokenizer.nextToken();
      type = tokenizer.nextToken();
      answer.jdStep = tokenizer.nval;

      // Should have "GROUP" and "1040" next.
      type = tokenizer.nextToken();
      tmpstr = tokenizer.sval;
      type = tokenizer.nextToken();
      tempint = (int) tokenizer.nval;
      
      // The number of constants.
      type = tokenizer.nextToken();
      int N = (int) tokenizer.nval;

      // Read all the constant names.
      answer.constNames = new String[N];
      for (int i = 0; i < N; i++)
        {
          type = tokenizer.nextToken();
          answer.constNames[i] = tokenizer.sval;
        }
      int NCON = N;

      // Should have "GROUP" and "1041" next.
      type = tokenizer.nextToken();
      tmpstr = tokenizer.sval;
      type = tokenizer.nextToken();
      tempint = (int) tokenizer.nval;
      
      // Get number of values in next block.
      type = tokenizer.nextToken();
      N = (int) tokenizer.nval;

      answer.constValues = new double[N];
      for (int i = 0; i < N; i++)
        {
          // Read the constants. This is a hassle because the numbers appear as
          // 1.23D+02 for 123. The D confuses the tokenizer, as do the
          // pluses. Make the D's into whitespace, then trust the file to
          // alternate values and exponents.
          tokenizer.whitespaceChars('D','D');

          // Read the value.
          type = tokenizer.nextToken();
          answer.constValues[i] = tokenizer.nval;

          // Next token could be the '+' or a negative exponent.
          double exponent = 0.0;
          type = tokenizer.nextToken();
          if (type == StreamTokenizer.TT_NUMBER)
            exponent = tokenizer.nval;
          else
            {
              // Skip that one. It was '+'.
              type = tokenizer.nextToken();
              exponent = tokenizer.nval;
            }

          answer.constValues[i] *= Math.pow(10, exponent);
          
          // Keep separate copies of these constants. They're the only ones used.
          if (answer.constNames[i].equals("AU") == true)
            answer.AU = answer.constValues[i];
          if (answer.constNames[i].equals("EMRAT") == true)
            answer.EMRAT = answer.constValues[i];
          if (answer.constNames[i].equals("DENUM") == true)
            answer.NUMDE = (short) answer.constValues[i];
          if (answer.constNames[i].equals("CLIGHT") == true)
            answer.CLIGHT = answer.constValues[i];
        }
      
      // There are probably left over values. Chuck them.
      int LeftOver = N % 3;
      int NumZeros = 0;
      switch (LeftOver) {
        case 0:
          NumZeros = 0;
          break;
        case 1:
        case 2:
          NumZeros = 3 - LeftOver;
          break;
      }

      if (LeftOver > 0)
        br.readLine();

      // Should have "GROUP" and "1050" next.
      type = tokenizer.nextToken();
      tmpstr = tokenizer.sval;
      type = tokenizer.nextToken();
      tempint = (int) tokenizer.nval;
      
      // Read pointers from file 
      int[][] dataPointers = new int[3][13];
      for (int jrow = 0; jrow < 3; jrow++) 
        {
          for (int jcol = 0; jcol < 13; jcol++) 
            {
              type = tokenizer.nextToken();
              dataPointers[jrow][jcol] = (short) tokenizer.nval;
            }
        }

      // We care about the number of days for each step of the Chebyshev approximation.
      answer.bodyStep = new double[13];
      for (int i = 0; i < 13; i++)
        {
          if (dataPointers[2][i] != 0)
            answer.bodyStep[i] = answer.jdStep / (double) dataPointers[2][i];
          else
            answer.bodyStep[i] = -1;
        }

      // Only thing left is "GROUP" and "1070".
      type = tokenizer.nextToken();
      tmpstr = tokenizer.sval;
      type = tokenizer.nextToken();
      tempint = (int) tokenizer.nval;
      
      // Close up the header file and open the data file.
      br.close();
      reader.close();
      File f = new File(dataLoc[0],dataLoc[1]);
      reader = new FileReader(f);
      br = new BufferedReader(reader);
      tokenizer = new StreamTokenizer(br);

      // Read through the data file once to count up how many records there are. This is 
      // needed so that we know how large to allocate the arrays in this.data.
      int recordCount = 0;
      boolean hitEOF = false;
      while (hitEOF == false)
        {
          double[] curData = readDataBlock(br,tokenizer);
          if (curData == null)
            // Hit EOF.
            break;
          else
            ++recordCount;
        }

      // Close and reopen the data file.
      br.close();
      reader.close();
      f = new File(dataLoc[0],dataLoc[1]);
      reader = new FileReader(f);
      br = new BufferedReader(reader);
      tokenizer = new StreamTokenizer(br);

      // Allocate space to hold all the coefficients.
      answer.data = new double[13][][][];
      // Most bodies have 3 coordinates, but not nutation.
      for (int i = JPLData.Mercury; i <= JPLData.Sun; i++)
        answer.data[i] = new double[3][][];
      answer.data[JPLData.Nutations] = new double[2][][];
      answer.data[JPLData.Librations] = new double[3][][];

      // Some ephemera lack nutation and/or libration data.
      if (dataPointers[1][JPLData.Nutations] == 0)
        answer.data[JPLData.Nutations] = null;
      if (dataPointers[1][JPLData.Librations] == 0)
        answer.data[JPLData.Librations] = null;

      // Allocating space to hold coefficient indices.
      for (int i = JPLData.Mercury; i <= JPLData.Librations; i++)
        {
          if (answer.data[i] != null)
            {
              for (int j = 0; j < answer.data[i].length; j++)
                answer.data[i][j] = new double[dataPointers[1][i]][];
            }
        }

      // Allocate space to hold the time step indices.
      for (int i = JPLData.Mercury; i <= JPLData.Librations; i++)
        {
          if (answer.data[i] != null)
            {
              for (int j = 0; j < answer.data[i].length; j++)
                {
                  for (int k = 0; k < answer.data[i][j].length; k++)
                    answer.data[i][j][k] = new double[recordCount * dataPointers[2][i]];
                }
            }
        }

      // Have all the space needed to hold the data. Read it in.
      recordCount = 0;
      hitEOF = false;
      double finalJD = 0;

      // Each body steps through the time index at a different rate.
      int[] bodyTimeCount = new int[13];
      for (int i = 0; i < 13; i++)
        bodyTimeCount[i] = 0;

      while (hitEOF == false)
        {
          // Read in one entire record for a 32 or 64 day block.
          double[] curData = readDataBlock(br,tokenizer);
          if (curData == null)
            // Hit EOF.
            break;

          // If this was the first record, note the start time.
          if (recordCount == 0)
            answer.startJD = curData[0];

          // Note also the last valid JD read.
          finalJD = curData[1];

          // Parse out the data and put it into this.data.
          int dataIndex = 2;
          for (int i = JPLData.Mercury; i <= JPLData.Librations; i++) {
            // Make sure not trying to store nonexistent nutations or librations.
            if (answer.data[i] != null) {
              // Look over how many time blocks appear for each body in each record.
              for (int u = 0; u < dataPointers[2][i]; u++) {
                // Looping over number of axes.
                for (int j = 0; j < answer.data[i].length; j++) {
                  for (int k = 0; k < dataPointers[1][i]; k++) {
                    answer.data[i][j][k][bodyTimeCount[i]] = curData[dataIndex];
                    ++dataIndex;
                  }
                }
                ++bodyTimeCount[i];
              }
            }
          }

          ++recordCount;
        }

      // Note the last time read.
      answer.endJD = finalJD;

      // Done reading.
      br.close();
      reader.close();

    } catch (Exception e) {
      System.err.println("Trouble with header or data file: " +e);
      System.exit(1);
    }
    
    return answer;
  }
  
  private static JPLData loadFromUnixBinFile(String dir,String dname) {

    // Similar to loadFromAsciiFile(), but the source file in in binary format. 
    // This is harder to understand since you can't easily read the file directly and 
    // compare with what you're doing. On the other hand, the data doesn't have to be
    // parsed. Unlike loadFromAsciiFile(), there is no argument for the header file. 
    // The header is included in the binary data.
    JPLData answer = new JPLData();
    
    try {
      // Open the file
      File f = new File(dir,dname);
      FileInputStream in = new FileInputStream(f);
      DataInputStream fin = new DataInputStream(in);
      
      // First is the three lines of title text. Each line is 84 characters
      // long, the end of which is just white space.
      answer.title = new String[3];
      answer.title[0] = "";
      for (int i = 0; i < 84; i++)
        {
          int c = fin.readUnsignedByte();
          answer.title[0] = answer.title[0] + String.valueOf((char) c);
        }
      answer.title[0] = answer.title[0].trim();

      answer.title[1] = "";
      for (int i = 0; i < 84; i++)
        {
          int c = fin.readUnsignedByte();
          answer.title[1] = answer.title[1] + String.valueOf((char) c);
        }
      answer.title[1] = answer.title[1].trim();

      answer.title[2] = "";
      for (int i = 0; i < 84; i++)
        {
          int c = fin.readUnsignedByte();
          answer.title[2] = answer.title[2] + String.valueOf((char) c);
        }
      answer.title[2] = answer.title[2].trim();

      // Now comes the names of the different constants. Each of these takes six
      // characters, and may have trailing whitespace. There is space for 400 of these 
      // 6 character names. Read them all and check if they are just spaces before 
      // keeping them.
      String[] tempNames = new String[400];
      int nameCount = 0;
      for (int i = 0; i < 400; i++)
        {
          tempNames[nameCount] = "";
          for (int j = 0; j < 6; j++)
            {
              int c = fin.readUnsignedByte();
              tempNames[nameCount] = tempNames[nameCount] + String.valueOf((char) c);
            }
          tempNames[nameCount] = tempNames[nameCount].trim();
          if (tempNames[nameCount].length() != 0)
            ++nameCount;
        }
      
      // Copy the non-empty names out.
      answer.constNames = new String[nameCount];
      for (int i = 0; i < nameCount; i++)
        answer.constNames[i] = tempNames[i];
      tempNames = null;

      // Read the start and end JD. These are discarded since they are worked
      // out later based on the JDs appearing with each coefficient record.
      double tempDouble = fin.readDouble();
      tempDouble = fin.readDouble();

      // Number of days per block and number of constants (already known).
      answer.jdStep = fin.readDouble();
      int tempInt = fin.readInt();

      // Read KM/AU, E/M mass ratio.
      answer.AU = fin.readDouble();
      answer.EMRAT = fin.readDouble();
      
      // Now the pointer block that includes the DE number.
      int[][] dataPtr = new int[3][13];
      for (int j = 0; j < 12; j++)
        {
          for (int i = 0; i < 3; i++)
            dataPtr[i][j] = fin.readInt();
        }
      answer.NUMDE = fin.readInt();
      for (int i = 0; i < 3; i++)
        dataPtr[i][12] = fin.readInt();

      // Figure out the number of doubles in a coefficient block.
      int blockValues = 0;
      if (dataPtr[0][12] != 0)
        // There is libration data. It has 3 coordinates.
        blockValues = dataPtr[0][12] + dataPtr[1][12] * dataPtr[2][12] * 3;
      else if (dataPtr[0][11] != 0)
        // No libration data, but there is nutation data. It has 2 coordinates.
        blockValues = dataPtr[0][11] + dataPtr[1][11] * dataPtr[2][11] * 2;
      else
        // No libration or nutation. Use the sun, which has 3 coordinates.
        blockValues = dataPtr[0][10] + dataPtr[1][10] * dataPtr[2][10] * 3;
      blockValues -= 1;
      
      int bytesPerBlock = blockValues * 8;
      
      // Read to a point where we've read in a total of bytesPerBlock
      // bytes. 2856 bytes have already been read.
      int skip = bytesPerBlock - 2856;
      tempInt = fin.skipBytes(skip);
      if (tempInt != skip)
        {
          System.err.println("Problem skipping bytes in the file!");
          System.exit(1);
        }

      // Read the constant values.
      answer.constValues = new double[answer.constNames.length];
      for (int i = 0; i < answer.constValues.length; i++)
        answer.constValues[i] = fin.readDouble();
      
      // The only one of these constants thats needed is CLIGHT,
      // the speed of light. Find it and save it separately.
      for (int i = 0; i < answer.constNames.length; i++)
        {
          if (answer.constNames[i].equals("CLIGHT"))
            answer.CLIGHT = answer.constValues[i];
        }

      // Read enough data to make a total of two blocks of bytesPerBlock bytes.
      skip = bytesPerBlock - 8 * answer.constValues.length;
      tempInt = fin.skipBytes(skip);
      if (tempInt != skip)
        {
          System.err.println("Problem skipping second set of bytes in the file!");
          System.exit(1);
        }
      
      // Now ready to read the coefficient data (the ephemeris records). Very 
      // similar to the ASCII case. Allocate space to hold all the coefficients.
      // To do this, we need to know how many coefficient blocks there are. This 
      // can be worked out from the file size.
      long fsize = f.length();
      int recordCount = (int)(fsize / bytesPerBlock - 2);

      // We care about the number of days for each step of the Chebyshev approximation.
      answer.bodyStep = new double[13];
      for (int i = 0; i < 13; i++)
        {
          if (dataPtr[2][i] != 0)
            answer.bodyStep[i] = answer.jdStep / (double) dataPtr[2][i];
          else
            answer.bodyStep[i] = -1;
        }

      // Allocate memory.
      answer.data = new double[13][][][];
      // Most bodies have 3 coordinates, but not nutation.
      for (int i = JPLData.Mercury; i <= JPLData.Sun; i++)
        answer.data[i] = new double[3][][];
      answer.data[JPLData.Nutations] = new double[2][][];
      answer.data[JPLData.Librations] = new double[3][][];

      // Some ephemera lack nutation and/or libration data.
      if (dataPtr[1][JPLData.Nutations] == 0)
        answer.data[JPLData.Nutations] = null;
      if (dataPtr[1][JPLData.Librations] == 0)
        answer.data[JPLData.Librations] = null;

      // Allocate space to hold coefficient indices.
      for (int i = JPLData.Mercury; i <= JPLData.Librations; i++)
        {
          if (answer.data[i] != null)
            {
              for (int j = 0; j < answer.data[i].length; j++)
                answer.data[i][j] = new double[dataPtr[1][i]][];
            }
        }

      // Allocate space to hold the time step indices.
      for (int i = JPLData.Mercury; i <= JPLData.Librations; i++)
        {
          if (answer.data[i] != null)
            {
              for (int j = 0; j < answer.data[i].length; j++)
                {
                  for (int k = 0; k < answer.data[i][j].length; k++)
                    answer.data[i][j][k] = new double[recordCount * dataPtr[2][i]];
                }
            }
        }

      // Each body steps through the time index at a different rate.
      int[] bodyTimeCount = new int[13];
      for (int i = 0; i < 13; i++)
        bodyTimeCount[i] = 0;

      // Have all the space needed to hold the data. Read it in.
      double finalJD = 0;

      for (int r = 0; r < recordCount; r++)
        {
          // Inform the user of our progress on stdout.
          if ((r % 100) == 0)
            System.out.println("Reading record " +r+ " of " +recordCount);

          // Read in one entire record for a 32 or 64 day block.
          double[] curData = readDataBlock(fin,blockValues);

          // If this was the first record, note the start time.
          if (r == 0)
            answer.startJD = curData[0];

          // Note also the last valid JD read.
          finalJD = curData[1];

          // Parse out the data and put it into this.data.
          int dataIndex = 2;
          for (int i = JPLData.Mercury; i <= JPLData.Librations; i++) {
            // Make sure not trying to store nonexistent nutations or librations.
            if (answer.data[i] != null) {
              // Look over how many time blocks appear for each body in
              // each record.
              for (int u = 0; u < dataPtr[2][i]; u++) {
                // Looping over number of axes.
                for (int j = 0; j < answer.data[i].length; j++) {
                  for (int k = 0; k < dataPtr[1][i]; k++) {

                    answer.data[i][j][k][bodyTimeCount[i]] = curData[dataIndex];
                    ++dataIndex;
                  }
                }
                ++bodyTimeCount[i];
              }
            }
          }
        }

      // Note the last time read.
      answer.endJD = finalJD;
    } catch (Exception e) {
      System.err.println("Trouble reading binary data file: " +e);
      System.exit(1);
    }
    
    return answer;
  }

  private static double[] readDataBlock(DataInputStream in,int size) throws Exception {
  
    // Reads an entire Ephemeris Record. Similar to readDataBlock()
    // below, but this is for binary data and is much simpler.
    double[] answer = new double[size];
    for (int i = 0; i < size; i++)
      answer[i] = in.readDouble();
    return answer;
  }
  
  private static double[] readDataBlock(BufferedReader br, StreamTokenizer tokenizer) 
                                                                    throws Exception {
  
    // Read a whole Ephemeris Record. Return null if at the EOF. This is for 
    // reading ASCII data.
  
    int type = tokenizer.nextToken();
    if (type == tokenizer.TT_EOF)
      return null;
    int NRW = (int) tokenizer.nval;
    type = tokenizer.nextToken();
    if (type == tokenizer.TT_EOF)
      return null;
    int NCOEFF = (int) tokenizer.nval;
    
    // Read and return an entire Ephemeris Record.
    double[] answer = new double[NCOEFF];
    for (int i = 0; i < NCOEFF; i++)
      {
        // This is a hassle because the numbers appear as (e.g.) 1.23D+02 for 
        // 123. The D confuses the tokenizer, as do the pluses. Make the D's 
        // into whitespace, then trust the file to alternate values and exponents.
        tokenizer.whitespaceChars('D','D');
        
        // Read the value.
        type = tokenizer.nextToken();
        answer[i] = tokenizer.nval;
        
        // Next token could be the '+' or a negative exponent.
        double exponent = 0.0;
        type = tokenizer.nextToken();
        if (type == StreamTokenizer.TT_NUMBER)
          exponent = tokenizer.nval;
        else
          {
            // Skip that one. It was '+'.
            type = tokenizer.nextToken();
            exponent = tokenizer.nval;
          }
  
        answer[i] *= Math.pow(10, exponent);
      }
  
    // There may be left-over values. Chuck them.
    int LeftOver = NCOEFF % 3;
    int NumZeros = 0;
    switch (LeftOver) {
      case 0:
        NumZeros = 0;
        break;
      case 1:
      case 2:
        NumZeros = 3 - LeftOver;
        break;
    }
  
    if (LeftOver > 0)
      br.readLine();
  
    return answer;
  }
}