package com.myapp.gallary.repository;

import com.myapp.gallary.Entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {

}
