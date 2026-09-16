package com.sist.web.domain.exam.restcontroller;

import com.sist.web.domain.exam.dto.AiExamCreateRequest;
import com.sist.web.domain.exam.dto.AiExamCreateResponse;
import com.sist.web.domain.exam.service.AiExamService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exam/ai")
public class AiExamRestController {
    private final AiExamService aiService;

    @PostMapping("generate")
    public ResponseEntity<AiExamCreateResponse> generateQuestion(@RequestBody AiExamCreateRequest request, HttpSession session){
        try{
            Object sessionMid=session.getAttribute("member_id");
            if(sessionMid==null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Integer memberId=Integer.parseInt(String.valueOf(sessionMid));
            AiExamCreateResponse response=aiService.createExam(request,memberId);
            return ResponseEntity.ok(response);
        }catch(IllegalArgumentException ex){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,ex.getMessage(),ex);
		}catch(IllegalStateException ex){
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,ex.getMessage(),ex);
		}catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
