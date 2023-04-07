package com.blog.summer.domain;


import com.blog.summer.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post extends BaseTimeEntity {

    private CommentRepository commentRepository;

    @Autowired
    public Post(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public Post() {}

    public void setUser(UserEntity user) {
        this.user=user;
        this.user.addPost(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public List<Comment> getComments() {
        // 댓글 리스트를 조회할 때마다 게시물의 댓글 리스트를 갱신합니다.
        comments = commentRepository.findByPost(this);
        return comments;
    }

    @Builder
    public Post(String title, String content, String categoryName) {
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
    }

}

