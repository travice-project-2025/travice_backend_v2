package com.gmg.travice.domain.board.service;

import com.gmg.travice.domain.board.dto.*;
import com.gmg.travice.domain.board.entity.Comment;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BoardService {
    List<BoardListDTO> getAllBoards(HttpServletRequest request);
    BoardDetailDTO getBoardDetailById(HttpServletRequest request, Long id);
    List<CommentDTO> getComments(HttpServletRequest request, Long id);
    void createBoard(HttpServletRequest request, BoardCreateDTO boardCreateDTO);
    Comment createComment(HttpServletRequest request, CommentCreateDTO commentCreateDTO);
    void deleteComment(Long id);
    Comment updateComment(CommentDTO commentDTO);
    void deleteBoard(Long id);
    void updateView(Long id);
    void updateBoardType(Long id);
    void updateBoard(Long id, BoardUpdateDTO boardUpdateDTO);
}
