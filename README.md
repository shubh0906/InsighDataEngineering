# Table of Contents
1. [Problem](README.md#problem)
2. [Approach](README.md#approach)
3. [Run](README.md#run)

# Problem
The problem presented here is to analyze past year immigration data trends on H1B and give mechanism to calculate **Top 10 Occupations** and **Top 10 States** for **certified** visa applications.


# Approach
* __`ASSUMPTIONS`__
    * The name of the input file will always be **h1_input.csv** and always a semicolon separated (";") format.
    * The columns in the input have been considered from **File Structure** for 2009-2017, any different **File Structure** might break the code.

First read the input file line by line and for all those applications whose case status is `Certified`, we save their state and occupation count in a HashMap. The time complexity for this whole operation is **O(n)**, where n is the number of rows in input. The space complexity will be **O(n)** for storing information in HashMap.

Now when we have count of each state and occupation, we can a take a two Priority Queue(min-heap) for state and occupation both of size 10. While traversing the HashMap,if the element is greater than the smallest element in the Priority Queue then smallest element is replaced by this element. Removal of smallest element from Priority Queue is **O(1)** and insertion of element is **O(log 10)** since the size of Priority Queue is 10 it can be considered **O(1)**. Whole time complexity of this operation over the HashMap will be **O(n)** with space complexity of **O(1)**, since size of Priority Queue is constant.


Now Priority Queue consists of top 10 elements sorted by __`NUMBER_CERTIFIED_APPLICATIONS`__, and in case of a tie, alphabetically by 
__`TOP_OCCUPATIONS`__/__`TOP_STATES`__ but in reverse order. So we reverse the Priority Queue and store the results in Output file.

Overall, run time complexity will be **O(n)** and space complexity **O(n)**.

# Run

To run the this code just make sure you are using Java 1.8.0_181 or above. Use `run.sh` script to generate Output files.
