/*

 Notes for Project:




        Speed of adding a key or looking up a key in an array is as fast as possible?:

            Sorted array: gibes log2N performance on add, remove, and contains
                    Disadvantages: lots of removes and adds continually

            Hashing algorithm: String converting to number
                 Use ASCII table and assign each char to the associated numbers from table value

                "13876" -> 13,876
                "a                  b                           m                         d   "
                1*26^3       +     2*26^2           +       13*26^1           +        4*26^0

                    Weakness: big string will produce bigger and bigger numbers
                        To fix, use modulus for string number/index based from wanted table size
                                Problem: 2 strings can have same values -> called collision

                        To fix problem of collsion:
                                Open addressing:
                                      - if same/taken, move linearlly until next free spot
                                      - if search, first hash and if there is another string sitting at spot,
                                                        - move until you find the desired key
                                      - after hashing, you find the spot to be blank, that means key is not in storage

                                Closed addressing:

                                    - can store multiple values in same spot (still using modules)
                                                -> through using linked lists and having head pointer

                    Hashing function is O(1) with respect to number of elements to table -> putting value in index value
                    However, hashing function is O(N) with respect of String -> longer string takes longer to hash

                    As you hash more and more keys, lengths of lists will be closer to each other



import java.io.*;
import java.util.*;


        Collisions: when strings hash to same number:

                Linear probing:
                    if spot taken, walk linearally across array until you find blank, then put it in -> open addressing
                    A   "Blank"  H   U   P
                    If "Z" hashed to where U was, move linearally and add Z to next open spot, wrap around if you went to end of array

                    As the table gets bigger and spaces are uneven, the time gets O(N) , called clustering
                    - Very prone to clustering


                Closed Addressing:

                        - Array of LinkedLists
                        - If element collides, walk the list and see if the same key landed in same bucket, if key already there, don't add, if different, add to end
                        O(N) operation

                 - Quality of hash function of how evenly spread is the most important idea of hashing

                 - when rehashing, do insertATFront when copying values over
                 - rehashing is faster than hashing
                 - remove, walk list and remove (like linkedlist with nodes)

 */



import java.io.*;
import java.util.*;

public class MyHashSet implements HS_Interface
{	private int numBuckets; // changes over life of the hashset due to resizing the array
    private Node[] bucketArray;
    private int size; // total # keys stored in set right now

    // THIS IS A TYPICAL AVERAGE BUCKET SIZE. IF YOU GET A LOT BIGGER THEN YOU ARE MOVING AWAY FROM (1)
    private final int MAX_ACCEPTABLE_AVE_BUCKET_SIZE = 1;  // MAY CHOSE ANOTHER NUMBER

    public MyHashSet( int numBuckets )
    {	size=0;
        this.numBuckets = numBuckets;
        bucketArray = new Node[numBuckets]; // array of linked lists
        System.out.format("IN CONSTRUCTOR: INITIAL TABLE LENGTH=%d RESIZE WILL OCCUR EVERY TIME AVE BUCKET LENGTH EXCEEDS %d\n", numBuckets, MAX_ACCEPTABLE_AVE_BUCKET_SIZE );
    }

    private int hashOf( String key, int numBuckets ) //  number returned -MUST- MUST BE IN [0..numBuckets-1]
    {


//		write a loop that uses looks at ALL the chars in the string
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = 3 * hash + key.charAt(i);
        }
        return Math.abs((int) hash % numBuckets);
    }


    public boolean add( String key )
    {
        // your code here to add the key to the table and ++ your size variable

        int hashNum = hashOf(key, numBuckets);

        if(bucketArray[hashNum] == null)
        {
            bucketArray[hashNum] = new Node(key, null);
            return true;
        }

        Node curr = bucketArray[hashNum]; //like the head
        //System.out.println(" curr.data: " + curr.data);

        while (curr.next != null)
        {
            if (curr.data.equals(key))
                return false;
            else
                curr = curr.next;
        }

        if (curr.data.equals(key)) return false;
        curr.next = new Node(key, null);

        ++size; // you just added a key to one of the lists
        if ( size > MAX_ACCEPTABLE_AVE_BUCKET_SIZE * this.numBuckets)
            upSize_ReHash_AllKeys(); // DOUBLE THE ARRAY .length THEN REHASH ALL KEYS
        return true;
    }

    public boolean contains( String key )
    {
        if(isEmpty())
        {
            return false;
        }
        int index = hashOf(key, numBuckets);
        Node curr = bucketArray[index];
        while(curr!= null)
        {
            if(curr.data.equals(key))
            {
                return true;
            }
            curr = curr.next;
        }

        return false;
        // You hash this key. goto that list. look for this key in that list
    }


    private void upSize_ReHash_AllKeys()
    {
        System.out.format("KEYS HASHED=%10d UPSIZING TABLE FROM %8d to %8d REHASHING ALL KEYS\n",
            size, bucketArray.length, bucketArray.length*2  );
        Node[] biggerArray = new Node[ bucketArray.length * 2 ];
        this.numBuckets = biggerArray.length;

        /*
        FOR EACH LIST IN THE ARRAY
        FOR EACH NODE IN THAT LIST
        HASH THAT KEY INTO THE BIGGER TABLE
        BE SURE TO USE THE BIGGER .LENGTH AS THE MODULUS

         */
        for(Node head: bucketArray)

        {
            if (head==null) continue; // goes to the next cell in the buck array

            Node curr = head;

            while (curr != null )
            {
                String data = curr.data;
                int index = hashOf(data, biggerArray.length);
                biggerArray[index] = new Node( data, biggerArray[index] );
                curr = curr.next;
            }

        }
        bucketArray = biggerArray;
    } // END OF UPSIZE & REHASH

    public boolean remove( String key ) // if not found return false else remove & return true

    {
        int hashNum = hashOf(key, numBuckets);

        if (bucketArray[hashNum] == null) return false;
        if (bucketArray[hashNum].data.equals(key)) {
            bucketArray[hashNum] = bucketArray[hashNum].next;
            return true;
        }

        Node curr = bucketArray[hashNum];  // key is E
        // [*] --> A->  B->  C->  D->  E
        while(curr.next != null && !curr.next.data.equals(key) )

            curr = curr.next;

        if (curr.next == null)  // cur reached tail
            return false;

        curr.next = curr.next.next;
        return true;

    }

    public int size() // number of keys currently stored in the container
    {
        return size;
    }

    public boolean isEmpty() // use the call to size
    {
        return size() == 0;

    }
    public void clear()
    {
        size = 0;
    }



} //END MyHashSet CLASS

class Node
{	String data;
    Node next;
    public Node ( String data, Node next )
    { 	this.data = data;
        this.next = next;
    }
}



