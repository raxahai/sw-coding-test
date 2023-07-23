package com.smallworld.data;

import lombok.Data;

@Data
public class Issue {
    private Long id;
    private Boolean solved;
    private String message;
}
