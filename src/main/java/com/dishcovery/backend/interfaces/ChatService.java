package com.dishcovery.backend.interfaces;

import com.dishcovery.backend.dto.*;
import com.dishcovery.backend.model.Chat;
import com.dishcovery.backend.model.Community;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface ChatService {
    //Create a community
    Community createCommunity(CommunityRequestDto communityRequestDto);
    // Save the chat
    Chat saveChats(ChatMessage chatMessage, Long id);
    //Get the Chat by Community
    List<ChatResponseDto> getChatsByCommunity(Long communityId);
    //Get the list of Community
    List<CommunityResponseDto> getCommunities(String title);
    // Search for Community
    List<Community> searchCommunity(String str);
    //Save Files
    Chat saveChatFiles(ChatFilesRequestDto chatFilesRequestDto, Long communityId, Principal principal) throws IOException;

}
