package org.example.expert.log;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LogActions {
    CREATE_MANAGER("매니저 생성 로그");

    private final String text;
}
