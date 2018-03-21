
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.lang.String;







public class homework {


    static int numberofQueries;
    static int numberofStatements;
    static String[] queries;
    static String[] statements;
    static ArrayList<String> kb = new ArrayList<>();

    static ArrayList<Boolean> results = new ArrayList<Boolean>();
    static ArrayList<predicates> pospred= new ArrayList<>();
    static ArrayList<predicates> negpred=new ArrayList<>();



    static class predicates {
        String key;

        ArrayList<Integer> val= new ArrayList<>();


        predicates(String key,ArrayList<Integer> val) {
            this.key=key;
            this.val=val;
        }
    }


    static boolean has(ArrayList<predicates> P,String g){
        Boolean flag=false;
        for(int q=0;q<P.size();q++){

            if(P.get(q).key.equals(g)){
                flag=true;
            }


        }

        return flag;

    }


    static int getno(ArrayList<predicates> P,String g){
        int fl=0;
        for(int q=0;q<P.size();q++){

            if(P.get(q).key.equals(g)){
                fl=q;
            }


        }

        return fl;


    }


    //get Predicate name
    public static String predName(String pred) {
        String s = "";

        for (int i = 0; i < pred.length(); i++) {
            char ch = pred.charAt(i);

            if (ch == '~') {
                continue;
            } else {
                s = pred.substring(i, pred.indexOf("("));
                break;
            }
        }
        return s;
    }
    public static void add(String[] sen){

        String s[];
        String s1[];
        int l=0;


        ArrayList<Integer> pno=new ArrayList<>();
        ArrayList<Integer> nno=new ArrayList<>();

        for(int y=0;y<sen.length;y++){

            s=sen[y].split("[|]");
            for(int z=0;z<s.length;z++){

                s1=s[z].split("[(]");

                if(s1[0].toCharArray()[0]=='~'){

                    if(!has(negpred,s1[0].substring(s1[0].indexOf("~") +1))) {

                        nno = new ArrayList<>();
                        nno.add(y);
                        negpred.add(new predicates(s1[0].substring(s1[0].indexOf("~") + 1), nno));


                    }else{

                        l=getno(negpred,s1[0].substring(s1[0].indexOf("~")+1));

                        negpred.get(l).val.add(y);

                        printMes("NAME: " + negpred.get(l).key + " SIZE: " + negpred.get(l).val.size());



                    }

                }
                else{

                    if(!has(pospred,s1[0].substring(s1[0].indexOf("~") +1))) {

                        pno = new ArrayList<>();
                        pno.add(y);
                        pospred.add(new predicates(s1[0].substring(s1[0].indexOf("~") + 1), pno));


                    }else{

                        l=getno(pospred,s1[0].substring(s1[0].indexOf("~")+1));

                        pospred.get(l).val.add(y);
                        printMes("NAME: " + pospred.get(l).key + " SIZE: " + pospred.get(l).val.size());




                    }










                }



            }

        }







    }

    //get Predicate name regardless of not.
    public static String allname(String pred) {
//        String m = "";
//
//        m = pred.startsWith("~") ? pred.substring(1, pred.indexOf("(")) : pred.substring(0, pred.indexOf("("));

        return getOnlyName(pred);

    }


    public static String getOnlyName(String pred){
        return pred.substring(pred.indexOf('~')+1).split("[(]")[0];
    }


    //Predicate type
    public static boolean predicatetype(String pred) {
        return (pred.startsWith("~"));

    }

    //splits the sentences from OR
    public static String[] OrSplit(String pred) {


        String[] ors = pred.split("\\|");
        return ors;
    }


    //check if sentence is negative
    static boolean isnegative(String pred) {

        return pred.toCharArray()[0] == '~';
    }


    //Number of parameters in a statement
    public static int findTheNoOfParameter(String pred) {
        // TODO Auto-generated method stub
        String t = pred.substring(pred.indexOf("("), pred.indexOf(")"));

        return t.split(",").length;
    }


    //The variables inside a predicate
    public static String[] predvar(String pred) {
        String str;
        String s[];
        str = pred.substring(pred.indexOf("(") + 1, pred.indexOf(")"));

        s = str.split("[,]");

        return s;
    }

    //checking if variable
    private static boolean isVariable(String temp) {
        // TODO Auto-generated method stub

        char a = temp.charAt(0);
        if (a >= 'a' && a <= 'z') {
            return true;
        }
        return false;
    }

    //checking if constant
    private static boolean isConstant(String pred) {

        if (Character.isUpperCase((pred).charAt(0)))
            return true;
        return false;

    }


    //negates a query
    private static String negateQuery(String pred) {
        if (pred.charAt(0) == '(') {
            pred = pred.substring(1, pred.length() - 1);
        }
        pred = "~" + pred;

        return pred;
    }


    public static HashMap<String, String> finalunify(String X, String Y) {
        String[] arg1;
        String[] arg2;
        arg1 = predvar(X);
        arg2 = predvar(Y);
        HashMap<String, String> mapping = new HashMap<>();

        for (int i = 0; i < arg1.length; i++) {

            if (isConstant(arg1[i]) && isConstant(arg2[i])) {

                //Same arg and constant
                if (arg1[i].equals(arg2[i])) {
                    continue;
                } else {
                    return null;
                }
            }

            if (isConstant(arg1[i]) && isVariable(arg2[i])) {

                if (mapping.containsKey(arg2[i])) {
                    if (mapping.get(arg2[i]).equals(arg1[i])) {
                        continue;
                    } else {
                        return null;
                    }
                } else {
                    printMes(arg2[i] + "/" + arg1[i]);
                    mapping.put(arg2[i], arg1[i]);

                }

            }

            if (isVariable(arg1[i]) && isConstant(arg2[i])) {

                if (mapping.containsKey(arg1[i])) {
                    if (mapping.get(arg1[i]).equals(arg2[i])) {
                        continue;
                    } else {
                        return null;
                    }
                } else {
                    printMes(arg1[i] + "/" + arg2[i]);
                    mapping.put(arg1[i], arg2[i]);

                }

            }


            //
            if (isVariable(arg1[i]) && isVariable((arg2[i]))) {

                //var var case
            }


        }

        return mapping;


    }

    public static String unify(String A, String B) {

        ArrayList<String> arr = new ArrayList<>();
        HashMap<String, String> unimap = new HashMap<>();
        String str = "";
        String[] p;
        String[] q;
        String str1 = "";
        String str2 = "";

        String[] tokens = B.split("[ ]*[|][ ]*");

        String[] tok = A.split("[ ]*[|][ ]*");
        int v = -1;

        for (int i = 0; i < tok.length; i++) {
            if (allname(tok[i]).equals(allname(tokens[0])) && ((isnegative(tok[i]) && !isnegative(tokens[0])) || (!isnegative(tok[i]) && isnegative(tokens[0])))) {
                unimap = finalunify(tok[i], tokens[0]);
                if (unimap != null) {
                    v = i;
                    break;
                }
            }

        }

        if (unimap == null) {
            return null;
        } else {
            printMes("hello"+v);
            for (int z = 0; z < tok.length; z++) {
                p = predvar(tok[z]);
                if (z != v) {

                    if (isnegative(tok[z])) {
                        str += "~";

                    }
                    str += allname(tok[z]);
                    str += "(";
                    p = predvar(tok[z]);
                    for (int y = 0; y < p.length; y++) {
                        if (y != 0) {
                            str += ",";
                        }
                        if (unimap.containsKey(p[y])) {

                            str += unimap.get(p[y]);


                        } else {
                            str += p[y];
                        }
                    }
                    str += ")";
                    arr.add(str);
                    str = "";

                }


            }

            for (int u = 1; u < tokens.length; u++) {
                //q = predvar(tokens[u]);
                if (true) {

                    if (isnegative(tokens[u])) {
                        str1 += "~";

                    }
                    str1 += allname(tokens[u]);
                    str1 += "(";
                    q = predvar(tokens[u]);
                    for (int y = 0; y < q.length; y++) {
                        if (y != 0) {
                            str1 += ",";
                        }
                        if (unimap.containsKey(q[y])) {

                            str1 += unimap.get(q[y]);


                        } else {
                            str1 += q[y];
                        }
                    }
                    str1 += ")";
                    arr.add(str1);
                    str1 = "";
                }


            }
        }
        for (int x = 0; x < arr.size(); x++) {

            str2 += arr.get(x);
            if (x != arr.size() - 1) {
                str2 += "|";
            }


        }
        return str2;

    }


    public static void resolveQuer() {



        for (int i = 0; i < queries.length; i++) {
            String predicate = negated(queries[i]);
            //kb.add(predicate);
            printMes("Query: " + predicate);
            results.add(resolve(predicate, 0));
            //System.out.println("------------" + results.get(results.size()-1));
            //kb.remove(kb.size() - 1);
        }


    }

    private static String negated(String query) {
        if (query.toCharArray()[0] == '~') {
            return query.substring(1);
        } else {
            return "~" + query;
        }
    }


    public static boolean resolve(String query, int depth) {

        if (depth > 8) {
            return false;
        }

        if (kb.contains(negated(query))) {
            return true;
        }

        String firstPred = query.split("[|]")[0];
        Boolean suc = false;
        ArrayList<Integer> statements = null;
        if(!isnegative(firstPred)){
            for(int i=0;i<negpred.size();i++){
                if(getOnlyName(firstPred).equals(negpred.get(i).key)){
                    statements=negpred.get(i).val;
                }
            }
        }else{
            for(int i=0;i<pospred.size();i++){

                if(getOnlyName(firstPred).equals(pospred.get(i).key)){
                    statements=pospred.get(i).val;
                }
            }
        }

        if(statements==null){
            printMes("Statements null");
            return false;
        }

        printMes("statements.size:" + statements.size());
        for (int i = 0; i < statements.size(); i++) {
            printMes("Trying to unify: " + kb.get(statements.get(i)) + " :: " + query);
            String uni = unify(kb.get(statements.get(i)), query);
            if (uni == null) {
                continue;
            } else if (uni.equals("")) {
                //System.out.println("BLANK. Return true");
                suc = true;
                break;
            } else {
                if (resolve(uni, depth + 1)) {
                    //System.out.println(uni);
                    suc = true;
                    break;
                }
            }

        }


        return suc;
    }

    static void printMes(String mes) {
        //System.out.println(mes);
    }


    public static void main(String[] args) {


        // printMes("FOL INFERENCE ENGINE");
        File file = new File("input.txt");

        try {
            Scanner scan = new Scanner(file);
            String string = scan.nextLine();
            numberofQueries = Integer.parseInt(string);
            //printMes(numberofQueries);
            queries = new String[numberofQueries];
            for (int i = 0; i < numberofQueries; i++) {
                StringBuilder builder = new StringBuilder(scan.nextLine());
                for (int j = 0; j < builder.length(); j++) {
                    if (builder.charAt(j) == ' ' || builder.charAt(j) == '\t' || builder.charAt(j) == '\n') {
                        builder.replace(j, j + 1, "");
                        j--;
                    }
                }
                queries[i] = builder.toString();
            }
            numberofStatements = Integer.parseInt(scan.nextLine());
            statements = new String[numberofStatements];
            for (int i = 0; i < numberofStatements; i++) {
                StringBuilder builder = new StringBuilder(scan.nextLine());
                for (int j = 0; j < builder.length(); j++) {
                    if (builder.charAt(j) == ' ' || builder.charAt(j) == '\t' || builder.charAt(j) == '\n') {
                        builder.replace(j, j + 1, "");
                        j--;
                    }
                }
                statements[i] = builder.toString();
            }


        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        // printMes("Queries Number: " + numberofQueries);


        for (String i : queries) {
            //  printMes(i);
        }
        //printMes("Statement Number:" + numberofStatements);

        for (String i : statements) {
            //  printMes(i);

        }

        //finalunify("F(y,x)", "~F(S,SS)");
        //printMes("Unify-------: " + unify("~American(x0) | Criminal(x0)","~Criminal(West)"));


        for (int i = 0; i < numberofStatements; i++) {

            String[] temporary;
            String[] var1;
            String tempkb = "";
           /* for (int z=0;z<statements.length;z++) {
                printMes(statements[z]);
            }
*/
            temporary = statements[i].split("[|]");

            for (int z = 0; z < temporary.length; z++) {
                printMes(temporary[z]);
            }
            for (int j = 0; j < temporary.length; j++) {
                if (j != 0) {
                    tempkb += " | ";
                }

                // String t = temporary[j].split("[(]")[0];
                tempkb += temporary[j].split("[(]")[0] + "(";
                //printMes(t);
                //while(!isConstant(t))
                {
                    String var = (temporary[j].substring(temporary[j].indexOf("(") + 1));
                    var = var.substring(0, var.indexOf(")"));

                       /* if (isConstant(var)) {
                        printMes(var);
                        }*/
                    //printMes(var);

                    var1 = var.split("[,]");

                    printMes(var);

                    for (int p = 0; p < var1.length; p++) {
                        if (p != 0) {
                            tempkb += ",";
                        }

                        tempkb += var1[p];
                        if (!Character.isUpperCase(var1[p].toCharArray()[0])) {
                            tempkb += String.valueOf(i);
                        }


                    }
                    tempkb += ")";


                }
                //kb.add(statements[i]);


            }

            kb.add(tempkb);
            // printMes(allname("Fukrey(x,y)"));
            //printMes(isnegative("~Fukrey(x,y)"));
            //String[] s=predvar("~Fukrey(John,c,y)");
            /*for (String g:s) {
            printMes(g);
            }*/



        /*for(int q=0;q<kb.size();q++) {
            printMes(kb.get(q));

        }*/





        /*for (String s: statements)
        {
            String key = "";
            predicates value;
            key = PredName(s);

            if(KB.containsKey(key)) {
                value = KB.get(key);
            }
            else {
                value = new predicates();
            }

            switch(predicatetype(s))
            {
                //negative fact
                case 1:	value.negpred.add(s);

                        break;
                //positive fact
                case 2: value.pospred.add(s);
                        break;
                //normal predicate
                case 3: value.pred.add(s);
                        break;
                default: printMes("Error");
                        break;
            }
            KB.put(key, value);

            printMes(KB);*/

        }

        add(statements);
        printMes("-----Shaz----");

        for(String i : statements){
            printMes(i);

        }


        printMes("-----Shaz----");
        resolveQuer();

        try {
            PrintWriter prin = new PrintWriter("output.txt");
            for(int i=0; i<results.size(); i++){
                if (results.get(i)) {
                    System.out.println("TRUE");
                    prin.println("TRUE");
                } else {
                    System.out.println("FALSE");

                    prin.println("FALSE");
                }
            }
            prin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}













