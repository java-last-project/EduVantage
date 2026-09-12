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

    // 외부 HTTP API 호출용
    private final RestClient restClient=RestClient.create("https://www.googleapis.com");

    @Override
    public List<CourseVideoVO> searchVideos(CourseVO course) {
        List<CourseVideoVO> videoList=vMapper.courseVideoList(course.getNo());
        if(videoList!=null && !videoList.isEmpty()){
            return videoList;
        }
        String keyword=makeKeyword(course);
        List<YoutubeVideoVO> vList=requestYoutube(keyword);

        videoList=new ArrayList<>();
        int order=1;
        for(YoutubeVideoVO video:vList){
            CourseVideoVO vo=new CourseVideoVO();
            vo.setCourse_no(course.getNo());
            vo.setVideoId(video.getVideoId());
            vo.setTitle(video.getTitle());
            vo.setThumbnail(video.getThumbnail());
            vo.setVOrder(order++);
            vMapper.courseVideoInsert(vo);
            videoList.add(vo);
        }
        return videoList;
    }

    private List<YoutubeVideoVO> requestYoutube(String keyword){
        // 요청 주소 조립
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

                // 가비지데이터 거름망
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
