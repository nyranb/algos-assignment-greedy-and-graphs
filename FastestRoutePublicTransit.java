/**
 * Public Transit
 * Author: Nyran Bonilla and Carolyn Yao
 * Does this compile? Y
 */

/**
 * This class contains solutions to the Public, Public Transit problem in the
 * shortestTimeToTravelTo method. There is an existing implementation of a
 * shortest-paths algorithm. As it is, you can run this class and get the
 * solutions from the existing shortest-path algorithm.
 */
public class FastestRoutePublicTransit {

  /**
   * The algorithm that could solve for shortest travel time from a station S to a
   * station T given various tables of information about each edge (u,v)
   *
   * @param S         the s th vertex/station in the transit map, start From
   * @param T         the t th vertex/station in the transit map, end at
   * @param startTime the start time in terms of number of minutes from 5:30am
   * @param lengths   lengths[u][v] The time it takes for a train to get between
   *                  two adjacent stations u and v
   * @param first     first[u][v] The time of the first train that stops at u on
   *                  its way to v, int in minutes from 5:30am
   * @param freq      freq[u][v] How frequently is the train that stops at u on
   *                  its way to v
   * @return shortest travel time between S and T
   */

  public int myShortestTravelTime(int S, int T, int startTime, int[][] lengths, int[][] first, int[][] freq) {
    // Your code along with comments here. Feel free to borrow code from any
    // of the existing method. You can also make new helper methods.
    int vertices = lengths[0].length; //variable to hold vertices amount
    int routes[] = dijkstra(lengths, S, T, vertices); //shortest path 
    int k = vertices - 1; //set index for traversing
    int shortestTime = 0; //initialize shortestTime for return
    int nextT; //initialize to hold value of next train
    int t = startTime;

    //return back to source/beginning of routes (like in Dijkstra function)
    while (routes[k] != S && k > 0) {
      k--;
    }
    
    //traverse the arrays to find the shortestTime using next train and starting times
    int temp = k;

    while(temp != 1) {
      nextT = incoming(lengths, first, freq, routes, t, temp); //finds next train using given tables, time and route
      shortestTime = shortestTime + (nextT - t);
      t = nextT;
      temp--;
    }
    return shortestTime;
  }

  // Modified version of dijkstra from geekforgeeks to find shortest path from src to target
  public int[] dijkstra(int graph[][], int src, int tar, int V) {
    int dist[] = new int[V];
    int prev[] = new int[V];
    int finalTimes[] = new int[V];
    prev[src] = -1;

    // sptSet[i] will true if vertex i is included in shortest
    // path tree or shortest distance from src to i is finalized
    Boolean sptSet[] = new Boolean[V];

    // Initialize all distances as INFINITE and stpSet[] as false
    for (int i = 0; i < V; i++) {
      finalTimes[i] = Integer.MAX_VALUE;
      sptSet[i] = false;
      dist[i] = -1;
    }

    // Distance of source vertex from itself is always 0
    finalTimes[src] = 0;

    // Find shortest path for all vertices
    for (int count = 0; count < V - 1; count++) {
      // Pick the minimum distance vertex from the set of vertices
      // not yet processed. u is always equal to src in first
      // iteration.
      int u = findNextToProcess(finalTimes, sptSet);

      // Mark the picked vertex as processed
      sptSet[u] = true;

      // Update finalTimes value of the adjacent vertices of the
      // picked vertex.
      for (int v = 0; v < V; v++)

        // Update finalTimes[v] only if is not in sptSet, there is an
        // edge from u to v, and total weight of path from src to
        // v through u is smaller than current value of dist[v]
        if (!sptSet[v] && graph[u][v] != 0 && finalTimes[u] != Integer.MAX_VALUE && finalTimes[u] + graph[u][v] < finalTimes[v]) {
          finalTimes[v] = finalTimes[u] + graph[u][v];
          prev[v] = u;
        }
    }

    int tempTarget = tar; 
    int currStat = 0; 

    //go back to source and add source station to dist[]
    while (tempTarget != src) {
      dist[currStat++] = tempTarget;
      tempTarget = prev[tempTarget];
    }
    dist[currStat] = tempTarget;
    
    return dist;
  }

  //Helps find incoming train to station in order to help find shortest time
  public int incoming(int lengths[][], int first[][], int freq[][], int path[], int time, int i) {
    //base case if already at target, then we do nothing
    if (i == 0) {
      return 0;
    }
    int currStation = path[i];
    int nextStation = path[i - 1]; 
    int trainArrivals = first[currStation][nextStation];// time each train arrives, initialized to first train arrival time
    int j = 0; 
    
    while (trainArrivals < time) {
      trainArrivals = first[currStation][nextStation] + (j * freq[currStation][nextStation]); //finds train arrivals times using when the first train arrives and the frequency of arriving trains
      j++;
    }
    int next = trainArrivals + lengths[currStation][nextStation];
    return next;
  }

  /**
   * Finds the vertex with the minimum time from the source that has not been
   * processed yet.
   * 
   * @param times     The shortest times from the source
   * @param processed boolean array tells you which vertices have been fully
   *                  processed
   * @return the index of the vertex that is next vertex to process
   */
  public int findNextToProcess(int[] times, Boolean[] processed) {
    int min = Integer.MAX_VALUE;
    int minIndex = -1;

    for (int i = 0; i < times.length; i++) {
      if (processed[i] == false && times[i] <= min) {
        min = times[i];
        minIndex = i;
      }
    }
    return minIndex;
  }

  public void printShortestTimes(int times[]) {
    System.out.println("Vertex Distances (time) from Source");
    for (int i = 0; i < times.length; i++)
      System.out.println(i + ": " + times[i] + " minutes");
  }

  /**
   * Given an adjacency matrix of a graph, implements
   * 
   * @param graph  The connected, directed graph in an adjacency matrix where if
   *               graph[i][j] != 0 there is an edge with the weight graph[i][j]
   * @param source The starting vertex
   */
  public void shortestTime(int graph[][], int source) {
    int numVertices = graph[0].length;

    // This is the array where we'll store all the final shortest times
    int[] times = new int[numVertices];

    // processed[i] will true if vertex i's shortest time is already finalized
    Boolean[] processed = new Boolean[numVertices];

    // Initialize all distances as INFINITE and processed[] as false
    for (int v = 0; v < numVertices; v++) {
      times[v] = Integer.MAX_VALUE;
      processed[v] = false;
    }

    // Distance of source vertex from itself is always 0
    times[source] = 0;

    // Find shortest path to all the vertices
    for (int count = 0; count < numVertices - 1; count++) {
      // Pick the minimum distance vertex from the set of vertices not yet processed.
      // u is always equal to source in first iteration.
      // Mark u as processed.
      int u = findNextToProcess(times, processed);
      processed[u] = true;

      // Update time value of all the adjacent vertices of the picked vertex.
      for (int v = 0; v < numVertices; v++) {
        // Update time[v] only if is not processed yet, there is an edge from u to v,
        // and total weight of path from source to v through u is smaller than current
        // value of time[v]
        if (!processed[v] && graph[u][v] != 0 && times[u] != Integer.MAX_VALUE && times[u] + graph[u][v] < times[v]) {
          times[v] = times[u] + graph[u][v];
        }
      }
    }

    printShortestTimes(times);
  }

  public static void main(String[] args) {
    /* length(e) */
    int lengthTimeGraph[][] = new int[][] { 
      { 0, 4, 0, 0, 0, 0, 0, 8, 0 }, 
      { 4, 0, 8, 0, 0, 0, 0, 11, 0 },
        { 0, 8, 0, 7, 0, 4, 0, 0, 2 }, 
        { 3, 0, 7, 0, 9, 14, 0, 0, 0 }, 
        { 0, 0, 0, 9, 0, 10, 0, 0, 0 },
        { 0, 7, 4, 14, 10, 0, 2, 0, 0 }, 
        { 9, 8, 0, 7, 0, 2, 0, 1, 6 }, 
        { 8, 11, 0, 0, 0, 0, 1, 0, 7 },
        { 0, 0, 2, 0, 0, 0, 6, 7, 0 } };
    FastestRoutePublicTransit t = new FastestRoutePublicTransit();
    t.shortestTime(lengthTimeGraph, 0);

    // You can create a test case for your implemented method for extra credit below
    int first[][]=new int[][] {
    	{0, 2, 0, 6, 0, 2, 0, 0, 1},
      {16, 0, 22, 0, 0, 9, 0, 3, 0},
      {0, 7, 0, 24, 0, 0, 0, 0, 3},
      {32, 0, 11, 0, 9, 0, 0, 14, 0},
      {0, 0, 0, 40, 0, 28, 0, 0, 0},
      {11, 48, 0, 0, 42, 0, 8, 0, 0},
      {0, 0, 0, 0, 0, 6, 0, 20, 0},
      {0, 7, 0, 19, 0, 0, 23, 0, 2},
      {5, 0, 0, 0, 5, 0, 5, 8, 0}
    };

    /*freq(e)*/
    int freq[][]= new int[][] {
    	{0, 8, 0, 10, 0, 4, 0, 0, 3},
      {12, 0, 3, 0, 0, 3, 0, 4, 0},
      {0, 7, 0, 3, 0, 0, 0, 9, 0},
      {5, 0, 13, 0, 5, 0, 11, 6, 0},
      {0, 0, 0, 5, 0, 14, 0, 0, 0},
      {8, 5, 0, 2, 4, 0, 16, 0, 0},
      {0, 4, 0, 12, 0, 4, 0, 6, 0},
      {0, 7, 0, 9, 0, 0, 8, 0, 5},
      {11, 0, 3, 0, 4, 0, 0, 9, 0}
    };
    /*length(e)*/
    int length[][] = new int[][]{
      {0, 5, 0, 5, 0, 2, 0, 3, 9},
      {8, 0, 2, 0, 0, 3, 0, 7, 0},
      {0, 2, 0, 1, 0, 9, 0, 6, 0},
      {4, 0, 1, 0, 5, 0, 0, 2, 0},
      {0, 11, 0, 5, 0, 4, 0, 0, 0},
      {8, 3, 0, 0, 4, 0, 2, 0, 0},
      {0, 0, 12, 0, 0, 2, 0, 2, 0},
      {2, 7, 0, 2, 0, 0, 2, 0, 5},
      {7, 0, 2, 0, 0, 5, 0, 5, 0}
    };

    int startTime = 0;
    int src = 1;
    int target = 4;
    FastestRoutePublicTransit test = new FastestRoutePublicTransit();
    test.shortestTime(length, 0);
    int time = test.myShortestTravelTime(src, target, startTime,length, first, freq); //Should print shortest time is 8 minutes
    System.out.println("The shortest time from station " + src + " to station " + target + " is " + time);

  }
}

