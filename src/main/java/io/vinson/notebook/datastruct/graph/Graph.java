package io.vinson.notebook.datastruct.graph;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author: jiangweixin
 * @date: 2019/5/13
 */
public class Graph {

    // 表示无穷大
    public static final int INFINITY = Integer.MAX_VALUE / 2;

    static class Vertex {
        public Set<Integer> neighbours;

        public Vertex(){
            neighbours = new HashSet<>();
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
    public void addEdge(int src, int dest){
        Vertex s = vertices[src];
        s.neighbours.add(dest);
    }

    /**
     * 添加多条边
     * @param src
     * @param dest
     */
    public void addEdge(int src, List<Integer> dest){
        Vertex s = vertices[src];
        s.neighbours.addAll(dest);
    }

    // 删除指定顶点相关的所有边
    public void removeEdge(int vex) {
        for(int i = 0; i < vertices.length; i++) {
            vertices[i].neighbours.remove(vex);
        }
        vertices[vex].neighbours.clear();
    }
    // 删除指定顶点集合相关的所有边
    public void removeEdge(List<Integer> vexs) {
        for(int i = 0; i < vertices.length; i++) {
            vertices[i].neighbours.removeAll(vexs);
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
     * TODO: 待完善
     * 深复制
     * @return
     */
    public Graph deepClone() {
        return this;
    }

}
