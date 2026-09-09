package com.sist.web.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MarkReadRequest {
    private List<Integer> nos;
}
