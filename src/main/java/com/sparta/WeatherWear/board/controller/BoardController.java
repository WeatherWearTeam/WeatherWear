package com.sparta.WeatherWear.board.controller;

import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    /* 게시물 작성 */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> createBoard(@RequestBody BoardCreateRequestDto requestDto, @Valid @RequestParam("images") List<MultipartFile> images) {
        return boardService.createBoard(requestDto, images);
    }

//    /* 게시물 id로 조회 */
//    @GetMapping("/find/{user_id}/{board_id}")
//    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> findBoardById(@PathVariable Long user_id, @PathVariable Long board_id) {
//        return boardService.findBoardById(user_id,board_id);
//    }
//
//    /* 게시물 user_id 전체 목록 조회 (페이징) */
//    @GetMapping("/find/{user_id}")
//    public ResponseEntity<ApiResponse<List<BoardCreateResponseDto>>> findBoardByUserId(@PathVariable Long user_id) {
//        return boardService.findBoardByUserId(user_id);
//    }
//
//    /* 게시물 전체 목록 조회 (페이징) & 아이디에 해당하는 값 있으면 수정 기능 추가하기 */
//    @GetMapping("/find-all/{user_id}")
//    public ResponseEntity<ApiResponse<List<BoardCreateResponseDto>>> findBoardAll(@PathVariable Long user_id, @RequestBody BoardfindRequestDto requestDto) {
//        return boardService.findBoardAll(user_id, requestDto);
//    }
//
//    /* 게시물 수정 */
//    @PutMapping("/{user_id}")
//    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> updateBoard(@PathVariable Long user_id, @RequestBody BoardUpdateRequestDto requestDto) {
//        return boardService.updateBoard(user_id, requestDto);
//    }
//
//    /* 게시물 삭제 */
//    @DeleteMapping("/{user_id}")
//    public ResponseEntity<String> removeBoard(@PathVariable Long user_id) {
//        return boardService.removeBoard(user_id);
//    }
}
