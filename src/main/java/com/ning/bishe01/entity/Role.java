package com.ning.bishe01.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Integer rid;
    private String rolename;
    private String description;
    private String permissions;
}
