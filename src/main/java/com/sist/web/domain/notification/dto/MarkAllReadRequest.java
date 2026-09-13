package com.sist.web.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MarkAllReadRequest {
    private List<Integer> nos;
}
