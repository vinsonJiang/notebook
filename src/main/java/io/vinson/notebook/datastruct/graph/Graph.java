package io.vinson.notebook.datastruct.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: jiangweixin
 * @date: 2019/5/13
 */
public class Graph {

    // 表示无穷大
    public static final int INFINITY = Integer.MAX_VALUE / 2;

    static class Vertex {
        /** key：边，value：权重 */
        public Map<Integer, Integer> neighbours;

        public Vertex(){
            neighbours = new HashMap<>();
        }
    }

    private Vertex[] vertices;

    public Graph(int vexNum){
        vertices = new Vertex[vexNum];
        for(int i=0; i<vexNum; i++){
            vertices[i] = new Vertex();
        }
    }

    /**
     * 添加一条边
     * @param src
     * @param dest
     */
    public void addEdge(int src, int dest, int weight){
        Vertex s = vertices[src];
        s.neighbours.put(dest, weight);
    }

    /**
     * 添加多条边
     * @param src
     * @param dest
     */
    public void addEdge(int src, List<Integer> dest, List<Integer> weight){
        Vertex s = vertices[src];
        for(int i = 0; i < dest.size(); i++) {
            s.neighbours.put(dest.get(i), weight.get(i));
        }
    }

    /**
     * map方式添加多条边
     * @param src
     * @param neighbours
     */
    public void addEdge(int src, Map<Integer, Integer> neighbours){
        Vertex s = vertices[src];
        s.neighbours.putAll(neighbours);
    }

    /**
     * @param vex
     */
    // 删除指定顶点相关的所有边
    public void removeEdge(Integer vex) {
        for(int i = 0; i < vertices.length; i++) {
            vertices[i].neighbours.remove(vex);
        }
        vertices[vex].neighbours.clear();
    }

    public void removeEdge(Integer start, Integer end) {
        vertices[start].neighbours.remove(end);
        vertices[end].neighbours.remove(start);
    }
    // 删除指定顶点集合相关的所有边
    public void removeEdge(List<Integer> vexs) {
        for(int i = 0; i < vertices.length; i++) {
            for (Integer vex : vexs) {
                vertices[i].neighbours.remove(vex);
            }
        }
        for(Integer vex : vexs) {
            vertices[vex].neighbours.clear();
        }
    }

    public int getVexNum() {
        return vertices.length;
    }

    public Vertex getVertex(int vert){
        return vertices[vert];
    }

    /**
     * 深复制
     * @return
     */
    public Graph deepClone() {
        Graph temp = new Graph(this.getVexNum());
        for(int i = 0; i < temp.getVexNum(); i++) {
            temp.addEdge(i, getVertex(i).neighbours);
        }
        return temp;
    }

}
