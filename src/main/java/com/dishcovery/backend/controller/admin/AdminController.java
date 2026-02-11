package com.dishcovery.backend.controller.admin;


import com.dishcovery.backend.dto.RecipeResponseDto;
import com.dishcovery.backend.dto.UserResponseDto;
import com.dishcovery.backend.service.RecipeServiceImple;
import com.dishcovery.backend.service.UserServicesImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    ///  Aalik Ealxi kaam xa hei yo chai
    @Autowired
    private RecipeServiceImple recipeServiceImple;

    @Autowired
    private UserServicesImp userServicesImp;

    @GetMapping("/fetch-users")
    public ResponseEntity<List<UserResponseDto>> fetchAllUsers(
            @RequestParam("page") int pageNumber,
            @RequestParam("size") int pageSize
    ) throws IOException {
        List<UserResponseDto> userResponseDtos = userServicesImp.fetchAllUsers(pageNumber, pageSize);
        return ResponseEntity.ok().body(userResponseDtos);
    }
}
