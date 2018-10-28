import java.io.*;
import java.util.*;

class solution{

    //Custom Node to store count and state/occupation
    static class Node implements Comparable<Node>{
        int count;//maintaining count
        String str;// state/occupation
        static int totalCertified;//total applications that were Certified
        Node(String s){
            this.count = 1;
            this.str = s;
        }
        /*@override toString to print Node content in custom format*/
        public String toString(){
            float percent = (float)count/totalCertified;
            String percentage = String.format("%.1f",percent*100) ;
            return String.valueOf(str+";"+count+";"+percentage+"%");
        }
        /* @Override compareTO to compare two Node to decide whether to remove minimum element in Priority Queue or not*/
        public int compareTo(Node n){
            int ret = n.count-this.count;
            if(ret==0){
                ret = n.str.compareTo(this.str);
            }
            //System.out.println(n1.str+"  "+n2.str+" "+ret);
            return ret;
        }
    }
    /*Custom Comparator to sort Node in Priority Queue first ascending order on the basis of count
    then if same then desc order on the basis of name of state/occupation */
    static class NodeComprator implements Comparator<Node>{
        public int compare(Node n1,Node n2){
            int ret = n1.count-n2.count;
            if(ret==0){
                ret = n2.str.compareTo(n1.str);
            }
            //System.out.println(n1.str+"  "+n2.str+" "+ret);
            return ret;
        }
    }
    public static void main(String[] ss) throws Exception{
        long startTime   = System.nanoTime();
        //System.out.println(ss[0]);
        BufferedReader br = new BufferedReader(new FileReader("../input/H1B_FY_2014.csv"));
        String line = br.readLine();
        String[] columns = line.split(";");
        int occuptionIndex = 0;
        int stateIndex = 0;
        int certificateIndex = 0;
        PriorityQueue<Node> pq = new PriorityQueue<Node>(10, new NodeComprator()); 
        Map<String,Node> stateCount = new HashMap<>();
        Map<String,Node> occupationCount = new HashMap<>();
        for(int i=0; i<columns.length;i++){
            if(columns[i].equals("SOC_NAME")|| columns[i].equals("LCA_CASE_SOC_NAME"))
                occuptionIndex = i;
            else if(columns[i].equals("CASE_STATUS")|| columns[i].equals("STATUS"))
                certificateIndex = i;
            else if(columns[i].equals("WORKSITE_STATE")|| columns[i].equals("LCA_CASE_WORKLOC1_STATE"))
                stateIndex = i;
            //System.out.println(columns[i]);
        }
        while((line = br.readLine())!=null){
            String[] data = line.split(";");
            if(data[certificateIndex].equals("CERTIFIED")){
                Node.totalCertified++;
                String state = data[stateIndex].replaceAll("^\"|\"$", "");
                if(stateCount.containsKey(state)){
                    stateCount.get(state).count++;
                }
                else{
                    stateCount.put(state,new Node(state));
                }
                String occupation = data[occuptionIndex].replaceAll("^\"|\"$", "");
                if(occupationCount.containsKey(occupation)){
                    Node temp = occupationCount.get(occupation);
                    temp.count++;
                }
                else{
                    occupationCount.put(occupation,new Node(occupation));
                }
            }
        }
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
        Node[] arr = new Node[20];
        int i=0;
        while(pq.size()>0){
            arr[i++]=pq.poll();
        }
        i--;
        PrintWriter pw = new PrintWriter(new FileWriter("../output/top_10_occupations.txt"));
        pw.println("TOP_STATES;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE");
        for(;i>=0;i--)
            pw.println(arr[i]);
        i=0;
        while(pq2.size()>0)
            arr[i++]=pq2.poll();
        i--;
        pw.close();
        pw = new PrintWriter(new FileWriter("../output/top_10_states.txt"));
        pw.println("TOP_OCCUPATIONS;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE");
        for(;i>=0;i--)
            pw.println(arr[i]);
        pw.close();
        //System.out.println(pq);
        //System.out.println("CA".compareTo("DL"));
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime/1000000);
    }
}