package com.javaweb.service;

import com.javaweb.model.FavouriteListingDTO;

import java.util.List;

public interface FavouriteListingService {
    boolean addToFavourite(FavouriteListingDTO request);
    boolean removeFromFavourite(FavouriteListingDTO request);
    List<FavouriteListingDTO> getFavouriteList(Integer userId);
}
