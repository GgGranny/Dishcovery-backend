package com.dishcovery.backend.controller;


import com.dishcovery.backend.dto.RecipeDto;
import com.dishcovery.backend.dto.RecipeResponseDto;
import com.dishcovery.backend.interfaces.RecipeService;
import com.dishcovery.backend.model.Recipe;
import com.dishcovery.backend.model.Steps;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.model.Video;
import com.dishcovery.backend.service.RecipeServiceImple;
import com.dishcovery.backend.service.UserServicesImp;
import com.dishcovery.backend.service.VideoServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/recipes")
public class RecipeController {

    private final RecipeServiceImple recipeService;
    private final VideoServiceImplementation videoService;

    public RecipeController(RecipeServiceImple recipeService, VideoServiceImplementation videoService) {
        this.recipeService = recipeService;
        this.videoService = videoService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Recipe> uploadRecipe(
            @RequestParam(name = "file", required = false) MultipartFile videoFile,
            @RequestParam(name = "videoTitle", required = false) String videoTitle,
            @RequestParam(name = "videoDescription", required = false) String videoDescription,
            @RequestParam(name = "recipeName") String recipeName,
            @RequestParam(name = "recipeDescription") String recipeDescription,
            @RequestParam(name = "category") String category,
            @RequestParam(name = "recipeThumbnail") MultipartFile recipeThumbnail,
            @RequestParam(name= "cookTime") String cookTime,
            @RequestParam(name = "steps") String steps,
            @RequestParam(name = "ingredients" ) String ingredients
    ) throws IOException {
        Video video = null;
        if(videoFile != null && videoTitle != null && videoDescription != null ) {
            video = videoService.upload(videoTitle, videoDescription, videoFile);
        }
        RecipeDto recipe = new RecipeDto();
        recipe.setRecipeName(recipeName);
        recipe.setSteps(steps);
        recipe.setCategory(category);
        recipe.setRecipeName(recipeName);
        recipe.setDescription(recipeDescription);
        recipe.setCookTime(cookTime);
        recipe.setIngredients(ingredients);

        byte[] thumbnailArray = recipeThumbnail.getBytes();
        String recipeThumbnailBase64 = Base64.getEncoder().encodeToString(thumbnailArray);
        recipe.setThumbnail(recipeThumbnailBase64);

        Recipe result = recipeService.uploadRecipe(recipe , video);
        if(result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(result);
    }


    @GetMapping("/user/r1/{userId}")
    public ResponseEntity<List<Recipe>> getRecipeByUserId(@PathVariable("userId") int userId) {
        List<Recipe> recipes = recipeService.getRecipeByUserId(userId);
        return ResponseEntity.ok().body(recipes);
    }

    @GetMapping("/video/v1/{videoId}")
    public ResponseEntity<Recipe> getVideoByVideoId(@PathVariable("videoId") String videoId) {
        Recipe recipe = recipeService.getRecipeByVideoId(videoId);
        return ResponseEntity.ok().body(recipe);
    }

    @GetMapping("/recipe/r1/{recipeId}")
    public ResponseEntity<RecipeResponseDto> getRecipeByRecipeId(@PathVariable("recipeId") long recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        RecipeResponseDto recipeResponseDto = new RecipeResponseDto();
        recipeResponseDto.setRecipeId(recipe.getRecipeId());
        recipeResponseDto.setRecipeName(recipe.getRecipeName());
        recipeResponseDto.setDescription(recipe.getDescription());
        recipeResponseDto.setCategory(recipe.getCategory());
        recipeResponseDto.setCookTime(recipe.getCookTime());
        recipeResponseDto.setIngredients(recipe.getIngredients());
        if(recipe.getVideo() != null) {
            recipeResponseDto.setVideoId(recipe.getVideo().getVideoId());
        }else {
            recipeResponseDto.setVideoId(null);
        }

        if(recipe.getSteps() != null ) {
            List<String> stepsCopy = new ArrayList<>(recipe.getSteps().getSteps());
            recipeResponseDto.setSteps(stepsCopy);
        }
        recipeResponseDto.setThumbnail(recipe.getThumbnail());
        recipeResponseDto.setUserid(recipe.getUser().getId());
        recipeResponseDto.setEmail(recipe.getUser().getEmail());
        recipeResponseDto.setProfilePicture(recipe.getUser().getProfilePicture());
        recipeResponseDto.setUsername(recipe.getUser().getUsername());
        if(recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(recipeResponseDto);
    }

    @PutMapping("/recipe/r1/update/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable("recipeId") long recipeId,
            @RequestParam(name = "recipeName") String recipeName,
            @RequestParam(name = "recipeDescription") String recipeDescription,
            @RequestParam(name = "category") String category,
            @RequestParam(name = "recipeThumbnail") MultipartFile recipeThumbnail,
            @RequestParam(name= "cookTime") String cookTime,
            @RequestParam(name = "steps") String steps
    ) throws IOException {
        RecipeDto recipeDto = new RecipeDto();
        System.out.println(recipeDto);
        recipeDto.setRecipeName(recipeName);
        recipeDto.setDescription(recipeDescription);
        recipeDto.setCategory(category);
        recipeDto.setCookTime(cookTime);
        recipeDto.setSteps(steps);

        byte[] thumbnailByte = recipeThumbnail.getBytes();
        String thumbnailBase64 = Base64.getEncoder().encodeToString(thumbnailByte);
        recipeDto.setThumbnail(thumbnailBase64);

        Recipe r = recipeService.updateRecipe(recipeDto, recipeId);
        if(r == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }

    @GetMapping("/recipe")
    public ResponseEntity<List<Recipe>> getAllRecipes(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        List<Recipe> recipes = recipeService.getAllRecipes(page, size);
        if(recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(recipes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeResponseDto>> searchRecipe(
            @RequestParam("search") String value,
            @RequestParam("page") int pageNumber,
            @RequestParam("size") int pageSize
    ){
        String search = value != null ? String.valueOf(value).toLowerCase().trim(): "";
        int cleanedPageNumber = Math.max(0, Math.round(pageNumber));
        int cleanedSize = Math.max(1, Math.round(pageSize));
        List<Recipe> recipes = recipeService.fetchSearchResult(search, cleanedPageNumber, cleanedSize);
        List<RecipeResponseDto> foundRecipes = recipes.stream()
                .map(recipe -> {
                    RecipeResponseDto recipeResponseDto = new RecipeResponseDto();
                    recipeResponseDto.setRecipeId(recipe.getRecipeId());
                    recipeResponseDto.setRecipeName(recipe.getRecipeName());
                    recipeResponseDto.setDescription(recipe.getDescription());
                    recipeResponseDto.setCategory(recipe.getCategory());
                    recipeResponseDto.setCookTime(recipe.getCookTime());
                    recipeResponseDto.setIngredients(recipe.getIngredients());
                    if(recipe.getVideo() != null) {
                        recipeResponseDto.setVideoId(recipe.getVideo().getVideoId());
                    }else {
                        recipeResponseDto.setVideoId(null);
                    }

                    if(recipe.getSteps() != null ) {
                        List<String> stepsCopy = new ArrayList<>(recipe.getSteps().getSteps());
                        recipeResponseDto.setSteps(stepsCopy);
                    }
                    recipeResponseDto.setThumbnail(recipe.getThumbnail());
                    recipeResponseDto.setUserid(recipe.getUser().getId());
                    recipeResponseDto.setEmail(recipe.getUser().getEmail());
                    recipeResponseDto.setProfilePicture(recipe.getUser().getProfilePicture());
                    recipeResponseDto.setUsername(recipe.getUser().getUsername());
                    return recipeResponseDto;
                })
                .toList();
    return ResponseEntity.status(HttpStatus.OK).body(foundRecipes);
    }
}
