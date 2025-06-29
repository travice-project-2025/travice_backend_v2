package com.gmg.travice.domain.board.repository;

import com.gmg.travice.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findAllByOrderByCreatedAtDesc();
    Board findById(Long id);
}
