package com.ii.blog.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="title", nullable = false)
    private String title;
    @Column(name="description",nullable = false)
    private String description;
    @Column(name="content", nullable = false)
    private String content;
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();
}
