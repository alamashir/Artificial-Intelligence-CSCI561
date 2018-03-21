import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

@SuppressWarnings("Duplicates")

public class homework {

    static HashSet<pair> trees = new HashSet<>();
    static pair[] currentState;
    static int blankSpace=0;
    static Random ran = new Random();
    static long start;
    static long min45 = 270000;
    static long min2 = 120000;
    static ArrayList<state> lizardsBFS = new ArrayList<>();
    static pair[] solution;
    static pair[] lizards;
    static int insertedQueens = 0;
    static pair[][] treeArray;



    private static class pair {
        int key;
        int value;
        pair(int key, int value){
            this.key = key;
            this.value = value;
        }

        public boolean equals(Object o){
            pair p = (pair)o;
            return this.key == p.key && this.value == p.value;
        }
        @Override
        public int hashCode() {
            return 3*this.value + 7*this.key;
        }
    }

    private static class state{
        pair[] currentLizards;
        pair currentPosition;
        state(pair[] currentLizards, pair currentPosition){
            this.currentLizards = currentLizards;
            this.currentPosition = currentPosition;
        }
    }

    //Depth First Search
    private static Boolean isValidDFS(int p, int j){


        if(trees.contains(treeArray[p][j]))
            return false;

        for (int i = 0; i < insertedQueens; i++) {

            Boolean safeHor = true, safeVer = true, safeDiag = true, safeAnti = true;
            if (lizards[i].key == p) {
                //System.out.println("UnSafe horizontal queen at: " + p + ", " + j);
                safeHor=false;
                for(int k=lizards[i].value+1; k < j; k++){
                    if(trees.contains(treeArray[p][k])) {
                        //System.out.println("Safe horizontal tree at: " + (p+j-k) + ", " + k);
                        safeHor = true;
                        break;
                    }else{
                        //System.out.println("UnSafe horizontal tree at: " + (p+j-k) + ", " + k);
                    }
                }
            }

            if (lizards[i].value == j) {
                //System.out.println("UnSafe vertical queen at: " + p + ", " + j);
                safeVer = false;
                for(int k=lizards[i].key+1; k < p; k++){
                    if(trees.contains(treeArray[k][j])) {
                        //System.out.println("Safe vertical tree at: " + (p+j-k) + ", " + k);
                        safeVer = true;
                        break;
                    }else{
                        //System.out.println("UnSafe vertical tree at: " + (p+j-k) + ", " + k);

                    }
                }
            }

            if(lizards[i].key + lizards[i].value == p + j ){

                //System.out.println("UnSafe anti diag queen at: " + lizards[i].key + ", " + lizards[i].value);
                safeAnti=false;
                for(int k=lizards[i].value+1; k < j; k++){
                    if(trees.contains(treeArray[p+j-k][k])) {
                        //System.out.println("Safe anti diag tree at: " + (p+j-k) + ", " + k);
                        safeAnti = true;
                        break;
                    }else{
                        //System.out.println("UnSafe anti diag tree at: " + (p+j-k) + ", " + k);
                    }

                }
            }

            if(lizards[i].key - lizards[i].value == p - j){

                //System.out.println("UnSafe diag queen at: " + lizards[i].key + ", " + lizards[i].value);
                safeDiag=false;
                for(int k=lizards[i].value+1; k < j; k++){
                    if(trees.contains(treeArray[k+p-j][k])) {
                        //System.out.println("Safe diag tree at: " + (k+p-j) + ", " + (k));
                        safeDiag = true;
                        break;
                    } else{
                        //System.out.println("UnSafe diag tree at: " + (k) + ", " + (k+p-j));
                    }
                }
            }/*
            if(!(safeHor&&safeVer&&safeAnti&&safeDiag)) {
                return false;
            }*/

            if(!safeHor || !safeVer || !safeAnti || !safeDiag){
                return false;
            }

        }

        return true;
    }
    private static Boolean nQueensDFSRecurWithTree(int i, int j, int k, int n) throws TimeUpException{

        if(System.currentTimeMillis()-start>min45){
            throw new TimeUpException();
        }

        if(k==0){
            return true;
        }

        if(i==n){
            i=0;
            j++;
        }
        //System.out.print(".");
        //printArrWithTree(n);


        if(j==n){
            return false;
        }

        if(isValidDFS(i, j)){
            lizards[insertedQueens] = new pair(i, j);
            insertedQueens++;
            if (nQueensDFSRecurWithTree(i+1, j, k-1, n)) {
                return true;
            } else{
                //System.out.println("Compromise");
                insertedQueens--;
                return nQueensDFSRecurWithTree(i + 1, j, k, n);
            }
        }else{
            return nQueensDFSRecurWithTree(i + 1, j, k, n);
        }
    }


    //Breadth First Search
    private static Boolean isValidBFS(int p, int j, pair[] currentLizards){
        if(trees.contains(new pair(p,j)))
            return false;
        for (int i = 0; i < currentLizards.length; i++) {

            Boolean safeHor = true, safeVer = true, safeDiag = true, safeAnti = true;
            if (currentLizards[i].key == p) {
                //System.out.println("UnSafe horizontal queen at: " + p + ", " + j);
                safeHor=false;
                for(int k=currentLizards[i].value+1; k < j; k++){
                    if(trees.contains(new pair(p, k))) {
                        //System.out.println("Safe horizontal tree at: " + (p+j-k) + ", " + k);
                        safeHor = true;
                        break;
                    }else{
                        //System.out.println("UnSafe horizontal tree at: " + (p+j-k) + ", " + k);
                    }
                }
            }

            if (currentLizards[i].value == j) {
                //System.out.println("UnSafe vertical queen at: " + p + ", " + j);
                safeVer = false;
                for(int k=currentLizards[i].key+1; k < p; k++){
                    if(trees.contains(new pair(k, j))) {
                        //System.out.println("Safe vertical tree at: " + (p+j-k) + ", " + k);
                        safeVer = true;
                        break;
                    }else{
                        //System.out.println("UnSafe vertical tree at: " + (p+j-k) + ", " + k);

                    }
                }
            }

            if(currentLizards[i].key + currentLizards[i].value == p + j ){

                //System.out.println("UnSafe anti diag queen at: " + currentLizards[i].key + ", " + currentLizards[i].value);
                safeAnti=false;
                for(int k=currentLizards[i].value+1; k < j; k++){
                    if(trees.contains(new pair(p+j-k, k))) {
                        //System.out.println("Safe anti diag tree at: " + (p+j-k) + ", " + k);
                        safeAnti = true;
                        break;
                    }else{
                        //System.out.println("UnSafe anti diag tree at: " + (p+j-k) + ", " + k);
                    }

                }
            }

            if(currentLizards[i].key - currentLizards[i].value == p - j){

                //System.out.println("UnSafe diag queen at: " + currentLizards[i].key + ", " + currentLizards[i].value);
                safeDiag=false;
                for(int k=currentLizards[i].value+1; k < j; k++){
                    if(trees.contains(new pair(k+p-j,k))) {
                        //System.out.println("Safe diag tree at: " + (k+p-j) + ", " + (k));
                        safeDiag = true;
                        break;
                    } else{
                        //System.out.println("UnSafe diag tree at: " + (k) + ", " + (k+p-j));
                    }
                }
            }/*
            if(!(safeHor&&safeVer&&safeAnti&&safeDiag)) {
                return false;
            }*/

            if(!safeHor || !safeVer || !safeAnti || !safeDiag){
                return false;
            }

        }

        return true;
    }
    private static boolean nQueensBFSIter(int k, int n){
        int row, col;
        pair[] blankpair = {};
        if(!trees.contains(new pair(0,0))){
            pair[] newpair = {new pair(0,0)};
            lizardsBFS.add(new state(newpair, new pair(0,0)));
            //System.out.println("(0, 0) is valid");
        }
        lizardsBFS.add(new state(blankpair, new pair(0,0)));
        //System.out.println("n: " + n);



        while(!lizardsBFS.isEmpty()){

            if(System.currentTimeMillis()-start>min45){
                return false;
            }

            //System.out.println("row: " + row + " col: " + col);
            state currentState = lizardsBFS.get(0);
            pair[] current = currentState.currentLizards;
            row = currentState.currentPosition.key;
            col = currentState.currentPosition.value;


            //printArrWithTree(current, n);

            lizardsBFS.remove(0);
            if(current.length==k){
                solution = current;
                return true;
            } else {
                if(row==n-1){
                    row=-1;
                    col++;
                }
                if (col < n) {
                    if(isValidBFS(row+1, col, current)){
                        //System.out.println("(" + (row+1) + ", " + col + ") is valid");
                        pair[] newpair = new pair[current.length+1];
                        newpair[newpair.length-1] = new pair(row+1, col);
                        System.arraycopy(current, 0, newpair, 0, current.length);
                        lizardsBFS.add(new state(newpair, new pair(row+1, col)));
                    }
                    lizardsBFS.add(new state(current, new pair(row+1, col)));
                }
            }
        }
        return false;
    }

    //Simulated Annealing
    private static double schedule(double t){
        return 10/Math.log(1+t);
    }
    private static pair generateNextValidLocation(int n){
        int x = ran.nextInt(n);
        int y = ran.nextInt(n);
        if(trees.contains(treeArray[x][y])){
            return generateNextValidLocation(n);
        }else{
            boolean valid = true;
            for (pair tempLocation : currentState) {
                if (tempLocation.key==x && tempLocation.value==y){
                    valid = false;
                    break;
                }
            }
            if(valid){
                return treeArray[x][y];
            }else{
                return generateNextValidLocation(n);
            }
        }
    }
    private static int countConflicts(int x, int y, int n){
        int conflicts = 0;
        // above
        for(int i=x-1; i>=0; i--){
            if(trees.contains(treeArray[i][y]))
                break;
            for (pair tempLocation : currentState) {
                if (tempLocation.key==i && tempLocation.value==y){
                    conflicts++;
                }
            }
        }
        //below
        for(int i=x+1; i<n; i++){
            if(trees.contains(treeArray[i][y]))
                break;
            for (pair tempLocation : currentState) {
                if (tempLocation.key==i && tempLocation.value==y){
                    conflicts++;
                }
            }
        }
        //left
        for(int i=y-1; i>=0; i--){
            if(trees.contains(treeArray[x][i]))
                break;
            for (pair tempLocation : currentState) {
                if (tempLocation.key==x && tempLocation.value==i){
                    conflicts++;
                }
            }
        }
        //right
        for(int i=y+1; i<n; i++){
            if(trees.contains(treeArray[x][i]))
                break;
            for (pair tempLocation : currentState) {
                if (tempLocation.key==x && tempLocation.value==i){
                    conflicts++;
                }
            }
        }
        //top left
        for(int i=x-1, j=y-1; i>=0 && j>=0; i--, j--){
            if(trees.contains(treeArray[i][j]))
                break;
            for (pair tempLocation : currentState) {
                if (tempLocation.key==i && tempLocation.value==j){
                    conflicts++;
                }
            }
        }
        //top right
        for(int i=x-1, j=y+1; i>=0 && j<n; i--, j++){
            if(trees.contains(treeArray[i][j]))
                break;
            for (pair tempLocation : currentState) {
                if (tempLocation.key==i && tempLocation.value==j){
                    conflicts++;
                }
            }

        }
        //bottom right
        for(int i=x+1, j=y+1; i<n && j<n; i++, j++){
            if(trees.contains(treeArray[i][j]))
                break;
            for (pair tempLocation : currentState) {
                if (tempLocation.key==i && tempLocation.value==j){
                    conflicts++;
                }
            }
        }
        //bottom left
        for(int i=x+1, j=y-1; i<n && j>=0; i++, j--){
            if(trees.contains(treeArray[i][j]))
                break;
            for (pair tempLocation : currentState) {
                if (tempLocation.key==i && tempLocation.value==j){
                    conflicts++;
                }
            }
        }
        return conflicts;
    }
    private static boolean nQueensSAIter(int k, int n) {

        Double Temperature;
        int randomQueenPosition;
        long deltaE;
        int newConflicts, oldConflicts;
        int stableState = 0;
        int nsquare = n*n;

        if(trees.size() + k == nsquare){
            boolean solved = true;
            for(pair p : currentState){
                if(countConflicts(p.key, p.value, n)!=0) {
                    solved = false;
                    break;
                }
            }
            if(solved){
                System.out.println("Breaking because no new place available");
                return true;
            }else{
                return false;

            }
        }


        for(Double t=2.0; t>0; t++){
            //System.out.print("t: " + t);
            Temperature = schedule(t);
            //System.out.print(" Temperature: " + Temperature);
            if(Temperature<0)
                return true;
            if(stableState==nsquare){
                boolean solved = true;
                for(pair p : currentState){
                    if(countConflicts(p.key, p.value, n)!=0) {
                        solved = false;
                        break;
                    }
                }
                if(solved){
                    System.out.println("Breaking because of stable iterations");
                    return true;
                }else{
                    //return true;
                    stableState=0;
                }
            }

            if(System.currentTimeMillis()-start>min45){
                return false;
            }


            randomQueenPosition = ran.nextInt(k);
            //System.out.print(" Random Queen Position: " + randomQueenPosition);
            pair randomQueen = currentState[randomQueenPosition];
            oldConflicts = countConflicts(randomQueen.key, randomQueen.value, n);
            pair newPosition = generateNextValidLocation(n);
            newConflicts = countConflicts(newPosition.key, newPosition.value, n);
            deltaE = newConflicts - oldConflicts;
            //if (true) {
            if (oldConflicts!=0) {
                if(deltaE<=0) {
                    //System.out.println(" swapped because of lower value");
                    currentState[randomQueenPosition] = treeArray[newPosition.key][newPosition.value];
                    stableState = 0;
                }else{
                    Double expDeltaETemp = Math.exp(-1*deltaE/Temperature);
                    Double mathRandom = Math.random();
                    if(mathRandom < expDeltaETemp){
                        //System.out.print(" exp: " + expDeltaETemp +" mathRandom: "+ mathRandom);
                        //System.out.println(" swapped because of probability");
                        currentState[randomQueenPosition] = treeArray[newPosition.key][newPosition.value];
                        stableState=0;
                    }else{
                        stableState++;
                        //System.out.println();
                    }
                }
            }else{
                stableState++;
                //System.out.println();
            }
        }
        return true;
    }


    private static void printArr(int[][] arr, int n){
        try{
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            writer.println("OK");
            for(int i=0; i<n; i++){
                for(int j=0; j<n; j++){
                    if (trees.contains(new pair(i,j))) {
                        System.out.print("2");
                        writer.print("2");
                    } else {
                        System.out.print(arr[i][j]);
                        writer.print(arr[i][j]);
                    }
                }
                writer.println();
                System.out.println();
            }
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
    private static void printArrWithTree(pair[] list, int n) {
        int[][] atemp = new int[n][n];


        for (int i = 0; i < list.length; i++) {
            pair liz = list[i];
            atemp[liz.key][liz.value] = 1;
        }
        System.out.println("Have "+ list.length + " lizards below" );
        printArr(atemp,n);

    }

    private static void printFailToFile(){
        try{
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            writer.println("FAIL");
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static Boolean ReadFromFile() {
        int n=0, k=0, tempk=0;
        String algo = "";
        //Scanner in = new Scanner(System.in);
        System.out.print("Running ");
        String fnm = "";

        File file = new File("input"+fnm+".txt");
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            algo = br.readLine();
            n = Integer.parseInt(br.readLine());
            k = Integer.parseInt(br.readLine());
            currentState = new pair[k];
            treeArray = new pair[n][n];
            lizards = new pair[k];

            for(int i=0; i<n; i++){
                for(int j=0; j<n; j++){
                    treeArray[i][j] = new pair(i,j);
                }
            }

            for(int i=0; i<n; i++){
                line = br.readLine();
                char[] charArray = line.toCharArray();
                for (int i1 = 0; i1 < n; i1++) {
                    if (charArray[i1] == '2') {
                        trees.add(treeArray[i][i1]);
                    }else{
                        if(tempk<k){
                            //System.out.println("Inserting at (" + i + ", " + i1 + ")");
                            currentState[tempk] = new pair(i,i1);
                            tempk++;
                        }
                    }
                }
                //printArrWithTree(currentState, n);
            }
        } catch (Exception e){
            e.printStackTrace();
            try{
                PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
                writer.println("FAIL");
                writer.close();
                return false;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }



        start = System.currentTimeMillis();


        switch (algo) {
            case "DFS":
                try {
                    if(nQueensDFSRecurWithTree(0,0, k,  n)){
                        printArrWithTree(lizards, n);
                    }else{
                        printFailToFile();
                        System.out.println("FAIL");
                    }
                } catch (TimeUpException e) {
                    printFailToFile();
                    System.out.println("FAIL because of time up");
                }
                break;

            case "BFS":
                if (nQueensBFSIter(k, n)) {
                    printArrWithTree(solution, n);
                } else {
                    printFailToFile();
                    System.out.println("FAIL");
                }
                break;

            case "SA":
                if (tempk == k && nQueensSAIter(k, n)) {
                    printArrWithTree(currentState, n);
                } else {
                    printFailToFile();
                    System.out.println("FAIL");
                }
                break;
        }


        long end = System.currentTimeMillis();
        System.out.println("\nTime: " + (end-start) + "ms");

        return true;
    }



    public static void main(String... args){
        ReadFromFile();
    }

    private static class TimeUpException extends Exception {
    }
}
