//***********************************************************************
// FILE NAME    : Intcoll6.java
// DESCRIPTION  : This file contains the class Intcoll6.
//************************************************************************
import java.util.*;
import java.io.*;

public class Intcoll6 {

    boolean DEBUG_MODE = true;
    String DEBUG_TEXT = "";

    private int howmany;
    private btNode c;

    /*//////////////////////////////
     Constructors 
     *///////////////////////////////
    public Intcoll6() {
        create();
    }

    public Intcoll6(int i) {
        create();
    }

    private void create() {
        //We don't care about setting a size for linked lists;
        //however, to prevents errors a constructor exists for it.
        //To stop code rendancy and anomilies, create() exists.
        c = null;
        howmany = 0;
    }

    ////////////////////////////////
    private static btNode copytree(btNode t) {
        btNode root = null;
        if (t != null) {
            root = new btNode();
            root.info = t.info;
            root.left = copytree(t.left);
            root.right = copytree(t.right);
        }
        return root;
    }
    //The object executing is copied over by the parameter
    public void copy(Intcoll6 obj) {
        //do these share the same head pointer, if so they are already
        //equal thus copying is pointless
        if (this != obj) {
            howmany = obj.howmany; //set the amount to be equal
            c = copytree(obj.c);
            //the actual meat of the copy method is in btnode.
            //btnode has special permissions we can't use here so we do
            //it there.
        }
    }
    
    public void insert(int i) {
        ////////////////////////////////////////////////
        // Copypasta
        ////////////////////////////////////////////////
        btNode pred = null, p = c;
        while ((p != null) && (p.info != i)) {
            pred = p;
            if (p.info > i) 
                p = p.left;
            else
                p = p.right;
        }
        ////////////////////////////////////////////////

        if (p == null) {
            howmany++;
            p = new btNode(i, null, null);
            if (pred != null) {
                if (pred.info > i)
                    pred.left = p;
                else pred.right = p;
            } else c = p;
        }
    }
    
    public void omit(int i) {
         btNode largest = null;
         String text = null; //for debug message
        
        btNode pred = null, p = c; //find i
        while ((p != null) && (p.info != i)) {
            pred = p;
            if (p.info > i) 
                p = p.left;
            else
                p = p.right;
        }
        
        if (p != null) { //if p is not null, we found the integer to omit
            howmany--;
            
            if (pred == null && c.left == null && c.right == null) {
                    text = "head node: only head";
                    // Case test 1, -1             PASS
                    // 1
                    c = null;
            } else if (p.left == null && p.right == null) {
                text = "leaf node";
                /*
                 Case test 3, 2, 4, -2      PASS
                   3
                  / \
                 2   4  
                 */
                text = "leaf node";
                //if p.info is greater than pred.info then leaf node p
                //is stored to the right. Therefore we have to set pred.right to
                //null to remove it. 
                if (p.info > pred.info) 
                    pred.right = null;
                else if (p.info < pred.info) 
                    pred.left = null;
                else 
                    DEBUG_TEXT = "error handler in the"
                            + "case pred.info == p.info";
            } else if (p.left != null && p.right == null) {
                text = "node with right only";
                /*
                 Case test 1, 7, 5, -7       PASS
                  1
                   \
                    7
                   /
                  5  
                
                 Case test 3, 2, 1, -2       PASS
                      3
                     / 
                    2  
                   /  
                  1     
                */

                btNode temp = p.left;
                largest = p.left;

                while (temp != null) {
                    if (temp.info < largest.info) 
                        largest = temp;
                    temp = temp.right;
                }
            } else if (p.left == null && p.right != null) {
                text = "node with left only";
                /*
                Case test 1, 2, 3, -2       PASS
                     1
                      \
                       2
                        \
                         3
                Case test 1, 2, 3, -1       PASS
                     1
                      \
                       2
                        \
                         3
                
                
                 Case test 3, 1, 2, -1       PASS
                         3
                        / 
                       1    
                        \  
                         2
                Case test 3, 1, 2, -3        PASS
                         3
                        / 
                       1    
                        \  
                         2
                 */

                btNode temp = p.right;
                largest = p.right;

                while (temp != null) {
                    if (temp.info > largest.info) {
                        largest = temp;
                    }
                    temp = temp.right;
                }
            } else if (pred != null && p.left != null && p.right != null) {
            text = "node with 2 children";
                
            /*
            200
            400
            450
            300
            250
            350
            325
            370
            100
            366
            -400
                        200                  error
                       /  \
                    100    400
                           /  \ 
                         300  450     
                        /   \
                     250   350
                           / \
                         325  370
                              /
                             366
        */
                //for this assignment find largest on the left 
                btNode temp = p.left; 
                largest = p.left;

                while (temp != null) {
                    if (temp.info < largest.info) 
                        largest = temp;
                    temp = temp.right;
                }
        ///////////////////////////////
        //Debug stuff
        ///////////////////////////////
            } else 
                DEBUG_TEXT = "NO CONDITION FOR SCENARIO";
            if (!DEBUG_TEXT.equals("")) {
                if (DEBUG_MODE == true) 
                    System.out.println("Error encountered.");
                System.out.println("message: " + DEBUG_TEXT);
                System.exit(0);
            }
        } else if (DEBUG_MODE) {
            System.out.println("Specified number to remove doesn't exist. "
                    + "Howmany is " + get_howmany());
        }
        ///////////////////////////////
        //Debug stuff END
        ///////////////////////////////
        
        //This code does the omission and reinsertion
        
        if (pred != null) {     //check if not the root
            if (pred.left == p)         
                pred.left = largest;        
            else if (pred.right == p) 
                pred.right = largest;
            else
                text = "Error encountered 3.";
        }   else if (c != null) //a check to avoid a crash
                c = c.right;
        
        if (DEBUG_MODE == true && text != null)
            System.out.println("Remove option selected: " + text);  //debug
    }
    
    public boolean belongs(int i) {
        //Get used to this code because you'll be seeing this a lot     
        //Basically this code finds if an integer belongs or not.
        //if p is null, the integer doesn't exist, if it isn't null it does.   
        //This code is copy and pasted a couple of times because readibility
        btNode p = c;
        while ((p != null) && (p.info != i)) {
            if (p.info > i)
                p = p.left;
            else
                p = p.right;
        }
        return (p != null);
    }

    public int get_howmany() {
        return howmany;
    }

    public void print() {
        //btnode has access we don't have in intcoll6 so we call a method
        //to print.
        printtree(c);
        System.out.println("Howmany: " + get_howmany());
    }

    public boolean equals(Intcoll6 obj) {
        int j = 0;
        boolean result = (howmany == obj.howmany);
        if (result) {
            btNode me = c;
            btNode boku = obj.c;

            int[] meArray   = new int[get_howmany() + 1];
            int[] objArray  = new int[get_howmany() + 1];
            
            toarray(me, meArray, 0);
            toarray(boku, objArray, 0);
            
        while((j < get_howmany() - 1) && (meArray[j] != objArray[j]) && result){
            if (meArray[j] != objArray[j])
                result = false;
            else
                j++;
            } 
        }
        return result;
    }

    private static void printtree(btNode t) {
        /*
        A recursive method and this requires a bit of explanation.
        
        First we check if the parameter pointer isn't null (basically
        if it's an actual node on the tree).
        
        If node does exist, we print the contents of the node and
        check its left and right branch. We check if those nodes exist and if
        they need printing. This cycle continues until we reach a node with
        null.
        */

        if (t != null) {
            printtree(t.left);
            System.out.println(t.info);
            printtree(t.right);
        }
    }

    /*
    Not sure if we even need this method buth you gave it to us.
    
    This method put all the binary tree elements into an array
    */
    private static int toarray(btNode t, int[] a, int i) {
        int num_nodes = 0;
        if (t != null) {
            num_nodes = toarray(t.left, a, i);
            a[num_nodes + i] = t.info;
            num_nodes = num_nodes + 1 + toarray(t.right, a, num_nodes + i + 1);
        }
        return num_nodes;
    }

    private static class btNode {

        int info;
        btNode left;
        btNode right;

        private btNode(int s, btNode lt, btNode rt) {
            create(s, lt, rt);
        }

        private btNode() {
            create(0, null, null);
        }

        private void create(int s, btNode lt, btNode rt) {
            info = s;
            left = lt;
            right = rt;
        }
    }
}
