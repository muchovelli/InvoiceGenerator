package com.invoicegenerator.invoicegenerator.controllers.resources;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResult {

    @NonNull
    private String jwt;
}
