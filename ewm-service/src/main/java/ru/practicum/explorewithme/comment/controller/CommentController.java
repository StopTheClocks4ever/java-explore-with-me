package ru.practicum.explorewithme.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> publicGetCommentByEventId(@PathVariable Long eventId,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {

        log.info("Получен запрос GET /events/" + eventId + "/comments");
        return commentService.publicGetCommentByEventId(eventId, from, size);
    }

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto privateAddComment(@PathVariable Long userId,
                                 @PathVariable Long eventId,
                                 @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Получен запрос POST /users/" + userId + "/events/" + eventId + "/comments");
        return commentService.privateAddComment(userId, eventId, newCommentDto);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void privateDeleteComment(@PathVariable Long userId,
                                     @PathVariable Long commentId) {
        log.info("Получен запрос DELETE /users/" + userId + "/comments/" + commentId);
        commentService.privateDeleteComment(userId, commentId);
    }

    @PatchMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto privatePatchComment(@PathVariable Long userId,
                                          @PathVariable Long commentId,
                                          @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Получен запрос PATCH /users/" + userId + "/comments/" + commentId);
        return commentService.privatePatchComment(userId, commentId, newCommentDto);
    }
}
