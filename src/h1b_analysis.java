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
                return n.str.compareTo(this.str);
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
            if(columns[i].equalsIgnoreCase("SOC_NAME")|| columns[i].equalsIgnoreCase("LCA_CASE_SOC_NAME")|| columns[i].equalsIgnoreCase("OCCUPATIONAL_TITLE"))
                occuptionIndex = i;
            else if(columns[i].equalsIgnoreCase("CASE_STATUS")|| columns[i].equalsIgnoreCase("STATUS")|| columns[i].equalsIgnoreCase("APPROVAL_STATUS"))
                certificateIndex = i;
            else if(columns[i].equalsIgnoreCase("WORKSITE_STATE")|| columns[i].equalsIgnoreCase("LCA_CASE_WORKLOC1_STATE")|| columns[i].equalsIgnoreCase("STAEE_1"))
                stateIndex = i;
        }
        
        Map<String,Node> stateCount = new HashMap<>();
        Map<String,Node> occupationCount = new HashMap<>();
        
        //readin the csv file and retrieving and storing relevant info in two HashMap 
        while((line = br.readLine())!=null){
            String[] data = line.split(";");
            if(data[certificateIndex].equalsIgnoreCase("CERTIFIED")){
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

        //Traversing the occupationCount HashMap and inserting and maintaining the size of Priority Queue 
        PriorityQueue<Node> pq = new PriorityQueue<Node>(10, new NodeComprator()); 
        for(Map.Entry<String,Node> e : occupationCount.entrySet()){
            if(pq.size()==10){
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
        
        //Traversing the stateCount HashMap and inserting and maintaining the size of Priority Queue 
        PriorityQueue<Node> pq2 = new PriorityQueue<Node>(10, new NodeComprator()); 
        for(Map.Entry<String,Node> e : stateCount.entrySet()){
            if(pq2.size()==10){
                Node temp = pq2.peek();
                int comp = temp.compareTo(e.getValue()); 
               if(comp>0){
                    pq2.poll();
                    pq2.add(e.getValue());
                }
            }
            else{
                pq2.add(e.getValue());
            }
        }  
        Node[] arr = new Node[10];
        int i=0;

        // reversin g the order of Priority Queue for occupations
        while(pq.size()>0){
            arr[i++]=pq.poll();
        }
        i--;
        // writing in the occupations output file
        PrintWriter pw = new PrintWriter(new FileWriter("../output/top_10_occupations.txt"));
        pw.println("TOP_OCCUPATIONS;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE");
        for(;i>=0;i--)
            pw.println(arr[i]);
        
        pw.close();
        i=0;
        
        // reversin g the order of Priority Queue for states
        while(pq2.size()>0)
            arr[i++]=pq2.poll();
        i--;
        // writing in the states output file
        pw = new PrintWriter(new FileWriter("../output/top_10_states.txt"));
        pw.println("TOP_STATES;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE");
        for(;i>=0;i--)
            pw.println(arr[i]);
        pw.close();

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime/1000000+" ms");
    }
}