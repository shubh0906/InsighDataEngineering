import java.io.*;
import java.util.*;

class solution{

    //Custom Node to store count and state/occupation
    static class Node implements Comparable<Node>{
        int count;                      //maintaining count
        String str;                     // state/occupation
        static int totalCertified;      //total applications that were Certified
        Node(String s){
            this.count = 1;
            this.str = s;
        }
        //increment count of state/occupation
        public void inc(){
            this.count++;
        }
        /*@override toString to print Node object content in custom format*/
        public String toString(){
            float percent = (float)count/totalCertified;
            String percentage = String.format("%.1f",percent*100) ;
            return String.valueOf(str+";"+count+";"+percentage+"%");
        }

        /* @Override compareTO to compare two Node object*/
        public int compareTo(Node n){
            int ret = n.count-this.count;
            if(ret==0)
                return this.str.compareTo(n.str);
            return ret;
        }
    }
    /*Custom Comparator to sort Node in Priority Queue first ascending order on the basis of count
    then if same then desc order on the basis of name of state/occupation */
    static class NodeComprator implements Comparator<Node>{
        public int compare(Node n1,Node n2){
            int ret = n1.count-n2.count;
            if(ret==0)
                return n2.str.compareTo(n1.str);
            return ret;
        }
    }
    /* Print the content of Priority Queue in file in sorted order */
    public static void printInFile(PriorityQueue<Node> pq, String fileName,String firstLine )throws Exception{
        Node[] arr = new Node[10];
        int i=0;
        // reversin g the order of Priority Queue for occupations
        while(pq.size()>0){
            arr[i++]=pq.poll();
        }
        i--;
        // writing in the occupations output file
        PrintWriter pw = new PrintWriter(new FileWriter(fileName));
        pw.println(firstLine);
        for(;i>=0;i--)
            pw.println(arr[i]);
        
        pw.close();
    }
    /* get elements from the HashMap and populate the top elements in Priority Queue*/
    public static void getTopElements(Map<String,Node> map,PriorityQueue<Node> pq, int size){
        for(Map.Entry<String,Node> e : map.entrySet()){
            if(pq.size()==size){
                Node temp = pq.peek();
                int comp = temp.compareTo(e.getValue()); 
                if(comp>0){
                    pq.poll();
                    pq.add(e.getValue());
                }
            }
            else{
                pq.add(e.getValue());
            }
            
        }
    }
    public static void main(String[] ss) throws Exception{
        long startTime   = System.nanoTime();
        
        String fileName = "../input/h1b_input.csv";
        if(ss.length>0)
            fileName = ss[0];
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();
        String[] columns = line.split(";");
        
        int occuptionIndex = 0,stateIndex = 0,certificateIndex = 0;
        
        // getting the column indexes for the information we need
        for(int i=0; i<columns.length;i++){
            String column = columns[i].replaceAll("^\"|\"$", "");
            if(column.equalsIgnoreCase("SOC_NAME")|| column.equalsIgnoreCase("LCA_CASE_SOC_NAME")|| column.equalsIgnoreCase("OCCUPATIONAL_TITLE"))
                occuptionIndex = i;
            else if(column.equalsIgnoreCase("CASE_STATUS")|| column.equalsIgnoreCase("STATUS")|| column.equalsIgnoreCase("APPROVAL_STATUS"))
                certificateIndex = i;
            else if(column.equalsIgnoreCase("WORKSITE_STATE")|| column.equalsIgnoreCase("LCA_CASE_WORKLOC1_STATE")|| column.equalsIgnoreCase("STATE_1"))
                stateIndex = i;
        }
        
        Map<String,Node> stateCount = new HashMap<>();
        Map<String,Node> occupationCount = new HashMap<>();
        
        //readin the csv file and retrieving and storing relevant info in two HashMap 
        while((line = br.readLine())!=null){
            String[] data = line.split(";");
            if(data[certificateIndex].replaceAll("^\"|\"$", "").equalsIgnoreCase("CERTIFIED")){
                Node.totalCertified++;

                String state = data[stateIndex].replaceAll("^\"|\"$", "");
                if(stateCount.containsKey(state))
                    stateCount.get(state).inc();
                else
                    stateCount.put(state,new Node(state));

                String occupation = data[occuptionIndex].replaceAll("^\"|\"$", "");
                if(occupationCount.containsKey(occupation))
                    occupationCount.get(occupation).inc();
                else
                    occupationCount.put(occupation,new Node(occupation));
            }
        }

        //Traversing the occupationCount HashMap and inserting and maintaining the size of Priority Queue with top 10 Node in asc order.
        PriorityQueue<Node> occupationPQ = new PriorityQueue<Node>(10, new NodeComprator()); 
        getTopElements(occupationCount, occupationPQ, 10);
        
        //Traversing the stateCount HashMap and inserting and maintaining the size of Priority Queue with top 10 Node in asc order.
        PriorityQueue<Node> statePQ = new PriorityQueue<Node>(10, new NodeComprator()); 
        getTopElements(stateCount, statePQ, 10);

        //printing the content in output file
        printInFile(occupationPQ,"../output/top_10_occupations.txt","TOP_OCCUPATIONS;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE");
        printInFile(statePQ,"../output/top_10_states.txt","TOP_STATES;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE");

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime/1000000+" ms");
    }
}