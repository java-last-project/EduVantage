package com.sist.web.domain.notification.repository;

import com.sist.web.domain.notification.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
