package com.blogapi.service.impl;

import com.blogapi.entity.Comment;
import com.blogapi.entity.Post;
import com.blogapi.exceptions.ResourceNotFoundException;
import com.blogapi.payload.CommentDto;
import com.blogapi.payload.PostDto;
import com.blogapi.repository.CommentRepository;
import com.blogapi.repository.PostRepository;
import com.blogapi.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepo;

    private CommentRepository commentRepo;

    private ModelMapper modelMapper;

    public CommentServiceImpl(PostRepository postRepo, CommentRepository commentRepo, ModelMapper modelMapper) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException(postId));

        Comment comment = mapToEntity(commentDto);

        comment.setPost(post);

        Comment savedComment = commentRepo.save(comment);

        CommentDto dto = mapToDto(savedComment);

        return dto;
    }

    public List<CommentDto> findCommentPostById(long postId){

        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException(postId));

        List<Comment> comments = commentRepo.findByPostId(postId);

        return comments.stream().map(comment->mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public void deleteCommentById(long postId, long id) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException(postId));

        Comment comment = commentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        commentRepo.deleteById(id);

    }

    @Override
    public CommentDto getCommentById(long postId, long id) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException(postId));

        Comment comment = commentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        CommentDto commentDto = mapToDto(comment);

        return commentDto;
    }

    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentDto) {
        // retrieve post entity by id
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException(postId));

        // retrieve comment by id
        Comment comment = commentRepo.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException(commentId));

        Comment comment1 = mapToEntity(commentDto);
        comment1.setId(comment.getId());
        comment1.setPost(post);

        Comment updatedComment = commentRepo.save(comment1);
        return mapToDto(updatedComment);
    }

    Comment mapToEntity(CommentDto commentDto){
        Comment comment = modelMapper.map(commentDto, Comment.class);
        return comment;
    }

    CommentDto mapToDto(Comment comment){
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
        return dto;
    }
}
