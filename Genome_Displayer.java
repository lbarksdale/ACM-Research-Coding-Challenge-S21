/*
Created by Levi Barksdale on 1/21/2021 for ACM Research application.

This program is designed to take a .gb file (genbank file, containing information about an individual's
genome, along with the genome itself) and output a circular genome map as a PNG file. This is accomplished
with the help of the Java library CGView (Circular Genome Viewer). Information about CGView can be found
at https://paulstothard.github.io/cgview/index.html. Citation:
Stothard P, Wishart DS (2005) Circular genome visualization and exploration using CGView. Bioinformatics 21:537-539.

The scanning process will actually ignore a significant portion of the information in the .gb file. Relevant
information, such as the organism name and various labels, will be taken and formatted properly. Once relevant
information is found, it will be used to create a new CGView object, complete with labels and a title. Finally,
this CGView object will be used to generate the PNG file.
 */


import ca.ualberta.stothard.cgview.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Genome_Displayer implements CgviewConstants
{
    public static void main(String[] args)
    {
        Scanner getFileName = new Scanner(System.in);
        Scanner inputFile = null;

        //Asks the user for the filename and attempts to open the file.
        //If it cannot open the file, asks the user for another filename.
        do {
            System.out.print("Please enter the file name: ");
            String filename = getFileName.nextLine();


            try
            {
                inputFile = new Scanner(new File(filename));
            } catch (FileNotFoundException e)
            {
                System.out.println("Bad file name entered.");
            }
        }while(inputFile == null);

        String firstLine = inputFile.nextLine().substring(6).trim();
        firstLine = firstLine.substring(firstLine.indexOf(" ")).trim();
        firstLine = firstLine.substring(0, firstLine.indexOf(" "));

        Cgview circleGenome = new Cgview(Integer.parseInt(firstLine));

        //some optional settings. Much of this was copied from example files found at
        //https://paulstothard.github.io/cgview/api_overview.html
        //Specifically from the CgviewTest0 example file
        circleGenome.setWidth(600);
        circleGenome.setHeight(600);
        circleGenome.setBackboneRadius(160.0f);
        circleGenome.setTitle(getName(inputFile));      //Displays the name of the organism in the middle
        circleGenome.setLabelPlacementQuality(10);
        circleGenome.setShowWarning(true);
        circleGenome.setLabelLineLength(8.0d);
        circleGenome.setLabelLineThickness(0.5f);

        //From here, all we need is the labels for the diagram. As such, we only look for where the labels are declared.
        while(inputFile.hasNext())
        {
            String curLine = inputFile.nextLine();

            //Identifies places in the .gb file where a label is specified
            if(curLine.contains("     gene            "))
            {

                //This chunk gets the label text and location from the .gb file
                //First, we get the start and end index for the label, so we know where to put it
                curLine = (curLine.trim()).substring(5).trim();
                int startIndex;
                int endIndex;

                //The gene range might be given as a complement. We check for that and adjust here.
                if(curLine.contains("complement"))
                {
                    startIndex = Integer.parseInt(curLine.substring(11, curLine.indexOf(".")));
                    endIndex = Integer.parseInt(curLine.substring(curLine.lastIndexOf(".")+1, curLine.length()-2));
                }
                else
                {
                    startIndex = Integer.parseInt(curLine.substring(0, curLine.indexOf(".")));
                    endIndex = Integer.parseInt(curLine.substring(curLine.lastIndexOf(".") + 1));
                }

                //The next line down is the text to show on the label. We go to that line now.
                curLine = inputFile.nextLine().trim();
                //We need to parse the line a bit to get just the label text. We do that here.
                String labelText = curLine.substring(curLine.indexOf("\"")+1, curLine.length() - 1);

                //Now we add the new label into the list of all the features to show on the diagram.
                FeatureSlot labels = new FeatureSlot(circleGenome, DIRECT_STRAND);

                Feature currentGene = new Feature(labels, labelText);
                //I just set a random color for each label. That seemed to be the easiest
                //way to differentiate between labels.
                currentGene.setColor(getRandomColor());
                FeatureRange currentGeneRange = new FeatureRange(currentGene, startIndex, endIndex);
                currentGeneRange.setDecoration(CgviewConstants.DECORATION_STANDARD);

            }
        }

        //This section uses the CGView library to actually create the PNG image.
        //If for some reason this fails, an error message is printed.
        try{
            CgviewIO.writeToPNGFile(circleGenome, "Circular_Genome_Picture.png");
        } catch (IOException e)
        {
            System.out.println("Error creating file.");
        }
    }


    //This method parses through the .gb file to find the organism's name
    public static String getName(Scanner input)
    {
        String curLine = "";
        //Searches through input file to find line where organism name is. It's always near the top,
        //after the length of the genome.
        while(!curLine.contains("SOURCE"))
        {
            curLine = input.nextLine();
        }
        //Next, we trim the line containing the organism name until we have just the organism name.
        curLine = input.nextLine();
        curLine = curLine.trim();
        curLine = curLine.substring(curLine.indexOf(' ') + 1);  //At this point, curLine should contain the name
                                                                //of the organism
        //Returns the name of the organism
        return curLine;
    }

    //This method generates a random color for the label. It's the easiest way I could think of
    //to differentiate between labels.
    public static Color getRandomColor()
    {
        int num1 = (int)(Math.random()*256);
        int num2 = (int)(Math.random()*256);
        int num3 = (int)(Math.random()*256);
        return new Color(num1, num2, num3);

    }

}
