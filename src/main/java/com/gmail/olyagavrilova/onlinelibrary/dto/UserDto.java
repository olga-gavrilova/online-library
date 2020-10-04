package com.gmail.olyagavrilova.onlinelibrary.dto;

import lombok.Data;

@Data
public class UserDto {

    private long id;

    private String username;

    private boolean enabled;

}
