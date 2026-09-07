package com.sist.web.domain.book.commons; // 1. 패키지 경로 수정

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginationUtil {
    
    public static Map<String, Object> getPageInfo(int totalCount, int currentPage) {
        int rowSize = 12; 
        int BLOCK = 10; 
        
        int totalpage = (int) Math.ceil(totalCount / (double) rowSize);
        if (totalpage == 0) totalpage = 1;
        
        int startPage = ((currentPage - 1) / BLOCK) * BLOCK + 1;
        int endPage = startPage + BLOCK - 1;
        if (endPage > totalpage) endPage = totalpage;
        
        List<Integer> range = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            range.add(i);
        }
        
        Map<String, Object> map = new HashMap<>(); 
        map.put("curpage", currentPage);
        map.put("totalpage", totalpage);
        map.put("startPage", startPage);
        map.put("endPage", endPage);
        map.put("range", range);
        
        return map;
    }
}