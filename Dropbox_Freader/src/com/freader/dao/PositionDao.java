package com.freader.dao;
 
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;
 
public class PositionDao {
 
    private DbxDatastore datastore;
    private static final String BOOK_PATH_FIELD_NAME = "bookFullPath";
    private static final String PARAGRAPH_FIELD_NAME = "paragraph";
    private static final String TABLE_NAME = "position_android";
     
     
    public PositionDao(DbxDatastoreManager mDatastoreManager) throws DbxException{
        datastore = mDatastoreManager.openDefaultDatastore();
         
    }
     
    public synchronized void savePosition(String bookFullPath, long absoluteParagraph) throws DbxException{
        DbxTable positionTable = datastore.getTable(TABLE_NAME);
        positionTable.insert()
        .set(BOOK_PATH_FIELD_NAME, bookFullPath)
        .set(PARAGRAPH_FIELD_NAME, absoluteParagraph);
        datastore.sync();
    }
     
    public long getPosition(String bookFullPath) throws DbxException{
        DbxTable positionTable = datastore.getTable(TABLE_NAME);
        DbxFields queryParams = new DbxFields().set(BOOK_PATH_FIELD_NAME, bookFullPath);
        DbxTable.QueryResult results = positionTable.query(queryParams);
        DbxRecord firstResult = results.iterator().next();
        return firstResult.getLong(PARAGRAPH_FIELD_NAME);
    }
     
}