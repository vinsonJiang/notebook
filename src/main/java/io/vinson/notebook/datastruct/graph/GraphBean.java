package io.vinson.notebook.datastruct.graph;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: jiangweixin
 * @date: 2019/5/7
 */
@Getter
@Setter
public class GraphBean {
    /**顶点个数*/
    private int vexNum;
    /**连接的各个点*/
    private List<GraphBean> vexList;
}
