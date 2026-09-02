package com.sist.web.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="course")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private String title;
    @Column(name="instructor_no")
    private int instructorNo;
    private Double star;
    @Column(name="student_count")
    private int studentCount;
    @Column(name="pay_price")
    private int payPrice;
    @Column(name="regular_price")
    private int regularPrice;
    private String content;
    private String images;
    private String thumbnail;
    @ManyToMany
    @JoinTable(
            name="course_tech_mapping",
            joinColumns = @JoinColumn(name="course_no"),
            inverseJoinColumns = @JoinColumn(name="tech_no")
    )
    private List<TechStack> techStacks;
}
