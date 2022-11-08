package com.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="results")
public class Result extends BaseEntity {

    @Column
    private String content;

    @Column
    @ElementCollection
    private List<String> files;

    @JsonIgnore
    @ManyToOne
    private Request request;

}