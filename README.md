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

First read the input file line by line and for all those applications whose case status is `Certified`, we save their state and occupation count in a HashMap. The time complexity for the whole operation is **O(mn)**, where n is the number of rows in input and m is the length of the each row. The space complexity will be **O(n)** for storing information in HashMap.

Now when we have count of each state and occupation, we can a take a two Priority Queue(min-heap) for state and occupation both of size 10. While traversing the HashMap we put the entries in Prioirity Queue if that Node is greater than the smallest Node in the Priority Queue is replaced by this node. Removal of element from Priority Queue is **O(1)** and insertion of element is **O(log 10)** since the size of Priority Queue is 10 it can be considered **O(1)**.

Now Priority Queue consists of top 10 elements sorted by __`NUMBER_CERTIFIED_APPLICATIONS`__, and in case of a tie, alphabetically by __`TOP_OCCUPATIONS`__/__`TOP_STATES`__.
