package io.vinson.notebook.webflux.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Player {
    private int id;

    private String name;

    private int num;
}
