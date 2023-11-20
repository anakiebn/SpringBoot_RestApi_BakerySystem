package com.anakie.restApiBakery;

public class Test {

    public static void main(String[] args) {

        int counter=1;
        int rowCounter=1;
        for(int i=0;i<25;i++){
            for(int j=0;j<79;j++){
                if((i+1)%10==0 && j==0){
                    System.out.print(rowCounter++);
                    continue;
                }

                
                if((j+1)%10==0){
                    System.out.print(counter++);
                }
                else{
                    System.out.print("=");
                }
            }
            counter=1;
            System.out.println();
      }



    }
}
