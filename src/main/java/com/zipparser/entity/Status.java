package com.zipparser.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "status")
public class Status extends BaseEntity {

    @Column
    private String name;

}
