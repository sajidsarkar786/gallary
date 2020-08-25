package com.myapp.gallary.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date createdDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "folder_id")
    private List<File> fileList;

    @ManyToOne
    private Folder parentFolder;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "parent_child",
        joinColumns = { @JoinColumn(name = "parent_id") },
        inverseJoinColumns = { @JoinColumn(name = "child_id") })
    private List<Folder> childFolder;
}
