package study.querydsl.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
public class Hello {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
