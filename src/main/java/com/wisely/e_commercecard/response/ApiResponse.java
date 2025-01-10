package com.wisely.e_commercecard.response;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor

public class ApiResponse {
    private String message;
    private Object data;
}
