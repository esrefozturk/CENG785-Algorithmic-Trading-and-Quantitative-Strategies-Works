/***
 * The Example is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Example is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neuroph.contrib.samples.stockmarket;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.LMS;

/**
 *
 * @author Dr.V.Steinhauer
 */
public class Main {
    
    public  static double calc(String filename,int maxIterations ) throws FileNotFoundException, IOException{
                ArrayList<Double> ar = new ArrayList<Double>();
        FileInputStream fis = new FileInputStream(filename);
        BufferedReader dis = new BufferedReader(new InputStreamReader(fis));
        String s;
        while ((s = dis.readLine()) != null) {
            ar.add(Double.parseDouble(s));
        }
        NeuralNetwork neuralNet = new MultiLayerPerceptron(5, 9, 1);
        ((LMS) neuralNet.getLearningRule()).setMaxError(0.001);//0-1
        ((LMS) neuralNet.getLearningRule()).setLearningRate(0.7);//0-1
        ((LMS) neuralNet.getLearningRule()).setMaxIterations(maxIterations);//0-1
        TrainingSet trainingSet = new TrainingSet();
        for(int i=0;i<ar.size()-5;i++){
          
            trainingSet.addElement(new SupervisedTrainingElement(new double[]{ar.get(i)/1000,ar.get(i+1)/1000,ar.get(i+2)/1000,ar.get(i+3)/1000,ar.get(i+4)/1000 },
                    new double[]{ar.get(i+5)/1000}));
            
        
        }
        
        
        neuralNet.learnInSameThread(trainingSet);
        
        TrainingSet testSet = new TrainingSet();
        for(int i=ar.size()-5;i<ar.size()-5+1;i++){
            testSet.addElement(new TrainingElement(new double[]{ar.get(i)/1000,ar.get(i+1)/1000,ar.get(i+2)/1000,ar.get(i+3)/1000,ar.get(i+4)/1000 }));
        }
        int i=ar.size()-20;
        double result=0;
        for (TrainingElement testElement : testSet.trainingElements()) { // loops only once
            neuralNet.setInput(testElement.getInput());
            neuralNet.calculate();
            Vector<Double> networkOutput = neuralNet.getOutput();
            //System.out.print("Input: " + testElement.getInput());
            //System.out.println(" Output: " + networkOutput);
            //System.out.println(networkOutput.get(0)-ar.get(i+4)/1000  );
            i++;
            result = networkOutput.get(0)*1000;
            
        }
        return result;
    }
    

    public static void main(String[] args)  {
        
        try {
            System.out.println("Stock,Predicted");
            System.out.println("BBRY," + calc("../averages/BBRY.csv",1000));
            System.out.println("BHP," + calc("../averages/BHP.csv",1000));
            System.out.println("DE," + calc("../averages/DE.csv",1000));
            System.out.println("FE," + calc("../averages/FE.csv",1000));
            System.out.println("GOOG," + calc("../averages/GOOG.csv",1000));
            System.out.println("GS," + calc("../averages/GS.csv",1000));
            System.out.println("JNJ," + calc("../averages/JNJ.csv",1000));
            System.out.println("KO," + calc("../averages/KO.csv",1000));
            System.out.println("WMT," + calc("../averages/WMT.csv",1000));
            System.out.println("XOM," + calc("../averages/XOM.csv",1000));
            
     
            
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        System.exit(0);
    }
}
