package com.gmg.travice.domain.board.service;

import com.gmg.travice.domain.board.dto.*;
import com.gmg.travice.domain.board.entity.Board;
import com.gmg.travice.domain.board.entity.BoardType;
import com.gmg.travice.domain.board.entity.Comment;
import com.gmg.travice.domain.board.repository.BoardRepository;
import com.gmg.travice.domain.board.repository.CommentRepository;
import com.gmg.travice.domain.plan.entity.Plan;
import com.gmg.travice.domain.plan.service.PlanService;
import com.gmg.travice.domain.user.entity.User;
import com.gmg.travice.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final PlanService planService;
    private final CommentRepository commentRepository;

    @Override
    public List<BoardListDTO> getAllBoards(HttpServletRequest request) {
        List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc();

        List<BoardListDTO> result = new ArrayList<>();

        for (Board board : boards) {
            User user = userService.findById(board.getUser().getId());
            Plan plan = planService.findByPlanId(board.getPlan().getId());

            BoardListDTO boardListDTO = new BoardListDTO();
            boardListDTO.setId(board.getId());
            boardListDTO.setTitle(board.getTitle());
            boardListDTO.setDetail(board.getDetail());
            boardListDTO.setMemberCount(board.getMemberCount());
            boardListDTO.setPreferredGender(board.getPreferenceGender());
            boardListDTO.setPreferenceMaxAge(board.getPreferenceMaxAge());
            boardListDTO.setPreferenceMinAge(board.getPreferenceMinAge());
            boardListDTO.setViewCount(board.getViewCount());
            boardListDTO.setBoardType(board.getBoardType());
            boardListDTO.setDeleted(board.isDeleted());
            boardListDTO.setCreatedAt(board.getCreatedAt());

            boardListDTO.setLocation(plan.getCity().getCityName());
            boardListDTO.setStartDate(plan.getStartDate());
            boardListDTO.setEndDate(plan.getEndDate());

            boardListDTO.setWriter(user.getNickname());
            boardListDTO.setGender(user.getGender());
            boardListDTO.setAge(user.getAge());

            result.add(boardListDTO);
        }

        return result;
    }

    @Override
    public BoardDetailDTO getBoardDetailById(HttpServletRequest request, Long id) {
        Board board = boardRepository.findById(id);

        User user = userService.findById(board.getUser().getId());
        Plan plan = planService.findByPlanId(board.getPlan().getId());
        List<CommentDTO> comments = getComments(request, id);


        BoardDetailDTO boardDetailDTO = new BoardDetailDTO();
        boardDetailDTO.setName(user.getNickname());
        boardDetailDTO.setNickname(user.getNickname());
        boardDetailDTO.setAge(user.getAge());
        boardDetailDTO.setGender(user.getGender());
        boardDetailDTO.setProfileImageUrl(user.getProfileImageUrl());
        boardDetailDTO.setTravelCount(user.getTravelCount());
        boardDetailDTO.setCompaniesCount(user.getCompanionCount());

        boardDetailDTO.setStartDate(plan.getStartDate());
        boardDetailDTO.setEndDate(plan.getEndDate());
        boardDetailDTO.setLocation(plan.getCity().getCityName());

        boardDetailDTO.setTitle(board.getTitle());
        boardDetailDTO.setDetail(board.getDetail());
        boardDetailDTO.setMemberCount(board.getMemberCount());
        boardDetailDTO.setPreferenceGender(board.getPreferenceGender());
        boardDetailDTO.setPreferenceMaxAge(board.getPreferenceMaxAge());
        boardDetailDTO.setPreferenceMinAge(board.getPreferenceMinAge());
        boardDetailDTO.setBoardType(board.getBoardType());
        boardDetailDTO.setViewCount(board.getViewCount());
        boardDetailDTO.setCreatedAt(board.getCreatedAt());
        boardDetailDTO.setCost(board.getCost());
        boardDetailDTO.setDeleted(board.isDeleted());

        boardDetailDTO.setComments(comments);

        return boardDetailDTO;
    }

    @Override
    public List<CommentDTO> getComments(HttpServletRequest request, Long id) {
        List<Comment> comments = commentRepository.findByBoardIdOrderByCreatedAtDesc(id);
        List<CommentDTO> result = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            if(comment.getUser().getNickname() != null){
                commentDTO.setWriter(comment.getUser().getNickname());
            }else{
                commentDTO.setWriter(comment.getUser().getName());
            }
            commentDTO.setId(comment.getId());
            commentDTO.setWiriterProfileImage(comment.getUser().getProfileImageUrl());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreatedAt(comment.getCreatedAt());
            commentDTO.setDeleted(comment.isDeleted());
            commentDTO.setGender(comment.getUser().getGender());
            commentDTO.setAge(comment.getUser().getAge());
            result.add(commentDTO);
        }
        return result;
    }

    @Override
    public void createBoard(HttpServletRequest request, BoardCreateDTO boardCreateDTO) {
        User user = userService.findUserByJWTToken(request);
        Plan plan = planService.findByPlanId(boardCreateDTO.getPlanId());
        Board board = new Board();

        board.setTitle(boardCreateDTO.getTitle());
        board.setDetail(boardCreateDTO.getDetail());
        board.setMemberCount(boardCreateDTO.getMemberCount());
        board.setPreferenceMaxAge(boardCreateDTO.getPreferenceMaxAge());
        board.setPreferenceMinAge(boardCreateDTO.getPreferenceMinAge());
        board.setPreferenceGender(boardCreateDTO.getPreferenceGender());
        board.setBoardType(BoardType.OPEN);
        board.setCost(boardCreateDTO.getCost());
        board.setPlan(plan);
        board.setUser(user);
        board.setViewCount(0);
        board.setCreatedAt(LocalDate.now());
        board.setDeleted(false);

        Board savedBoard = boardRepository.save(board);

        return;
    }

    @Override
    public Comment createComment(HttpServletRequest request, CommentCreateDTO commentCreateDTO) {
        User user = userService.findUserByJWTToken(request);
        Board board = boardRepository.findById(commentCreateDTO.getBoardID());
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setBoard(board);
        comment.setContent(commentCreateDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setDeleted(false);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
        return;
    }

    @Override
    public Comment updateComment(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getId())
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        comment.setContent(commentDTO.getContent());

        return commentRepository.save(comment);
    }

    @Override
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id);
        board.setDeleted(true);
        boardRepository.save(board);
        return;
    }

    @Override
    public void updateView(Long id) {
        Board board = boardRepository.findById(id);
        board.setViewCount(board.getViewCount() + 1);
        boardRepository.save(board);
        return;
    }

    @Override
    public void updateBoardType(Long id) {
        Board board = boardRepository.findById(id);
        board.setBoardType(BoardType.CLOSED);
        boardRepository.save(board);
        return;
    }

    @Override
    public void updateBoard(Long id, BoardUpdateDTO boardUpdateDTO) {
        Board board = boardRepository.findById(id);
        board.setTitle(boardUpdateDTO.getTitle());
        board.setDetail(boardUpdateDTO.getDetail());
        board.setMemberCount(boardUpdateDTO.getMemberCount());
        board.setPreferenceMaxAge(boardUpdateDTO.getPreferenceMaxAge());
        board.setPreferenceMinAge(boardUpdateDTO.getPreferenceMinAge());
        board.setPreferenceGender(boardUpdateDTO.getPreferenceGender());
        board.setCost(boardUpdateDTO.getCost());
        boardRepository.save(board);
        return;
    }

}
