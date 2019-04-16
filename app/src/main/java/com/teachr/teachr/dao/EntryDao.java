package com.teachr.teachr.dao;

import com.teachr.teachr.models.Entry;

import java.util.List;

public interface EntryDao {

    Entry getEntry(String id);
    Entry findEntry(String key, String value);
    List<Entry> findEntries(String key, String value, int comparator);
    List<Entry> findEntries(List<SearchCriteria> searchCriteria);
    void addEntry(Entry entry);
    void deleteEntry(String id);
    void updateEntry(String id, Entry entry);

}
