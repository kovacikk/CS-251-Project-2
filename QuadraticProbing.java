/* Quadratic Probing */
	public class QuadraticProbing<AnyType>
	{
		private static final int DEFAULT_TABLE_SIZE = 13;
		private HashEntry [ ] array; // The array of elements
		 int size;
		 int elementsSize = 0;
	
	public static class HashEntry<AnyType>
	{
		/* Initialize the entries here. You can write a constructor for the same */
		public AnyType  element; 
		public boolean isActive;  // For Lazy deletion
		public String toString()
		{
			if(this.element!=null)
				return (String) element;
			else
				return "NULL";
		}

		public HashEntry(AnyType x, boolean isActive) {
			this.element = x;
			this.isActive = isActive;
		}
	}


/* Construct the hash table */
	public QuadraticProbing( )
	{
		this.size = DEFAULT_TABLE_SIZE;
		array = new HashEntry[this.size];
	}

/* Construct the hash table */

	public QuadraticProbing( int size )
	{
		/* allocate memory to hash table */
		this.size = size;
		array = new HashEntry[this.size];
	}


/* Return true if currentPos exists and is active - Lazy Deletion*/
	public boolean isActive(int position)
	{
		return array[position].isActive;
	}
	

/* Find an item in the hash table. */
	public boolean contains( AnyType x )
	{
		/* Should return the active status of key in hash table */
		int inactivePos = 0;
		boolean inactiveBool = false;

		int hashVal = hash(x.toString(), size);
		int result = hashVal;
		int i = 1;

		while (array[result] != null) {
			if (array[result].isActive && array[result].element.equals(x)) {
				if (inactiveBool) {
					HashEntry temp = array[inactivePos];
					array[inactivePos] = array[result];
					array[result] = temp;
				}
				return true;
			}else if (!array[result].isActive){
				inactivePos = result;
				inactiveBool = true;
				result = (hashVal + (int) Math.pow(i,2)) % this.size;
			}
			else {
				result = (hashVal + (int) Math.pow(i,2)) % this.size;
			}
			i++;
		}
		return false;
	}


/* Insert into the Hash Table */
	
	public void insert( AnyType x )
	{

		/* Insert an element */
		int hashVal = hash(x.toString(), this.size);

		int result = hashVal;

		for (int i = 1; array[result] != null && isActive(result); i++) {
			result = (hashVal + (int) Math.pow(i, 2)) % this.size;
			System.out.println(result);
		}

		array[result] = new HashEntry<>(x, true);
		elementsSize++;
		//System.out.println("Inserted " + x.toString() + "to index " + result);

		if (((double) elementsSize / (double) this.size) >= 0.4) {
			//System.out.println("Re-Sizing!!!!!!!!");
			rehash();
			printTest(); // Remember to take this out
		}
	}


/* Remove from the hash table. */
	
	public void remove( AnyType x )	
	{
		/* Lazy Deletion*/
		int hashVal = hash(x.toString(), size);
		int result = hashVal;
		int i = 1;
		while (array[result] != null) {
			if (array[result].element.equals(x)) {
				array[result].isActive = false;
				return;
			}else {
				result = (hashVal + (int) Math.pow(i,2)) % this.size;
			}
			i++;
			//System.out.println("Deleted " + x.toString() + "from index " + result);
		}
		elementsSize--;
   	}

   
/* Rehashing for quadratic probing hash table */
	private void rehash( )
	{
		int newSize = size * 2;
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


		HashEntry [ ] newList = new HashEntry[newSize];

		for (int i = 0; i < size; i++) {
			if (array[i] != null && array[i].isActive) {
				int hashVal = hash(array[i].element.toString(), newSize);

				int result = hashVal;
				for (int j = 1; newList[result] != null; j++) {
					result = (hashVal + (int) Math.pow(j, 2)) % newSize;
				}
				newList[result] =  new HashEntry<>(array[i].element, true);
			}
		}
		array = newList;
		size = newSize;
	}
	

/* Hash Function */
	public int hash( String key, int tableSize )
	{
		/**  Make sure to type cast "AnyType"  to string 
		before calling this method - ex: if "x" is of "AnyType", 
		you should invoke this function as hash((x.toString()), tableSize) */

		int hashVal = 0;
		for (int i = 0; i < key.length(); i++) {
			hashVal = ((37 * hashVal) + key.charAt(i)) % tableSize;
		}
		hashVal = hashVal % tableSize;
		/* Compute the hash code*/
		return hashVal;
	}

	public int probe(AnyType x) // check
	{
		/* Return the number of probes encountered for a key */
		/** int hashVal = hash(x.toString(), size);

		int result = hashVal;
		for (num_of_probes = 1; array[result] == null; num_of_probes++) {
			result = (hashVal + (int) Math.pow(num_of_probes, 2)) % this.size;
		}
		*/

		int hashVal = hash(x.toString(), size);
		int result = hashVal;
		int i = 0;
		while (array[result] != null) {
			if (array[result].isActive && array[result].element.equals(x)) {
				return i;
			}else {
				i++;
				result = (hashVal + (int) Math.pow(i,2)) % this.size;
			}
		}



		return 0;
	}

	public void printTest() {
		System.out.println("--------------PRINTING-------------");

		for (int i = 0; i < size; i++) {
			System.out.println(i + ": " + array[i]);
			if (array[i] != null) {
				System.out.printf("isActive: " + array[i].isActive + "\n");
			}
		}
		System.out.println("--------------PRINTING-------------");

	}
}

