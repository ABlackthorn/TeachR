package com.teachr.teachr.dao;

import com.teachr.teachr.models.Subject;

import java.util.List;

public interface SubjectDao {
    Subject getSubject(String id);
    Subject findSubject(String key, String value);
    List<Subject> findSubjects(String key, String value, int comparator);
    List<Subject> findSubjects(List<SearchCriteria> searchCriteria);
    void addSubject(Subject subject);
    void deleteSubject(String id);
    void updateSubject(String id, Subject subject);
}
