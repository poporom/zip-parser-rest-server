package com.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="results")
public class Result extends BaseEntity {

    @Column
    private String content;

    @Column
    private String file;

    @JsonIgnore
    @ManyToOne
    private Request request;

}