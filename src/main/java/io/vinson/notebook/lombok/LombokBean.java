package io.vinson.notebook.lombok;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/2/27
 */

@Builder
@Getter
@ToString
public class LombokBean {

    private int id;

    private String name;

    public static void main(String[] args) {

        List<LombokBean> beanList = new ArrayList<>();
        LombokBean bean1 = LombokBean.builder().id(1).name("first bean").build();
        LombokBean bean2 = LombokBean.builder().id(2).name("second bean").build();
        beanList.add(bean1);
        beanList.add(bean2);

        for(LombokBean bean : beanList) {
            System.out.println(bean.toString());
        }
    }
}
