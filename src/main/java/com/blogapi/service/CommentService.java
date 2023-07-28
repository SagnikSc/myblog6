package com.blogapi.service;

import com.blogapi.payload.CommentDto;
import com.blogapi.payload.PostDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> findCommentPostById(long postId);

    void deleteCommentById(long postId, long id);

    CommentDto getCommentById(long postId, long id);

    CommentDto updateComment(Long postId, long commentId, CommentDto commentDto);

}
