package com.example.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChatRoomRequest {
    private String roomName;
    private List<String> participantIds; // 초대할 유저 ID 목록
}