package com.nilo.communityapplication.controller;

import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.requests.CommunityRequest;
import com.nilo.communityapplication.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping
    public Community createNewCommunity(@RequestBody CommunityRequest communityRequest) throws Exception {
        return communityService.createCommunity(communityRequest);
    }

}
