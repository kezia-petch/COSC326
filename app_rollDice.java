package rollin;

import java.util.*;

public class app_rollDice{

    /** Main method that calls the random method used to complete the rollin task
     **/
    public static void main(final String[] args) {

        int num_of_roll = 0;
        //Dice dice = new Dice(new int[]{3,5,5,5,4,6});
        Dice dice = new Dice();
        Random rand = new Random();
        //dice.printTable();
        while(!dice.isComplete()){
            int roll = rand.nextInt(6);
            int d[] = dice.getDice();
            System.out.println("\n===============================\n" +Arrays.toString(dice.getDice()) + " + new roll: " + (roll+1));
            
            int diceToReplace = dice.handleRoll(roll);
            
            if(diceToReplace != -1){ //if handle roll give us an index to replace
                System.out.println("REPLACE " + d[diceToReplace] + " WITH "+ (roll+1));               
                dice.replaceDice(roll,diceToReplace);
                System.out.println(Arrays.toString(dice.getDice()));
                
            }else{
                System.out.println("reroll");
            }
            
            num_of_roll++;
        }
        //dice.printTable();
        
        System.out.println("\n****************************\n!! TWO SET COMPLETED !!\n Took "+ num_of_roll + " roll");
        System.out.println("final set of dice :"+Arrays.toString(dice.getDice()));
        
    }


}
