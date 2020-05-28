import java.util.*; 
import java.text.ParseException;
import java.util.concurrent.TimeUnit;
 

class Solver 
{ 
	
	static int N = 100; 
	static int final_path[] = new int[N + 1];  
	static boolean visited[] = new boolean[N]; 
	static int final_res = Integer.MAX_VALUE; 
	static void copyToFinal(int curr_path[]) 
	{ 
		for (int i = 0; i < N; i++) 
			final_path[i] = curr_path[i]; 
		final_path[N] = curr_path[0]; 
	} 
	static int firstMin(int adj[][], int i) 
	{ 
		int min = Integer.MAX_VALUE; 
		for (int k = 0; k < N; k++) 
			if (adj[i][k] < min && i != k) 
				min = adj[i][k]; 
		return min; 
	} 
	static int secondMin(int adj[][], int i) 
	{ 
		int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE; 
		for (int j=0; j<N; j++) 
		{ 
			if (i == j) 
				continue; 

			if (adj[i][j] <= first) 
			{ 
				second = first; 
				first = adj[i][j]; 
			} 
			else if (adj[i][j] <= second && 
					adj[i][j] != first) 
				second = adj[i][j]; 
		} 
		return second; 
	} 
	static void VRPRec(int adj[][], int curr_bound, int curr_weight, 
				int level, int curr_path[]) 
	{ 
		if (level == N) 
		{ 
			if (adj[curr_path[level - 1]][curr_path[0]] != 0) 
			{ 
				int curr_res = curr_weight + 
						adj[curr_path[level-1]][curr_path[0]]; 
				if (curr_res < final_res) 
				{ 
					copyToFinal(curr_path); 
					final_res = curr_res; 
				} 
			} 
			return; 
		} 
		for (int i = 0; i < N; i++) 
		{ 
			if (adj[curr_path[level-1]][i] != 0 && 
					visited[i] == false) 
			{ 
				int temp = curr_bound; 
				curr_weight += adj[curr_path[level - 1]][i]; 
				if (level==1) 
				curr_bound -= ((firstMin(adj, curr_path[level - 1]) + 
								firstMin(adj, i))/2); 
				else
				curr_bound -= ((secondMin(adj, curr_path[level - 1]) + 
								firstMin(adj, i))/2); 
				if (curr_bound + curr_weight < final_res) 
				{ 
					curr_path[level] = i; 
					visited[i] = true; 
					VRPRec(adj, curr_bound, curr_weight, level + 1, 
						curr_path); 
				} 
				curr_weight -= adj[curr_path[level-1]][i]; 
				curr_bound = temp; 
				Arrays.fill(visited,false); 
				for (int j = 0; j <= level - 1; j++) 
					visited[curr_path[j]] = true; 
			} 
		} 
	} 
	static void VRP(int adj[][]) 
	{ 
		int curr_path[] = new int[N + 1]; 
		int curr_bound = 0; 
		Arrays.fill(curr_path, -1); 
		Arrays.fill(visited, false); 
		for (int i = 0; i < N; i++) 
			curr_bound += (firstMin(adj, i) + 
						secondMin(adj, i)); 
		curr_bound = (curr_bound==1)? curr_bound/2 + 1 : 
									curr_bound/2; 
		visited[0] = true; 
		curr_path[0] = 0; 
		VRPRec(adj, curr_bound, 0, 1, curr_path); 
	}  
	public static void main(String[] args) 
	{ 
		
		int no_of_nodes=0, no_of_veh=0;
		Scanner S = new Scanner(System.in);
		while(true)
		{
			System.out.println("Enter the number of nodes to visit ");
			no_of_nodes=S.nextInt();
			if(no_of_nodes==0)
				System.out.println("Nodes to visit can not be null!");
			else
			{	
				no_of_nodes++;
				while(true)
				{
					System.out.println("Enter the number of vehicles");
					no_of_veh=S.nextInt();
					if(no_of_veh==0)
						System.out.println("Atleast one vehicle is required!");
					else if(no_of_veh>(no_of_nodes-1))
						System.out.println("Number of vehicles cannot exceed number of nodes to visit!");
					else
						break;
				}
				break;
			}
			
		}
		int mat_size=no_of_nodes+no_of_veh-1;
		Solver.N=mat_size;
		int i=0, j=0;

		int[][] arr = new int[no_of_nodes][no_of_nodes];
		System.out.println("Enter the distance matrix");
		for(i=0; i<no_of_nodes; i++)
		{
			for(j=0; j<no_of_nodes; j++)
			{
				arr[i][j]=S.nextInt();
			}
		}
		long startTime = System.nanoTime();
		if(no_of_veh==1)
		{
			System.out.println("The distance matrix submitted is :");
			for(i=0; i<no_of_nodes; i++)
			{
				for(j=0; j<no_of_nodes; j++)
				{
					System.out.print(arr[i][j] + "\t");
				}
				System.out.print("\n");
			}
			VRP(arr); 
			System.out.printf("Minimum cost : %d\n", final_res); 
			System.out.printf("Path Taken : "); 
			int x;
			for (i = 0; i < N; i++) 
			{ 
				if(final_path[i]==0)
					System.out.printf(" d ");
				else
					System.out.printf(" - %d ", final_path[i]); 
			} 
			System.out.printf(" - d ");
			System.out.printf("\nVehicle 1: ");
			 
			for (i = 0; i < N; i++) 
			{ 
				if(final_path[i]==0)
					System.out.printf(" d ");
				else
					System.out.printf(" -> %d", final_path[i]); 
			}  
			System.out.println("-> d");
			long endTime = System.nanoTime();
			long durationInNano = (endTime - startTime);
		    long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);
			System.out.println("Time Taken: "+durationInMillis+" millisec");
		}
		else{

			int[][] newarr = new int[mat_size][mat_size];
			for(i=1; i<no_of_nodes; i++)
			{
				for(j=1; j<no_of_nodes; j++)
				{ 
					newarr[i-1][j-1]=arr[i][j];
				}
			}		

			int r=no_of_nodes-1;
			int c=0;
			for(i=0; i<no_of_nodes-1; i++)
			{
				for(j=no_of_nodes-1; j<mat_size; j++)
				{ 
					newarr[i][j]=arr[i+1][0];
					newarr[r][c]=newarr[i][j];
					r++;
				}
				r=no_of_nodes-1;
				c++;
			}

			for(i=no_of_nodes-1; i<mat_size; i++)
			{
				for(j=no_of_nodes-1; j<mat_size; j++)
				{ 
					newarr[i][j]=9999;
				}
			}
			
			System.out.println("The distance matrix is :");
			for(i=0; i<no_of_nodes; i++)
			{
				for(j=0; j<no_of_nodes; j++)
				{
					System.out.print(arr[i][j] + "\t");
				}
				System.out.print("\n");
			}
			//System.out.print("\n");
			// for(i=0; i<mat_size; i++)
			// {
			// 	for(j=0; j<mat_size; j++)
			// 	{
			// 		System.out.print(newarr[i][j] + "\t");
			// 	}
			// 	System.out.print("\n");
			// }		
			VRP(newarr); 
			System.out.printf("Minimum cost : %d\n", final_res); 
			System.out.printf("Path Taken : "); 
			System.out.printf("d "); 
			int x;
			for (i = 0; i < N; i++) 
			{ 
				x=final_path[i]+1;
					if(x>=no_of_nodes)
						System.out.printf(" - d");
					else 	
					System.out.printf(" - %d ", x); 
			}
			int count=1;
			System.out.printf("\nVehicle "+count+": ");
			System.out.printf("d"); 
			for (i = 0; i < N; i++) 
			{ 
				x=final_path[i]+1;
				if(x>=no_of_nodes)
				{
					count++;
					System.out.printf(" -> d "); 
					if(count>no_of_veh)
						break;
					System.out.printf("\nVehicle "+count+":");
					System.out.printf(" d");
				}
				else 	
					System.out.printf(" -> %d", x); 
			}  
			System.out.println("");
			long endTime = System.nanoTime();
			long durationInNano = (endTime - startTime);
		    long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);
			System.out.println("Time Taken: "+durationInMillis+" millisec");
		}
	} 
} 


//int arr[][] = {
  //       {
  //           0, 548, 776, 696, 582, 274, 502, 194, 308, 194, 536, 502, 388, 354,
  //           468, 776, 662
  //       },
  //       {
  //           548, 0, 684, 308, 194, 502, 730, 354, 696, 742, 1084, 594, 480, 674,
  //           1016, 868, 1210
  //       },
  //       {
  //           776, 684, 0, 992, 878, 502, 274, 810, 468, 742, 400, 1278, 1164,
  //           1130, 788, 1552, 754
  //       },
  //       {
  //           696, 308, 992, 0, 114, 650, 878, 502, 844, 890, 1232, 514, 628, 822,
  //           1164, 560, 1358
  //       },
  //       {
  //           582, 194, 878, 114, 0, 536, 764, 388, 730, 776, 1118, 400, 514, 708,
  //           1050, 674, 1244
  //       },
  //       {
  //           274, 502, 502, 650, 536, 0, 228, 308, 194, 240, 582, 776, 662, 628,
  //           514, 1050, 708
  //       },
  //       {
  //           502, 730, 274, 878, 764, 228, 0, 536, 194, 468, 354, 1004, 890, 856,
  //           514, 1278, 480
  //       },
  //       {
  //           194, 354, 810, 502, 388, 308, 536, 0, 342, 388, 730, 468, 354, 320,
  //           662, 742, 856
  //       },
  //       {
  //           308, 696, 468, 844, 730, 194, 194, 342, 0, 274, 388, 810, 696, 662,
  //           320, 1084, 514
  //       },
  //       {
  //           194, 742, 742, 890, 776, 240, 468, 388, 274, 0, 342, 536, 422, 388,
  //           274, 810, 468
  //       },
  //       {
  //           536, 1084, 400, 1232, 1118, 582, 354, 730, 388, 342, 0, 878, 764,
  //           730, 388, 1152, 354
  //       },
  //       {
  //           502, 594, 1278, 514, 400, 776, 1004, 468, 810, 536, 878, 0, 114,
  //           308, 650, 274, 844
  //       },
  //       {
  //           388, 480, 1164, 628, 514, 662, 890, 354, 696, 422, 764, 114, 0, 194,
  //           536, 388, 730
  //       },
  //       {
  //           354, 674, 1130, 822, 708, 628, 856, 320, 662, 388, 730, 308, 194, 0,
  //           342, 422, 536
  //       },
  //       {
  //           468, 1016, 788, 1164, 1050, 514, 514, 662, 320, 274, 388, 650, 536,
  //           342, 0, 764, 194
  //       },
  //       {
  //           776, 868, 1552, 560, 674, 1050, 1278, 742, 1084, 810, 1152, 274,
  //           388, 422, 764, 0, 798
  //       },
  //       {
  //           662, 1210, 754, 1358, 1244, 708, 480, 856, 514, 468, 354, 844, 730,
  //           536, 194, 798, 0
  //       }}; 