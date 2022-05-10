package com.aidootech.aidootechtest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class userDto {
    private String userName;
    private String isOnline;
    private String location;
}
