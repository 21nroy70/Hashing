# Hashing - involving Linked Lists and Nodes
I hashed a 50 million word file using an algorithm that gives O(1) (constant) time to search, add, and rehash. <br> <br>
To achieve this efficent time, I converted each of the strings from the incoming text file into unique numbers through using Horner's method. These unique numbers would be the index for which the incoming string would be assigned to in an array filled with Linked Lists. <br> <br>
Link Lists contain a data value and a pointer to the Next Node. in order to add more efficently, I would do an Insert at head function, meaning that the incoming string would be the head (first) pointer, pointing to the rest of the linked list. <br> <br>
To avoid large sizes of the array, I used modulus based on the given array size, to reduce slower performance and unnecessary arrays of big sizes, filled with big gaps. <br> <br>
