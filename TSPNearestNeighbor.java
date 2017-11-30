//This version messes with randomizing the start & taking minimum of a few runs
import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;
import java.util.Queue;
//import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//import java.util.concurrent.TimeUnit;
public class TSPNearestNeighbor
{
    private int numberOfNodes;
    private Stack<Integer> stack;
    private ArrayDeque<Integer> printable, temp;

    /* ----------------------------- GLOBAL VARIABLES ------------------------------ */
    private static int[][] distances;
    //private static int optimalDistance = Integer.MAX_VALUE;
    private static List<City> list;

    private static long startTime;
    private static long endTime;
    private static long elapsedTime;
    private static int size;
    private static int loops = 99;

    /* ----------------------------- CITY CLASS ------------------------------ */
    public static class City
    {
        int cityCode;
        int xCoordinate;
        int yCoordinate;

        City(int code, int x, int y)
        {
            this.cityCode = code;
            this.xCoordinate = x;
            this.yCoordinate = y;
        }

        public int getCityCode()
        {
            return cityCode;
        }

        public void setCityCode(int cityCode)
        {
            this.cityCode = cityCode;
        }

        public int getXCoordinate()
        {
            return xCoordinate;
        }

        public void setXCoordinate(int xCoordinate)
        {
            this.xCoordinate = xCoordinate;
        }

        public int getYCoordinate()
        {
            return yCoordinate;
        }

        public void setYCoordinate(int yCoordinate)
        {
            this.yCoordinate = yCoordinate;
        }

        public int distance(City other)
        {
            double val = Math.sqrt((Math.pow(this.xCoordinate - other.getXCoordinate(), 2) + (Math.pow(this.yCoordinate - other.getYCoordinate(), 2))));
            return (int) Math.round(val);
        }

        public int compareTo(City other)
        {
            return this.xCoordinate - other.getXCoordinate();
        }
    }

    public TSPNearestNeighbor()
    {
        stack = new Stack<Integer>();
        temp = new ArrayDeque<Integer>();
    }

    public ArrayDeque<Integer> tsp(int adjacencyMatrix[][], int start)
    {
        numberOfNodes = adjacencyMatrix[1].length;
        //System.out.println("Number of nodes: "+ numberOfNodes);
        int[] visited = new int[numberOfNodes];

        visited[start] = 1;
        stack.push(start);

        int element, dst = 0, i, last = 0;
        int min = Integer.MAX_VALUE;
        boolean minFlag = false;

        int cost = 0;

        while (!stack.isEmpty())
        {
            element = stack.peek();
            i = 0; //column iterator count
            min = Integer.MAX_VALUE;
            while (i < numberOfNodes)
            {
                if (adjacencyMatrix[element][i] >= 1 && visited[i] == 0)
                {
                    if (min > adjacencyMatrix[element][i])
                    {
                        min = adjacencyMatrix[element][i];
                        dst = i;
                        minFlag = true;
                    }
                }
                i++;
            }
            if (minFlag)
            {
                visited[dst] = 1;
                stack.push(dst);
                temp.addLast(dst);
                last = dst; //save last index so you can link to beginning again
                cost += min;
                minFlag = false;
                continue;
            }
            stack.pop();
        }
        cost += adjacencyMatrix[start][last]; //complete the loop
        temp.addLast(start);

        temp.addFirst(cost); //needs to be first to print to file
        // while(!printable.isEmpty()){
        // System.out.println(printable.remove());
        // }
        return temp;
    }

    private static void createDistanceMatrix(){
        int cities_length = list.size();
        distances = new int[cities_length][cities_length];

        for(int row = 0; row < cities_length; row++){

            City city1 = list.get(row);

            for(int col = 0; col < cities_length; col++){

                City city2 = list.get(col);
                int distance_between = city1.distance(city2);

                distances[row][col] = distance_between;
                //System.out.printf("%d\t", distance_between);

            }
            //System.out.println();
        }

        //System.out.println(Arrays.deepToString(distances).replace("], ", "]\n"));
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        startTime = System.currentTimeMillis();
        String fileName = args[0];
        PrintStream out = new PrintStream(new File(fileName + ".tour"));
        System.setOut(out);
        int number_of_nodes;

        Random generator = new Random(startTime);
        //loops = 99;
        ArrayList<ArrayDeque> save = new ArrayList<ArrayDeque>();
        try
        {

            list = new ArrayList<>();
            try(Scanner scanner = new Scanner(new File(args[0]))) {
                while(scanner.hasNext()) {
                    list.add(new City(scanner.nextInt(),scanner.nextInt(), scanner.nextInt()));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            createDistanceMatrix();
            size = list.size();
            //instaniate stack & queues

            //run a few times
            TSPNearestNeighbor TSPNearestNeighbor = new TSPNearestNeighbor();
            save.add(TSPNearestNeighbor.tsp(distances, 0));
            for(int i = 1; i <= loops; i++){
                TSPNearestNeighbor = new TSPNearestNeighbor();
                int randStart = generator.nextInt(size);
                save.add(TSPNearestNeighbor.tsp(distances, randStart));
            }

            ArrayDeque minDeque = save.get(0); //get the first ArrayDeque from the ArrayList
            // String test = min.toString(); // works
            // System.out.println("Testing string 0");
            // System.out.print(test);
            int minDistance = (Integer)minDeque.getFirst(); //and then at that deque's first entry, which is distance

            int idx = 0;
            for(int i = 1; i <= loops; i++){
                ArrayDeque t = save.get(i);
                if((Integer)t.getFirst() < minDistance){
                    minDeque = t;
                    idx = i;
                }
            }
            while(!minDeque.isEmpty()){
                System.out.println(minDeque.remove());
            }

        } catch (InputMismatchException inputMismatch)
        {
            System.out.println("Wrong Input format");
        }
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        System.out.println("Time in sec: " + elapsedTime/100);
        System.out.println("Time in min: " + elapsedTime/100/60);
    }

}
