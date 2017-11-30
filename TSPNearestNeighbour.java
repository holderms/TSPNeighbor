import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;
import java.util.Queue;
import java.util.Deque;
import java.util.ArrayDeque;
import java.io.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
public class TSPNearestNeighbour
{
    private int numberOfNodes;
    private Stack<Integer> stack;
    private Deque<Integer> printable;

    /* ----------------------------- GLOBAL VARIABLES ------------------------------ */
    private static int[][] distances;
    private static int optimalDistance = Integer.MAX_VALUE;
    private static String optimalPath = "";
    private static List<City> list;

    private static long startTime;
    private static long endTime;
    private static long elapsedTime;
    private static int size;

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

    }

    public TSPNearestNeighbour()
    {
        stack = new Stack<Integer>();
        printable = new ArrayDeque<Integer>();
    }

    public void tsp(int adjacencyMatrix[][])
    {
        numberOfNodes = adjacencyMatrix[1].length;
        //System.out.println("Number of nodes: "+ numberOfNodes);
        int[] visited = new int[numberOfNodes];
        visited[0] = 1;
        stack.push(0);
        //printable.addFirsts(0);

        int element, dst = 0, i, last = 0;
        int min = Integer.MAX_VALUE;
        boolean minFlag = false;

        int cost = 0;

        while (!stack.isEmpty())
        {
            element = stack.peek();
            i = 1; //iterator count (need to look at 0 again? I think it just skips first while loop)
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
                printable.addLast(dst);
                last = dst; //save last index so you can link to 0/beginning again
                /////////////////////////////////////System.out.print(dst + "\t");
                cost += min;
                last = dst;
                minFlag = false;
                continue;
            }
            stack.pop();
        }
        cost += adjacencyMatrix[0][last]; //complete the loop
        printable.addLast(0);

        printable.addFirst(cost); //needs to be first to print to file
        while(!printable.isEmpty()){
            System.out.println(printable.remove());
        }
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

            }

        }

        //System.out.println(Arrays.deepToString(distances).replace("], ", "]\n"));
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        String fileName = args[0];
        PrintStream out = new PrintStream(new File(fileName + ".tour"));
        System.setOut(out);
        int number_of_nodes;


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


            TSPNearestNeighbour tspNearestNeighbour = new TSPNearestNeighbour();

            tspNearestNeighbour.tsp(distances);
        } catch (InputMismatchException inputMismatch)
        {
            System.out.println("Wrong Input format");
        }


    }

    // public static void test(){
        // // for(int i = 3; i > 0; i--){
        // // System.out.printf("Test %d, file 1\n", i);
         // String[] testing = {"tsp_example_3.txt"};
        // // main(testing);

        // // testing[0] = "tsp_example_2.txt";
        // // System.out.printf("Test %d, file 2\n", i);
        // // main(testing);

        // //testing[0] = "tsp_example_3.txt";
        // System.out.printf("Test 1, file 3\n");
        // main(testing);
    // //}
    // }
}
