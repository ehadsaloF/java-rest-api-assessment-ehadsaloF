package com.cbfacademy.apiassessment.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private Date created;
    private String name;
    private String username;
    private String email;
    private Date updated;
}
