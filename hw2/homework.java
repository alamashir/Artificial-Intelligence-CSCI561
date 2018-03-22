import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class homework {
static  ArrayList<Integer> value=new ArrayList<Integer>();
static  ArrayList <pos> store =new ArrayList<>();
static rowcol colcor;
char c='0';
static int dep =0;
static int mD =5;
    static int wD = 3;
    static int o = 0;
    static float u =0;
    public static void matrix(int r, int c){
        System.out.println(r+c);
    }

    static class pos {
        int row;
        int col;


        pos(int row, int col) {
            this.row = row;
            this.col = col;


        }





        public boolean equals(Object o){
            return ((pos) o).row==row && ((pos) o).col==col;
        }


    }
    public static class rowcol
    {
        int i;
        int j;
        int score;
        int mine;
        int opp;
        char [][] parent;
        char [][] curarr;
        ArrayList<pos> cluster;

        rowcol(int r1, int c1, char[][] temporary, int maxscore)
        {
            this.i =r1;
            this.j =c1;
            this.parent =temporary;
            score = maxscore;
        }

        rowcol(rowcol a){
            this.i = a.i;
            this.j = a.j;
            this.parent = a.parent;
            this.curarr = a.curarr;
        }

        int evaluate(){
            return mine - opp;
        }

        public void saveCluster(ArrayList<pos> cluster) {
            this.cluster = new ArrayList<>(cluster);
        }

        public void setcurarr(char[][] arrarr)
        {
            curarr=arrarr;
        }
        public void setMine(int mine)
        {
            this.mine = mine;
        }
        public void setOpp(int opp)
        {
            this.opp = opp;
        }
    }







    public static ArrayList<rowcol> cluster(int n, char[][] starr2, char[][] starr, boolean turns, int mine, int oppo) {

        int large = 0;
        int count=0;
        ArrayList<rowcol> listofcoord=new ArrayList<>();
        for (int k = 0; k < value.size(); k++) {
            int p=0;
           p=value.get(k);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (Character.getNumericValue(starr2[i][j]) == p && p!=-1)

                    {
                        large = elements(i, j, starr2, starr2.length, p);


                        if (turns) {
                            listofcoord.add(new rowcol(i,j,starr, large*large));
                            listofcoord.get(listofcoord.size()-1).setMine(mine+large*large);
                            listofcoord.get(listofcoord.size()-1).setOpp(oppo);


                        } else {
                            listofcoord.add(new rowcol(i,j,starr, large*large));
                            listofcoord.get(listofcoord.size()-1).setOpp(oppo+large*large);
                            listofcoord.get(listofcoord.size()-1).setMine(mine);
                        }
                        listofcoord.get(listofcoord.size()-1).saveCluster(store);
                        store.clear();




                    }


                }


            }



            starr2 = makecpy(starr);
        }


        if (turns) {
            listofcoord.sort(new Comparator<rowcol>() {
                @Override
                public int compare(rowcol o1, rowcol o2) {
                    return o2.mine -o1.mine;
                }
            });
        } else {
            listofcoord.sort(new Comparator<rowcol>() {
                @Override
                public int compare(rowcol o1, rowcol o2) {
                    return o2.opp -o1.opp;
                }
            });
        }

        if(listofcoord.size()> wD)
        {
            listofcoord = new ArrayList<>(listofcoord.subList(0, wD));
        }
        for(rowcol i : listofcoord){
            i.setcurarr(movedown(n, i.parent, i.cluster));
        }


        return listofcoord;
    }
    public static int elements(int x, int y, char [][] starr2, int n, int p){
        if(((x<0 || y<0) || (x>=n || y>=n))  ||  Character.getNumericValue(starr2[x][y]) !=p || (Character.getNumericValue(starr2[x][y]) == -1 ))
            return 0;

        store.add(new pos(x,y));

        starr2[x][y] = (char) -1;


        return 1 +  elements(x-1,y,starr2,starr2.length,p)  + elements(x,y-1,starr2,starr2.length,p) + elements(x,y+1,starr2,starr2.length,p) +  elements(x+1,y,starr2,starr2.length,p) ;
    }



    public static char[][] movedown(int n, char[][] strarr, ArrayList<pos> points){


        char[][] cpy= makecpy(strarr);
        char[][] strarr2=new char[n][n];


        for(int i=0;i<n;i++)
        {
            for (int j = 0; j < n; j++)
            {
                if(points.contains(new pos(i,j)))
                {

                    cpy[i][j]= '*';
                }
            }
        }



        for(int i=0;i<n;i++) {
            int k=n-1;
            for (int j = n - 1; j >= 0; )
            {
                if (!(cpy[j][i] == '*'))
                {

                    strarr2[k][i] = cpy[j][i];
                    j--;
                    k--;
                }
                else
                {
                    j--;
                }

            }
            for(;k>=0;k--)
            {
                strarr2[k][i]= '*';
            }
        }


        return strarr2;


    }
    public static int player1(rowcol y, int alp, int bet, int mydepth)
    {


        if(mydepth> mD)
        {

            return y.evaluate();
        }
        ArrayList<rowcol> coordinateslist=cluster(o, makecpy(y.curarr),y.curarr, true,y.mine, y.opp);
        if(coordinateslist.size()==0){
            return y.evaluate();
        }

        for(rowcol w : coordinateslist){

            int str = player2(w,alp,bet,mydepth+1);

            if(alp < str){
                if(mydepth==0){


                    colcor = new rowcol(w);
                }
                alp = str;
            }
            if(alp>=bet)
            {

                return bet;
            }
        }
        return alp;

    }



    public static char[][] makecpy(char[][] arr) {
        char[][] arr2 = new char[o][o];
        for (int i = 0; i < arr.length; i++)
        {
            for (int j = 0; j< o; j++){
                arr2[i][j] = arr[i][j];
            }
        }
        return arr2;
    }

    private static Boolean element(int w) {
        for(int f=0;f<value.size();f++)
        {
            if(value.get(f)==w)
            {
                return false;
            }
        }
        return true;

    }

    public static int player2(rowcol z, int alp, int bet, int mydepth)
    {

        if (mydepth > mD)
        {

            return z.evaluate();
        }
        ArrayList<rowcol> coordinateslist = cluster(o, makecpy(z.curarr), z.curarr, false,z.mine,z.opp);
        if(coordinateslist.size()==0){
            return z.evaluate();
        }

        for (rowcol s : coordinateslist)
        {
            bet = Integer.min(bet, player1(s, alp, bet, mydepth+1));

            if (bet <= alp)
            {
                return alp;
            }
        }

        return bet;
    }


    public static void callibrate(float t)
    {
        if(t>120)
        {
            mD=6;
            wD=4;
        }
        if(t>60&&t<=120)
        {
            mD=5;
            wD=3;
        }
        if(t>=30&&t<=60)
        {
            mD=4;
            wD=2;
        }
        if(t<30)
        {
            mD=1;
            wD=1;
        }
    }
    public static void main(String args[] ){



        int f = 0;
        int counter = 0;

        

        String fileName = "input.txt";


        String line = null;

        char[][] strarr = new char[o][o];
        try {

            FileReader fileReader = new FileReader(fileName);


            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String strN = bufferedReader.readLine();
            String strF = bufferedReader.readLine();
            String strT = bufferedReader.readLine();
            o = Integer.parseInt(strN);
            f = Integer.parseInt(strF);
            u = Float.parseFloat(strT);
            strarr = new char[o][o];


            while ((line = bufferedReader.readLine()) != null && counter < o) {
                strarr[counter] = line.toCharArray();

                counter++;
            }
            System.out.println("File sucessfully read: ");
            bufferedReader.close();

        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long begin=  System.currentTimeMillis();

        callibrate(u);

        char [][] copy = new char[o][o];
        value.add(Character.getNumericValue(strarr[0][0]));

        for(int i = 0; i < strarr.length; i++)
        {

            for(int j = 0; j < strarr[i].length; j++)
            {
                copy[i][j] = strarr[i][j];

                if(element(Character.getNumericValue(strarr[i][j]))) {

                    value.add(Character.getNumericValue(strarr[i][j]));

                }

            }
        }
        rowcol initial = new rowcol(-1,-1,strarr,0);
        initial.mine =0;
        initial.opp =0;
        initial.setcurarr(strarr);
        player1(initial,Integer.MIN_VALUE,Integer.MAX_VALUE,0);

        System.out.println("File sucessfully written: ");
        try {
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            char colChar = ((char)(colcor.j +65));
            writer.print(colChar);

            writer.println((colcor.i +1));


            for(int i = 0; i< o; i++){
                for(int j = 0; j< o; j++){
                    writer.print(colcor.curarr[i][j]);
                }
                writer.println();

            }

            writer.close();
        }catch(IOException e){
            System.out.println(
                    "Error Printing file '"
            );

        }




    }

}
