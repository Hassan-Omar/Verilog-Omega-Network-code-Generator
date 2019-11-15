package client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OmegaNetworkConnector {
  
  // this method will write the script
  public void scriptWriter(int n)
  {   // I will divid the Script 3 parts
      String header="module OmegaNetwork(input ["+(n-1)+":0] InDat, input CLK, output ["+(n-1)+":0]  outData);\r\n";
      String wires="";
      String instancBlocks="";
      String continusAssign="";
      // stores number of satages and number of switches per stage
      int switshesPerStage = n/2;
      int stagesNumber = log2(n);
      // 
      int counter=0; 
      int downCounter=n-1;
      
      for(int k=0; k<stagesNumber; k++)
      {// outer loop for stages
          for(int i=0; i<switshesPerStage; i++)
          {
              // for each Banyan I need 4 wires X1,X2 -> INPUT , Y1,Y2 -> OUTPUT
              //wires+="    "+"wire W"+i+"S"+k+"X1;"+"\r\n";
              //wires+="    "+"wire W"+i+"S"+k+"X2;"+"\r\n";
              wires+="    "+"wire W"+"S"+k+"_"+counter+";"+"\r\n";
              wires+="    "+"wire W"+"S"+k+"_"+(counter+1)+";"+"\r\n";
              //______________________
              String temp = mapPorts(n,k,counter,downCounter);
              counter++;downCounter--;
              temp+= mapPorts(n,k,counter,downCounter);
              counter++;downCounter--;
              if(k<stagesNumber-1)
                 instancBlocks+="    "+"Banyan2_2 ins_Banyn"+i+"_"+k+"("+temp+"W"+"S"+k+"_"+(counter-2)+", "+"W"+"S"+k+"_"+(counter-1)+", CLK);"+"\r\n";
              else
                 instancBlocks+="    "+"Banyan2_2 ins_Banyn"+i+"_"+k+"("+temp+"outData["+(downCounter+2)+"], outData["+(downCounter+1)+"], CLK);"+"\r\n";
           } // end of inner loop
          counter=0;
          downCounter=n-1;
      } // end of outer loop
      
      scriptWriterTofile(header+wires+instancBlocks+"endmodule");
  }
//+++++++++++++++++++++++++++++++++++++++
  private String mapPorts(int n,int k,int counter, int downCounter)
  { 
      int mapNum;
      // check if n is even or odd
      String result="";
      if(counter%2==0)
      {
          mapNum = counter/2; 
      }else
      {
          mapNum = (((n-1)-counter)/2)+counter;

      }
      
      if(k==0)
      { 
          result+="InDat["+downCounter+"], ";

      }
      else
      {
          result+="W"+"S"+(k-1)+"_"+mapNum+", ";
      }
      return result;
  }
  
//+++++++++++++++++++++++++++++++++++++++
    // this method to calculate log base 2
    private int log2(int x)
    {
        return (int) (Math.log(x) / Math.log(2));
    }
//+++++++++++++++++++++++++++++++++++++++++   
    // thus function to write The script to current local directory
    private void scriptWriterTofile (String data)
    {
        File file = new File(System.getProperty("user.dir")+"/omega.v");
        FileWriter fr= null;
        try{
            fr = new FileWriter(file);
            fr.write(data);
        }catch(IOException e)
        {e.printStackTrace();}
        finally{
            try{
                fr.close();
            } catch
            (IOException e){
                e.printStackTrace();
                       }
            }
    }
}
