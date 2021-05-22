import java.util.ArrayList; 
import java.util.LinkedList;

/**
 * 
 * Kyle Kovacik
 * Implementation of a HashTable using an ArrayList of LinkedLists (Chaining)
 * 
 */


class Pair<K,V> 
{
    /*
    The Pair class is intended to store key, value pairs. It'll be helpful
    for part 1.2 of the assignment.
    */
    public K key;
    public V value;

    public Pair(K key, V value) 
   {
        this.key = key;
        this.value = value;
   }
}

/**************PART 1.2.1*******************/

public class HashTableChaining<K,V> 
{
   /*
    Write your code for the hashtable with chaining here. You are allowed
    to use arraylists and linked lists.
    */
   private int numberOfKeys = 0;
   private int capacity = 0;
   private ArrayList<LinkedList<Pair>> list;
       
    public HashTableChaining(int capacity)
    {
        /*
        Initialize your hashtable with capacity equal to the input capacity.
        */
        this.capacity = capacity;
        list = new ArrayList<>(capacity);

        for (int i = 0; i < capacity; i++) {
            LinkedList<Pair> ll = new LinkedList<Pair>();
            list.add(ll);
        }
    }

    public void insert(String key, int val)
    {
        /*
        Insert the key into the hashtable if it is not already in the hashtable.
        */
        if (contains(key)) {
            return;
        }
        int hashvalue = hash(key);

        list.get(hashvalue).add(new Pair<>(key, val));
        numberOfKeys++;

        if (((double) size() / (double) capacity) >= 0.75) {
                rehash();
        }
    }

    public void remove(String key)
    {
        /*
        Remove key from the hashtable if it is present in the hashtable.
        */
        if (!contains(key)){ 
            return;
        }
        int hashvalue = hash(key);

        for (int i = 0; i < list.get(hashvalue).size(); i++) {
            if (list.get(hashvalue).get(i).key.equals(key)) {
                list.get(hashvalue).remove(i);
                numberOfKeys--;
                return;
            }
        }
    }

    public boolean contains(String key)
    {
        /* 
        Search the hashtable for key, if found return true else
        return false
        */
        int hashvalue = hash(key);
        for (int i = 0; i < list.get(hashvalue).size(); i++) {
            if (list.get(hashvalue).get(i).key.equals(key)) {
                return true;
            }
        }
        return false;
    }
    
    public int size()
    {
        /*
        return the total number of keys in the hashtable.
        */
        return numberOfKeys;
    }
    
    public int hash(String key)
    {
        /*
        Use Horner's rule to compute the hashval and return it.
        */

        int hashcode = 0;
        for (int i = 0; i < key.length(); i++) {
            hashcode = ((37 * hashcode) + key.charAt(i)) % this.capacity;
        }

        int hashvalue = hashcode % this.capacity; //Is this step necessary????
        return hashvalue;
    }

    public int getVal(String key)
    {
        /*
        return the value corresponding to the key in the hashtable.
        */
        int hashvalue = hash(key);
        for (int i = 0; i < list.get(hashvalue).size(); i++) {
            if (list.get(hashvalue).get(i).key.equals(key)) {
                return (int) list.get(hashvalue).get(i).value; //check this later
            }
        }

        return 0;
    }

    
    public void rehash()
    {
        /*
        Resize the hashtable such that the new size is the first prime number
        greater than two times the current size.
        For example, if current size is 5, then the new size would be 11.
        */
        int newSize = this.capacity * 2;

        boolean isPrime = false;
        while (!isPrime) {
            isPrime = true;
            for (int i = 2; i < newSize; i++) {
                if ((newSize % i) == 0) {
                    newSize++;
                    isPrime = false;
                    break;
                }
            }
        }
        this.capacity = newSize;

        ArrayList<LinkedList<Pair>> newList = new ArrayList<LinkedList<Pair>>(); // Might need to put in the new Size????

        for (int i = 0; i < capacity; i++) {
            LinkedList<Pair> ll = new LinkedList<Pair>();
            newList.add(ll);
        }

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                int hashvalue = hash((String) list.get(i).get(j).key);
                newList.get(hashvalue).add(new Pair<>((String) list.get(i).get(j).key, (int) list.get(i).get(j).value));
            }
        }
        list = newList;
    }

    public static void main(String[] args) {
        HashTableChaining<String, Integer> hashTable = new HashTableChaining<>(1);
        String[] in = {"Hi", "Hi", "Hi", "Two", "Two", "Yes", "Maybe", "Idk", "Three", "Three", "Three", "Spaghetti", "Spaghetti"};
        System.out.println(in.length);

        String[] out = new String[5];
        out = hashTable.mostFrequentStrings(in);
        for (int i = 0; i < out.length; i++) {
            System.out.println(out[i]);
        }
    }

    /**************PART 1.2*******************/

    public String[] mostFrequentStrings(String[] in)
    {
        /*
        Given an array of strings, print the five most
        frequent strings. You must use your implementation
        for hashtable with seperate chaining for this.
        */
        HashTableChaining<String, Integer> hashTable = new HashTableChaining<>(in.length);
        for (int i = 0; i < in.length; i++) {
            int count = 0;

            if (hashTable.contains(in[i])) {
                count = hashTable.getVal(in[i]);
            }
            hashTable.remove(in[i]);
            count++;
            hashTable.insert(in[i], count);
        }

        String[] mostFrequent = new String[5];

        for (int j = 0; j < 5; j++) {
            int max = 0;
            String maxKey = "";
            for (int i = 0; i < in.length; i++) {
                if (hashTable.getVal(in[i]) > max) {
                    max = hashTable.getVal(in[i]);
                    maxKey = in[i];
                }
            }
            mostFrequent[j] = maxKey;
            hashTable.remove(maxKey);
        }

        return mostFrequent;
    }
    
}
