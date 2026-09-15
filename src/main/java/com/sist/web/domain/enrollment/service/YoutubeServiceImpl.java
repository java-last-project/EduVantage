package com.sist.web.domain.enrollment.service;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.course.vo.TechStackVO;
import com.sist.web.domain.enrollment.mapper.CourseVideoMapper;
import com.sist.web.domain.enrollment.vo.CourseVideoVO;
import com.sist.web.domain.enrollment.vo.YoutubeVideoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeServiceImpl implements YoutubeService {
    private final CourseVideoMapper vMapper;

    @Value("${youtube.api.key}")
    private String apiKey;

    // DB cache miss일 때만 YouTube API 호출
    private final RestClient restClient=RestClient.create("https://www.googleapis.com");

    @Override
    public List<CourseVideoVO> searchVideos(CourseVO course) {
        // DB cache hit 시 API 호출 생략
        List<CourseVideoVO> videoList=vMapper.courseVideoList(course.getNo());
        if(videoList!=null && !videoList.isEmpty()){
            return videoList;
        }
        String keyword=makeKeyword(course);
        List<YoutubeVideoVO> vList=requestYoutube(keyword);

        int order=1;

        // API 영상을 내부 PK로 저장해 사용자 진도와 연결
        for(YoutubeVideoVO video:vList){
            CourseVideoVO vo=new CourseVideoVO();
            vo.setCourse_no(course.getNo());
            vo.setVideoId(video.getVideoId());
            vo.setTitle(video.getTitle());
            vo.setThumbnail(video.getThumbnail());
            vo.setVOrder(order++);
            vMapper.courseVideoInsert(vo);
        }
        // Identity PK가 반영된 영상 목록 재조회
        return vMapper.courseVideoList(course.getNo());
    }

    private List<YoutubeVideoVO> requestYoutube(String keyword){
        // 한국어권 임베드 영상만 검색
        JsonNode response=restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/youtube/v3/search")
                        .queryParam("part","snippet")
                        .queryParam("q",keyword)
                        .queryParam("type","video")
                        .queryParam("videoEmbeddable","true")
                        .queryParam("relevanceLanguage","ko")
                        .queryParam("regionCode","KR")
                        .queryParam("maxResults",5)
                        .queryParam("key",apiKey)
                        .build())
                .retrieve()
                .body(JsonNode.class);
        List<YoutubeVideoVO> list=new ArrayList<>();
        if(response==null || response.get("items")==null){
            return list;
        }
        for(JsonNode item:response.get("items")){
            YoutubeVideoVO vo=new YoutubeVideoVO();
            vo.setVideoId(
                    item.path("id")
                            .path("videoId")
                            .asString()
            );
            vo.setTitle(
                    item.path("snippet")
                            .path("title")
                            .asString()
            );
            vo.setThumbnail(
                    item.path("snippet")
                            .path("thumbnails")
                            .path("high")
                            .path("url")
                            .asString()
            );
            list.add(vo);
        }
        return list;
    }

    private String makeKeyword(CourseVO course){
        List<String> keywords=new ArrayList<>();
        if(course.getTechList()!=null){
            for(TechStackVO tech:course.getTechList()){
                if(tech.getTech()==null || tech.getTech().isBlank()){
                    continue;
                }
                String techName=tech.getTech().trim();

                // 긴 기술명 제외로 검색 정확도 유지
                if(techName.length()>30){
                    continue;
                }

                if(!keywords.contains(techName)){
                    keywords.add(techName);
                }

                if(keywords.size()>=3){
                    break;
                }

            }
        }

        if(course.getCategoryList()!=null){
            for(String category:course.getCategoryList()){
                if(category==null || category.isBlank()){
                    continue;
                }
                if(!keywords.contains(category)){
                    keywords.add(category);
                }
                break;
            }
        }
        return String.join(" ",keywords)+"강의";
    }
}
