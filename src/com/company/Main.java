package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        int nbSupprime = 0;
        boolean quit = false;
        ArrayList<ArrayList<String>> AIMoves = new ArrayList<ArrayList<String>>();
        SetupDefaultAIMoves(AIMoves);

        do{
            int[] toDeleteA = new int[2];
            ArrayList<ArrayList<Integer>> toDelete = new ArrayList<ArrayList<Integer>>();

            System.out.println("Bienvenue au hexapawn!");
            System.out.println("Itération d'intélligence : " + nbSupprime);

            System.out.println("1. Commencer");
            System.out.println("2. Quitter");
            Scanner scanner = new Scanner(System.in);
            int choix = scanner.nextInt();
            switch(choix){
                case 1 :
                    toDeleteA = Game(AIMoves);
                    break;
                case 2 :
                    System.exit(0);
                    break;
            }

            if(toDeleteA[0] != 69){
                nbSupprime++;
                ArrayList<String> temp = new ArrayList<String>();
                temp = AIMoves.get(toDeleteA[0] - 1);
                temp.remove(toDeleteA[1]);
                AIMoves.set(toDeleteA[0] - 1, temp);
                System.out.println("deleted " + toDeleteA[1] + " at position " + (toDeleteA[0] - 1));
            }

        }while(!quit);
    }
    private static int[] Game(ArrayList<ArrayList<String>> AIMoves) throws InterruptedException{
        ArrayList<ArrayList<Integer>> toDelete = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> temp = new ArrayList<Integer>();
        ArrayList<Integer> temp2 = new ArrayList<Integer>();
        toDelete.add(new ArrayList<Integer>());
        toDelete.add(new ArrayList<Integer>());

        boolean gameDone = false;
        //0 - Nothing
        //1 - Friendly Pawn
        //2 - Ennemy Pawn
        boolean isValid;
        boolean textValid;
        int[][] board = new int[3][3];
        Setup(board);
        Afficher(board);
        Scanner scanner = new Scanner(System.in);
        while(!gameDone){
            isValid = true;
            textValid = true;
            String jeu = scanner.nextLine();
            if(jeu.equals("")){
                Clear(50);
                System.out.println("Abandon du jeu en cours...");
                TimeUnit.SECONDS.sleep(1);
                Clear(50);


                gameDone = true;
                return new int[]{69, 69};
            }
            else{

                textValid = TestText(jeu);

                int posO1 = 0;
                int posO2 = 0;
                int posD1 = 0;
                int posD2 = 0;



                if(textValid){
                    posO1 = jeu.charAt(1) - '1';
                    posO2 = jeu.charAt(0) - '1';
                    posD1 = jeu.charAt(3) - '1';
                    posD2 = jeu.charAt(2) - '1';
                    isValid = !testInvalide(board, posO1, posO2, posD1, posD2);
                }


                if(isValid && textValid){
                    MovePiece(board, posO1, posO2, posD1, posD2);
                    Afficher(board);
                    TimeUnit.SECONDS.sleep(1);
                    int positionActuelle = FindPosition(board);

                    if(positionActuelle != 69){
                        temp.add(positionActuelle % 24);

                        temp2.add(MovePieceComp(positionActuelle, board, AIMoves));
                        Afficher(board);

                        if(TestIfPCWon(board)){
                            TimeUnit.SECONDS.sleep(1);
                            Clear(50);
                            System.out.println("Vous avez perdu... Dommage..");
                            TimeUnit.SECONDS.sleep(1);
                            Clear(50);
                            return new int[]{69, 69};
                        }

                    }else{
                        if(TestIfWon(board)){
                            TimeUnit.SECONDS.sleep(1);
                            Clear(50);
                            System.out.println("Bravo! vous avez gagné!");
                            System.out.println("L'ordinateur apprend...");
                            TimeUnit.SECONDS.sleep(1);
                            Clear(50);
                        }
                        else{
                            TimeUnit.SECONDS.sleep(1);
                            Clear(50);
                            System.out.println("Vous avez perdu... Dommage..");
                            ArrayList<Integer> ttemp1 = new ArrayList<Integer>();
                            ArrayList<Integer> ttemp2 = new ArrayList<Integer>();
                            ttemp1 = toDelete.get(0);
                            ttemp2 = toDelete.get(1);

                            for(int i = 0; i < toDelete.get(0).size(); i++){
                                ttemp1.set(i, 69);
                                ttemp2.set(i, 69);
                            }
                            toDelete.set(0, ttemp1);
                            toDelete.set(1, ttemp2);
                            Clear(50);
                            return new int[]{69, 69};

                        }

                        gameDone = true;
                    }
                    toDelete.set(0, temp);
                    toDelete.set(1, temp2);
                }
                else{
                    Afficher(board);
                    System.out.println("Move invalide");
                }
            }
        }
        return new int[]{temp.get(temp.size() - 1), temp2.get(temp2.size() - 1)};
    }



    private static int FindPosition(int[][] boardd) {
        int[][] board = new int[4][4];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                board[i + 1][j + 1] = boardd[j][i];
            }
        }
        //Option 1
        int returnVal = Test(new int[]{2, 2, 2, 1, 0, 0, 0, 1, 1}, board);
        if(returnVal != 0){
            return 1 + 24 * (returnVal - 1);
        }
        //Option 2
        returnVal = Test(new int[]{2, 2, 2, 0, 1, 0, 1, 0, 1}, board);
        if(returnVal != 0) return 2 + 24 * (returnVal - 1);
        //Option 3
        returnVal = Test(new int[]{2, 0, 2, 2, 1, 0, 0, 0, 1}, board);
        if(returnVal != 0) return 3 + 24 * (returnVal - 1);
        //Option 4
        returnVal = Test(new int[]{0, 2, 2, 1, 2, 0, 0, 0, 1}, board);
        if(returnVal != 0) return 4 + 24 * (returnVal - 1);
        //Option 5
        returnVal = Test(new int[]{2, 0, 2, 1, 1, 0, 0, 1, 0}, board);
        if(returnVal != 0) return 5 + 24 * (returnVal - 1);
        //Option 6
        returnVal = Test(new int[]{2, 2, 0, 1, 0, 1, 0, 0, 1}, board);
        if(returnVal != 0) return 6 + 24 * (returnVal - 1);
        //Option 7
        returnVal = Test(new int[]{0, 2, 2, 0, 2, 1, 1, 0, 0}, board);
        if(returnVal != 0) return 7 + 24 * (returnVal - 1);
        //Option 8
        returnVal = Test(new int[]{0, 2, 2, 2, 1, 1, 1, 0, 0}, board);
        if(returnVal != 0) return 8 + 24 * (returnVal - 1);
        //Option 9
        returnVal = Test(new int[]{2, 0, 2, 2, 0, 1, 0, 1, 0}, board);
        if(returnVal != 0) return 9 + 24 * (returnVal - 1);
        //Option 10
        returnVal = Test(new int[]{2, 2, 0, 1, 1, 2, 0, 0, 1}, board);
        if(returnVal != 0) return 10 + 24 * (returnVal - 1);
        //Option 11
        returnVal = Test(new int[]{0, 2, 2, 0, 1, 0, 0, 0, 1}, board);
        if(returnVal != 0) return 11 + 24 * (returnVal - 1);
        //Option 12
        returnVal = Test(new int[]{0, 2, 2, 0, 1, 0, 1, 0, 0}, board);
        if(returnVal != 0) return 12 + 24 * (returnVal - 1);
        //Option 13
        returnVal = Test(new int[]{2, 0, 2, 1, 0, 0, 0, 0, 1}, board);
        if(returnVal != 0) return 13 + 24 * (returnVal - 1);
        //Option 14
        returnVal = Test(new int[]{0, 0, 2, 2, 2, 1, 0, 0, 0}, board);
        if(returnVal != 0) return 14 + 24 * (returnVal - 1);
        //Option 15
        returnVal = Test(new int[]{2, 0, 0, 1, 1, 1, 0, 0, 0}, board);
        if(returnVal != 0) return 15 + 24 * (returnVal - 1);
        //Option 16
        returnVal = Test(new int[]{0, 2, 0, 2, 1, 1, 0, 0, 0}, board);
        if(returnVal != 0) return 16 + 24 * (returnVal - 1);
        //Option 17
        returnVal = Test(new int[]{0, 2, 0, 1, 1, 2, 0, 0, 0}, board);
        if(returnVal != 0) return 17 + 24 * (returnVal - 1);
        //Option 18
        returnVal = Test(new int[]{2, 0, 0, 2, 2, 1, 0, 0, 0}, board);
        if(returnVal != 0) return 18 + 24 * (returnVal - 1);
        //Option 19
        returnVal = Test(new int[]{0, 0, 2, 1, 2, 2, 0, 0, 0}, board);
        if(returnVal != 0) return 19 + 24 * (returnVal - 1);
        //Option 20
        returnVal = Test(new int[]{0, 0, 2, 2, 1, 0, 0, 0, 0}, board);
        if(returnVal != 0) return 20 + 24 * (returnVal - 1);
        //Option 21
        returnVal = Test(new int[]{0, 2, 0, 1, 2, 0, 0, 0, 0}, board);
        if(returnVal != 0) return 21 + 24 * (returnVal - 1);
        //Option 22
        returnVal = Test(new int[]{0, 2, 0, 0, 2, 1, 0, 0, 0}, board);
        if(returnVal != 0) return 22 + 24 * (returnVal - 1);
        //Option 23
        returnVal = Test(new int[]{2, 0, 0, 2, 1, 0, 0, 0, 0}, board);
        if(returnVal != 0) return 23 + 24 * (returnVal - 1);
        //Option 24
        returnVal = Test(new int[]{0, 0, 2, 0, 1, 2, 0, 0, 01}, board);
        if(returnVal != 0) return 24 + 24 * (returnVal - 1);


        return 69; //l'ordinateur ne peut pas jouer : la partie est gagnee

    }

    //Returns 0 if false, 1 if true, 2 if mirror
    private static int Test(int[] pos, int[][] board){

        boolean testTwo = false;
        if(pos[0] != board[1][3]) testTwo = true;
        else if(pos[1] != board[2][3]) testTwo = true;
        else if(pos[2] != board[3][3]) testTwo = true;
        else if(pos[3] != board[1][2]) testTwo = true;
        else if(pos[4] != board[2][2]) testTwo = true;
        else if(pos[5] != board[3][2]) testTwo = true;
        else if(pos[6] != board[1][1]) testTwo = true;
        else if(pos[7] != board[2][1]) testTwo = true;
        else if(pos[8] != board[3][1]) testTwo = true;


        if(testTwo){
            if(pos[0] != board[3][3]) return 0;
            else if(pos[1] != board[2][3]) return 0;
            else if(pos[2] != board[1][3]) return 0;
            else if(pos[3] != board[3][2]) return 0;
            else if(pos[4] != board[2][2]) return 0;
            else if(pos[5] != board[1][2]) return 0;
            else if(pos[6] != board[3][1]) return 0;
            else if(pos[7] != board[2][1]) return 0;
            else if(pos[8] != board[1][1]) return 0;

        }
        else{
            return 1;
        }

        return 2;
    }



    private static void Afficher(int[][] board){

        Clear(50);



        for(int i = board.length - 1; i >= 0; i--){
            for(int j = 0; j < board.length; j++){
                System.out.print(board[i][j]);
            }
            System.out.println("");
        }
    }

    private static void Setup(int[][] board) {
        for(int i = 0; i < 3; i++){
            board[0][i] = 1;
            board[2][i] = 2;
        }
    }

    private static boolean testInvalide(int[][] board, int posO1, int posO2, int posD1,int posD2) {

        if(posD1 - posO1> 1){
            return true;
        }


        if(board[posO1][posO2] != 1 || posO1 == posD1){
            return true;
        }
        else{
            if(posO2 != posD2){
                if(board[posD1][posD2] == 0 || board[posD1][posD2] == 1){
                    return true;
                }
                if(posO2 - posD2 < -1 || posO2 - posD2 > 1){
                    return true;
                }
            }
            else{
                if(board[posD1][posD2] == 2 || board[posD1][posD2] == 1){
                    return true;
                }
            }
        }
        return false;
    }

    private static void MovePiece(int[][] board, int posO1, int posO2, int posD1, int posD2) {
        board[posD1][posD2] = 1;
        board[posO1][posO2] = 0;
    }

    private static void SetupDefaultAIMoves(ArrayList<ArrayList<String>> AIMoves) {
        //Position 1
        ArrayList<String> AIMovesPosTemp = new ArrayList<String>();
        AIMovesPosTemp.add("2312");
        AIMovesPosTemp.add("2322");
        AIMovesPosTemp.add("3332");

        ArrayList<String> TempTab1 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab1.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(TempTab1);
        AIMovesPosTemp.clear();

        //Position 2
        AIMovesPosTemp.add("1312");
        AIMovesPosTemp.add("1322");


        ArrayList<String> TempTab2 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab2.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(TempTab2);
        AIMovesPosTemp.clear();
        //Position 3
        AIMovesPosTemp.add("1322");
        AIMovesPosTemp.add("1211");
        AIMovesPosTemp.add("3322");
        AIMovesPosTemp.add("3332");


        ArrayList<String> TempTab3 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab3.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(TempTab3);
        AIMovesPosTemp.clear();

        //Position 4
        AIMovesPosTemp.add("2312");
        AIMovesPosTemp.add("3332");
        AIMovesPosTemp.add("2221");
        AIMovesPosTemp.add("2231");


        ArrayList<String> TempTab4 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab4.add(i, AIMovesPosTemp.get(i));

        }
        AIMoves.add(TempTab4);
        AIMovesPosTemp.clear();
        //Position 5
        AIMovesPosTemp.add("1322");
        AIMovesPosTemp.add("3322");
        AIMovesPosTemp.add("3332");

        ArrayList<String> TempTab5 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab5.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(TempTab5);
        AIMovesPosTemp.clear();




        //Position 6
        AIMovesPosTemp.add("2312");
        AIMovesPosTemp.add("2322");
        AIMovesPosTemp.add("2332");
        AIMoves.add(AIMovesPosTemp);
        ArrayList<String> TempTab6 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab6.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(5, TempTab6);
        AIMovesPosTemp.clear();
        //Position 7
        AIMovesPosTemp.add("2332");
        AIMovesPosTemp.add("2211");
        AIMovesPosTemp.add("2221");

        ArrayList<String> TempTab7 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab7.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(6, TempTab7);
        AIMovesPosTemp.clear();
        //Position 8
        AIMovesPosTemp.add("2332");
        AIMovesPosTemp.add("3322");

        ArrayList<String> TempTab8 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab8.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(7, TempTab8);
        AIMovesPosTemp.clear();
        //Position 9
        AIMovesPosTemp.add("1211");
        AIMovesPosTemp.add("1221");

        ArrayList<String> TempTab9 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab9.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(8, TempTab9);
        AIMovesPosTemp.clear();
        //Position 10
        AIMovesPosTemp.add("1322");
        AIMovesPosTemp.add("2312");


        ArrayList<String> TempTab10 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab10.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(9, TempTab10);
        AIMovesPosTemp.clear();
        //Position 11 & 12
        AIMovesPosTemp.add("3322");
        AIMovesPosTemp.add("3332");

        ArrayList<String> TempTab11 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab11.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(10, TempTab11);


        ArrayList<String> TempTab12 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab12.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(11, TempTab12);
        AIMovesPosTemp.clear();

        //Position 13
        AIMovesPosTemp.add("3332");

        ArrayList<String> TempTab13 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab13.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(12, TempTab13);
        AIMovesPosTemp.clear();
        //Position 14
        AIMovesPosTemp.add("1211");
        AIMovesPosTemp.add("2221");

        ArrayList<String> TempTab14 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab14.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(13, TempTab14);
        AIMovesPosTemp.clear();


        //Position 15
        AIMovesPosTemp.add("1322");
        AIMovesPosTemp.add("2312");
        ArrayList<String> TempTab15 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab15.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(14, TempTab15);
        AIMovesPosTemp.clear();

        //Position 16
        AIMovesPosTemp.add("1211");
        AIMovesPosTemp.add("2332");
        ArrayList<String> TempTab16 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab16.add(i, AIMovesPosTemp.get(i));

        }
        AIMoves.add(15, TempTab16);
        AIMovesPosTemp.clear();

        //Position 17
        AIMovesPosTemp.add("2312");
        AIMovesPosTemp.add("3231");
        ArrayList<String> TempTab17 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab17.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(16, TempTab17);
        AIMovesPosTemp.clear();

        //Position 18
        AIMovesPosTemp.add("1211");
        AIMovesPosTemp.add("2221");
        ArrayList<String> TempTab18 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab18.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(17, TempTab18);
        AIMovesPosTemp.clear();
        //Position 19
        AIMovesPosTemp.add("2221");
        AIMovesPosTemp.add("3231");
        ArrayList<String> TempTab19 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab19.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(18, TempTab19);
        AIMovesPosTemp.clear();

        //Position 20
        AIMovesPosTemp.add("3322");
        AIMovesPosTemp.add("3332");
        AIMovesPosTemp.add("1211");
        ArrayList<String> TempTab20 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab20.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(19, TempTab20);
        AIMovesPosTemp.clear();

        //Position 21
        AIMovesPosTemp.add("2312");
        AIMovesPosTemp.add("2221");
        ArrayList<String> TempTab21 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab21.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(20, TempTab21);
        AIMovesPosTemp.clear();

        //Position 22
        AIMovesPosTemp.add("2332");
        AIMovesPosTemp.add("2221");
        AIMovesPosTemp.add("1211");
        ArrayList<String> TempTab22 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab22.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(21, TempTab22);
        AIMovesPosTemp.clear();

        //Position 23
        AIMovesPosTemp.add("1322");
        AIMovesPosTemp.add("1211");
        ArrayList<String> TempTab23 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab23.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(22, TempTab23);
        AIMovesPosTemp.clear();

        //Position 24
        AIMovesPosTemp.add("3322");
        AIMovesPosTemp.add("3231");
        ArrayList<String> TempTab24 = new ArrayList<String>();
        for(int i = 0; i < AIMovesPosTemp.size(); i++){
            TempTab24.add(i, AIMovesPosTemp.get(i));
        }
        AIMoves.add(23, TempTab24);
        AIMovesPosTemp.clear();
    }

    private static int MovePieceComp(int positionActuelle, int[][] board, ArrayList<ArrayList<String>> AIMoves) {
        Random random = new Random();
        int rand = 0;
        if(positionActuelle < 24){
            ArrayList<String> poss = AIMoves.get(positionActuelle - 1);
            rand = random.nextInt(poss.size());
            System.out.println("size : " + poss.size());
            System.out.println("rand : " + rand);
            board[poss.get(rand).charAt(3) - '1'][poss.get(rand).charAt(2) - '1'] = 2;
            board[poss.get(rand).charAt(1) - '1'][poss.get(rand).charAt(0) - '1'] = 0;
            return rand;
        }
        else{
            ArrayList<String> poss = AIMoves.get(positionActuelle - 25);
            rand = random.nextInt(poss.size());
            System.out.println("size : " + poss.size());
            System.out.println("rand : " + rand);
            int pos1 = (poss.get(rand).charAt(3) - '0');
            int pos2 = 4 - (poss.get(rand).charAt(2) - '0');
            int pos3 = (poss.get(rand).charAt(1) - '0');
            int pos4 = 4 - (poss.get(rand).charAt(0) - '0');

            board[pos1 - 1][pos2 - 1] = 2;
            board[pos3 - 1][pos4 - 1] = 0;
            return rand;
        }


        //board[posD1][posD2] = 1;
        //board[posO1][posO2] = 0;
    }

    private static boolean TestIfWon(int[][] board) {
        for(int i = 0; i < 3; i++){
            if(board[2][i] == 1){
                return true;
            }
            if(board[0][i] == 2){
                return false;
            }
        }
        boolean PC = false;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board[i][j] == 2){
                    PC = true;
                }
            }
        }
        if(!PC){
            return true;
        }

        boolean user = false;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board[i][j] == 1){
                    user = true;
                }
            }
        }
        if(!user){
            return false;
        }
        return true;

    }

    private static boolean TestIfPCWon(int[][] board) {
        for(int i = 0; i < 3; i++){
            if(board[0][i] == 2){
                return true;
            }
        }
        return false;

    }

    private static boolean  TestText(String jeu) {
        if(jeu.length() != 4){
            return false;
        }
        for(int i = 0; i < 4; i++){
            if(!(jeu.charAt(i) == '1' || jeu.charAt(i) == '2' || jeu.charAt(i) == '3')) return false;
        }
        return true;
    }

    private static void Clear(int nb) {
        for (int i = 0; i < nb; i++){
            System.out.println("");
        }
    }
}
