package com.example.demo.InputDto.SocialMedia.FollowUnFollow;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FetchFollowInfoDto {
    @NotNull
    private Long profileId;
}
