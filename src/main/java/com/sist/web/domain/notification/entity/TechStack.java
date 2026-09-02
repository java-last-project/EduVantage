package com.sist.web.domain.notification.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tech_stack")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TechStack {
    @Id
    private int no;
    private String tech;
    private String category;
}
