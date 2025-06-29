package com.gmg.travice.domain.board.controller;

import com.gmg.travice.domain.board.dto.*;

import com.gmg.travice.domain.board.entity.Comment;
import com.gmg.travice.domain.board.service.BoardService;
import com.gmg.travice.domain.user.entity.User;
import com.gmg.travice.domain.user.service.UserService;
import com.gmg.travice.feature.login.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/v1/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final JWTUtil jwtUtil;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllBoards(HttpServletRequest request) {
        List<BoardListDTO> boards = boardService.getAllBoards(request);

        List<Map<String, Object>> result = new ArrayList<>();
        for (BoardListDTO board : boards) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", board.getId());
            item.put("title", board.getTitle());
            item.put("detail", board.getDetail());
            item.put("memberCount", board.getMemberCount());
            item.put("preferenceGender", board.getPreferredGender());
            item.put("preferenceMaxAge", board.getPreferenceMaxAge());
            item.put("preferenceMinAge", board.getPreferenceMinAge());
            item.put("viewCount", board.getViewCount());
            item.put("boardType", board.getBoardType());
            item.put("isDeleted", board.isDeleted());
            item.put("CreatedAt", board.getCreatedAt());

            item.put("location", board.getLocation());
            item.put("startDate", board.getStartDate());
            item.put("endDate", board.getEndDate());

            item.put("writer", board.getWriter());
            item.put("gender", board.getGender());
            item.put("age", board.getAge());
            result.add(item);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardDetails(HttpServletRequest request, @PathVariable Long id) {
        boardService.updateView(id);
        BoardDetailDTO details = boardService.getBoardDetailById(request, id);
        return ResponseEntity.ok(details);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBoard(HttpServletRequest request, @PathVariable Long id, @RequestBody BoardUpdateDTO boardUpdateDTO) {
        boardService.updateView(id);
        boardService.updateBoard(id, boardUpdateDTO);
        BoardDetailDTO details = boardService.getBoardDetailById(request, id);
        return null;
    }

    @PostMapping()
    public ResponseEntity<?> createBoard(HttpServletRequest request, @RequestBody BoardCreateDTO boardCreateDTO) {
        System.out.println("----------------요청 받음--------------------");
        System.out.println(boardCreateDTO);
        boardService.createBoard(request, boardCreateDTO);
        return ResponseEntity.ok(boardCreateDTO);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> createComment(HttpServletRequest request, @RequestBody CommentCreateDTO commentCreateDTO) {
        System.out.println("------------댓글 요청 받음---------------");
        System.out.println(commentCreateDTO);

        // 현재 로그인한 사용자 정보 가져오기
        User currentUser = userService.findUserByJWTToken(request);

        // 게시글 ID 설정 (URL 경로 파라미터 사용)
        commentCreateDTO.setBoardID(commentCreateDTO.getBoardID());

        // 댓글 생성 및 저장
        Comment savedComment = boardService.createComment(request, commentCreateDTO);

        // 응답 데이터 구성 - 프론트엔드 필드명과 일치시킴
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedComment.getId());          // 실제 생성된 댓글 ID 반환
        response.put("author", currentUser.getNickname()); // 작성자 닉네임
        response.put("content", commentCreateDTO.getContent()); // 댓글 내용
        response.put("createdAt", savedComment.getCreatedAt().toString()); // 생성 시간
        response.put("isAuthor", false); // 게시글 작성자와 동일한지 여부

        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        boardService.deleteComment(id);
        Map<String, String> response = new HashMap<>();
        response.put("status", "댓글 삭제 완료");
        return ResponseEntity.ok(response);
    }

    // 댓글 수정
    @PatchMapping("/comment/{id}")
    public ResponseEntity<?> updateComment(@RequestBody CommentDTO commentDTO ,@PathVariable Long id){
        Comment comment = boardService.updateComment(commentDTO);
        return ResponseEntity.ok(comment);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "게시글 삭제 성공");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/updateType/{id}")
    public ResponseEntity<?> updateBoardType(@PathVariable Long id) {
        boardService.updateBoardType(id);
        Map<String, String> response = new HashMap<>();
        response.put("status", "게시판 상태 변경 완료");
        return ResponseEntity.ok(response);

    }

}
