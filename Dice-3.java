package rollin;

import java.util.*;

public class Dice extends Rollin{
    
    int[][] d;
    int num_of_complete_set = 0;
  
    /** Constructor which sets dice and assigns the dice array with the updated sequence values
        @param dice array containing the random values of the six rolled dice
    **/
    public Dice(int[] dice){
        super();
        setDice(dice);
        d = new int[2][6];
        for(int i=0; i<6;i++){
            d[0][dice[i]-1] += 1;
        }        
        updateSequence();
    }
  
    /** Constructor method that take no inputs and initializes array of dice with random numbers
        Dice is also set with the updated sequence within this method.**/
    public Dice(){      
        super();
        int[] dice = new int[6];
        Random rand = new Random();
        for(int i=0;i<dice.length;i++){
            dice[i] = rand.nextInt(6)+1;
        }
        d = new int[2][6];
        for(int i=0; i<6;i++){
            d[0][dice[i]-1] += 1;
        }
        setDice(dice);
        updateSequence();
    }
  
    /** Take the random new roll as input, decide whether to replace one of
        the current set of dice with the new roll.

        @param roll the random roll value of the seventh dice used.
        @return The index of the dice to be replaces by the new dice, if replacing is chosen.l**/
    public int handleRoll(int roll){
        updateTable();
        roll -= 1;
        int diceToReplace = -1;
        
        for(int i=0;i <6;i++){ //check if adding new row results in two winning set
            int temp = dice[i];
            dice[i] = roll+1;
            if(isComplete()){ //if yes, replace one of the dice with new roll
                addDice(roll);
                dice[i] = temp;
                return i;
            }else{
                dice[i] = temp;
            }
        }    

        /*if new roll does not result in two complete set, work out if it's worth replacing new roll
          by checking number of possible winning set before and after adding new roll*/
        
        //find how many possible winning set we have (before adding new roll)
        int num_possible_set = possibleWinningSet();
    
        //find how many possible winning set we have after adding new roll
        addDice(roll);    
        int new_possible_set = possibleWinningSet();
    
        //if adding new roll increase number of possible winning set
        if(new_possible_set>num_possible_set){  //its a good idea to keep this dice, replace one of the current dice with new roll      
           
            diceToReplace = pickDiceToRemove();
            return diceToReplace;
      
        }else{ //it does increase number of winning set, do not add this roll
            removeDice(roll);
            return diceToReplace;
        }        
    }

    /** Method that calculates the dice index that is going to be replaced by the random dice rolled.
        A replace can also not occur if decided.
        @param roll the randomly chosen seventh dice value
        @return index of dice that are going to get removed **/
    public int pickDiceToRemove(){
        // array represents number of possible winning set if item at specified index is removed
        int[] ar = new int[6];
        Arrays.fill(ar,-1);        
    
        for(int i =0; i<6;i++){ //for each item in array, find out how many winning set we get after removing this item
            if(d[0][i] >0){
                removeDice(i);
                ar[i] = possibleWinningSet();                
                addDice(i); 
            }
        }

        // find dice that give highest chance of winning
        int highest = 0;
        for(int i=0; i< 6;i++){
            if(ar[i]> ar[highest]){ //removing this dice give highest chance of winning
                highest = i;
            }
        }        
            
        // find index of taht dice in dice array
        int index=-1;
        for(int i= 0;i<6;i++){
            if(dice[i] == highest+1){
                index = i;
                break;
            }
        }

        return index;
    }
    
  
    /** Method that replacea the dice at specified index by new given roll
        @param roll the randomly chosen seventh dice value
        @param diceToRemove index of dice to be removed
    **/
    public void replaceDice(int roll,int diceToRemove){
        roll-=1;
        /* remove dice */
        int del = diceToRemove;
        int index = diceToRemove;    
        
        removeDice(dice[del]-1); //remove dice
        dice[index] = roll+1;  //replace it with new roll
    }
    

    /** Method that counts the future possible winning sets if a that
        specific dice value was going to be removed from the dice array.

        @return the future number of possible sets if that dice was to be removed.
    **/
    public int  possibleWinningSet(){
        List<int[]> possible_set = new ArrayList<int[]>();
        int num_possible_set =0;
        for(int r=0;r<2;r++){
            for(int c=0;c<6;c++){
                if(d[r][c]==3){
                    num_possible_set++;
                    if(r==0){ // three similar dice
                        possible_set.add(new int[]{c,c,c});
                    }else{ // sequence of three dice
                        possible_set.add(new int[]{c-1,c,c+1});
                    }
                }                
            }
        }
        
        return num_possible_set;
    }


    /**Adds dice to the 2D array being used.
       @param index the index where the specific value is added to**/
    public void addDice(int index){
        d[0][index] += 1;
        updateSequence();
    }
    
    /** Removes dice from the 2D array being used.
        @param index the index where the specific value is being removed from**/
    public void removeDice(int index){       
        d[0][index] -= 1;
        updateSequence();
    }


    /** Method that records the sequence runs occuring in
        the dice array in the 2D row of our own assigned array.
    **/
    public void updateSequence(){
    
        for(int i=0; i<d[1].length;i++){ //reset everything to 0
            d[1][i]=0;
        }
        
        for(int i=0; i<6;i++){      
            if(i-1 >=0){ //left item exist, is part of sequence
                if(d[0][i-1] >0){
                    d[1][i] += 1;
                }
            }
            if(i+1 <6){
                if(d[0][i+1] >0){ //right item exist, is part of sequence
                    d[1][i] += 1;
                }
            }
            if(d[0][i] >0){ 
                d[1][i] += 1;
            }
            if(d[0][i] ==0){
                d[1][i] = 0;
            }    
        }    
    }

    /** Method that records frequency of dice and  sequence runs occuring in
        the dice array in the 2D row of our own assigned array.
    **/
    public void updateTable(){
        for(int i=0; i<d[1].length;i++){ //reset everything to 0
            d[0][i]=0;
            d[1][i]=0;
        }

        for(int i =0;i<6;i++){ // UPDATE FREQUENCY OF DICE
            d[0][dice[i]-1] += 1;
        }
        
        
        for(int i=0; i<6;i++){      
            if(i-1 >=0){ //left item exist, is part of sequence
                if(d[0][i-1] >0){
                    d[1][i] += 1;
                }
            }
            if(i+1 <6){
                if(d[0][i+1] >0){ //right item exist, is part of sequence
                    d[1][i] += 1;
                }
            }
            if(d[0][i] >0){ 
                d[1][i] += 1;
            }
            if(d[0][i] ==0){
                d[1][i] = 0;
            }    
        }
    }
  
    /** Prints a visual representation of the 2D array used to determine the
        replacing and general information of the dice array.
    **/
    public void printTable(){
        System.out.println("DICE : " + Arrays.toString(dice));
    
        for(int i=0;i<6;i++){
            System.out.print((i+1) + " | ");   
        }
        System.out.println("\n------------------------");
    
        for(int r=0;r<d.length;r++){
            for(int c=0; c<d[r].length;c++){
                System.out.print(d[r][c] + " | ");                
            }
            if(r ==0){
                System.out.print("   <- Frequency");
            }
            if(r ==1){
                System.out.print("   <- Longest sequence");
            }
            System.out.println();
        }
    }
  
  
  
}
