package com.dishcovery.backend.controller.admin;


import com.dishcovery.backend.dto.RecipeResponseDto;
import com.dishcovery.backend.dto.UserResponseDto;
import com.dishcovery.backend.model.Recipe;
import com.dishcovery.backend.service.RecipeServiceImple;
import com.dishcovery.backend.service.UserServicesImp;
import com.dishcovery.backend.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private AdminService adminService;

    @GetMapping("/fetch-users")
    public ResponseEntity<List<UserResponseDto>> fetchAllUsers(
            @RequestParam("page") int pageNumber,
            @RequestParam("size") int pageSize
    ) throws IOException {
        List<UserResponseDto> userResponseDtos = userServicesImp.fetchAllUsers(pageNumber, pageSize);
        return ResponseEntity.ok().body(userResponseDtos);
    }
    @PatchMapping("/recipe/feature")
    public ResponseEntity<?> featureRecipes(Long recipeId, @RequestParam("status") boolean status) {
        Recipe recipe  = adminService.featureRecipe(recipeId, status);
        if(recipe != null) {
            return ResponseEntity.ok().body(recipe);
        }
        return ResponseEntity.badRequest().build();
    }
}
