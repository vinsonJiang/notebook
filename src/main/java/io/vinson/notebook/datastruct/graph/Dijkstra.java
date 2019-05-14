package io.vinson.notebook.datastruct.graph;

import java.util.*;

/**
 * @author: jiangweixin
 * @date: 2019/5/14
 */
public class Dijkstra {

    /**
     * 计算 start->end 的最短路径
     * 如果不可到达，返回数组元素只包括其本身
     * @param graph
     * @param start
     * @param end
     * @return
     */
    public static List<Integer> calculate(Graph graph, int start, int end) {
        int[][] matrix = getMatrix(graph);
        int n = matrix.length; // 顶点个数

        boolean[] visited = new boolean[n];
        int[] shortPath = new int[n];
        List<Integer>[] path = new List[n];
        for(int i = 0; i < n; i++) {
            path[i] = new ArrayList<>(Arrays.asList(start, i));
        }
        shortPath[start] = 0;
        visited[start] = true;
        for (int count = 1; count < n; count++) {
            int k = 0;
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && matrix[start][i] < min) {
                    min = matrix[start][i];
                    k = i;
                }
            }
            shortPath[k] = min;
            visited[k] = true;

            for (int i = 0; i < n; i++) {
                if (!visited[i] && matrix[start][k] + matrix[k][i] < matrix[start][i]) {
                    matrix[start][i] = matrix[start][k] + matrix[k][i];
                    path[i] = new ArrayList<>(path[k]);
                    path[i].add(i);
                }
            }
        }

        if(shortPath[end] >= Graph.INFINITY) {
            return new ArrayList<>(Arrays.asList(start));
        }
        return path[end];
    }

    public static int[][] getMatrix(Graph graph) {
        int[][] matrix = new int[graph.getVexNum()][graph.getVexNum()];
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = (i == j) ? 0 : Graph.INFINITY;
            }
            for(Integer x : graph.getVertex(i).neighbours) {
                matrix[i][x] = 1;
            }
        }
        return matrix;
    }

    public static void main(String[] args) {
        Graph graph = new Graph(19);

        graph.addEdge(0, Arrays.asList(3,5,6));
        graph.addEdge(1, Arrays.asList(3,4,7));
        graph.addEdge(2, Arrays.asList(4,5,8));
        graph.addEdge(3, Arrays.asList(0,1,15));
        graph.addEdge(4, Arrays.asList(1,2,16));
        graph.addEdge(5, Arrays.asList(0,2,17));
        graph.addEdge(6, Arrays.asList(0,9));
        graph.addEdge(7, Arrays.asList(1,10));
        graph.addEdge(8, Arrays.asList(2,11));
        graph.addEdge(9, Arrays.asList(6,12,14));
        graph.addEdge(10, Arrays.asList(7,12,13));
        graph.addEdge(11, Arrays.asList(8,13,14));
        graph.addEdge(12, Arrays.asList(9,10,15,18));
        graph.addEdge(13, Arrays.asList(10,11,16,18));
        graph.addEdge(14, Arrays.asList(9,11,17,18));
        graph.addEdge(15, Arrays.asList(3,12));
        graph.addEdge(16, Arrays.asList(4,13));
        graph.addEdge(17, Arrays.asList(5,14));
        graph.addEdge(18, Arrays.asList(12,13,14));

        graph.removeEdge(1);
        graph.removeEdge(2);

        List<Integer> path = Dijkstra.calculate(graph, 0, 0);

        System.out.println(path);
    }
}
